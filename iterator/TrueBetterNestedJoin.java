package iterator;

import heap.*;
import global.*;
import diskmgr.*;
import bufmgr.*;
import index.*;
import java.io.*;
import java.util.ArrayList;

/**
 * This file contains the interface for the sort_merg joins.
 * We name the two relations being joined as R and S.
 * This file contains an implementation of the sort merge join
 * algorithm as described in the Shapiro paper. It makes use of the external
 * sorting utility to generate runs, and then uses the iterator interface to
 * get successive tuples for the final merge.
 */
public class TrueBetterNestedJoin extends Iterator implements GlobalConst
{
  private AttrType  _in1[], _in2[];
  private  int        in1_len, in2_len;
  private  FileScan  p_i1,        // pointers to the two iterators. If the
    p_i2;               // inputs are sorted, then no sorting is done
  private  TupleOrder  _order;                      // The sorting order.
  private  CondExpr  OutputFilter[];
  
  private  boolean      get_from_in1, get_from_in2;        // state variables for get_next
  private  int        jc_in1, jc_in2;
  private  short     inner_str_sizes[];
  private  Tuple     TempTuple1,  TempTuple2;
  private  Tuple     tuple1;
  private  boolean       done;
  private  byte    _bufs1[][],_bufs2[][];
  private  int        _n_pages; 
  private  AttrType   sortFldType;
  private  int        t1_size, t2_size;
  private  Tuple     Jtuple;
  private  FldSpec   perm_mat[];
  private  int        nOutFlds;
  
  /**
   *constructor,initialization
   *@param in1[]   Array containing field types of R
   *@param len_in1  # of columns in R
   *@param s1_sizes  shows the length of the string fields in R.
   *@param in2[]  Array containing field types of S
   *@param len_in2  # of columns in S
   *@param s2_sizes shows the length of the string fields in S
   *@param sortFld1Len the length of sorted field in R
   *@param sortFld2Len the length of sorted field in S
   *@param join_col_in1  The col of R to be joined with S
   *@param join_col_in2  the col of S to be joined with R
   *@param amt_of_mem   IN PAGES
   *@param am1  access method for left input to join
   *@param am2  access method for right input to join
   *@param in1_sorted  is am1 sorted?
   *@param in2_sorted  is am2 sorted?
   *@param order the order of the tuple: assending or desecnding?
   *@param outFilter[]  Ptr to the output filter
   *@param proj_list shows what input fields go where in the output tuple
   *@param n_out_flds number of outer relation fileds
   *@exception JoinNewFailed allocate failed
   *@exception JoinLowMemory memory not enough
	 *@exception SortException exception from sorting
	 *@exception TupleUtilsException exception from using tuple utils
   *@exception IOException some I/O fault
   */
  public TrueBetterNestedJoin(AttrType    in1[],               
		   int     len_in1,                        
		   short   s1_sizes[],
		   AttrType    in2[],                
		   int     len_in2,                        
		   short   s2_sizes[],
		   
		   int     join_col_in1,                
		   int      sortFld1Len,
		   int     join_col_in2,                
		   int      sortFld2Len,
		   
		   int     amt_of_mem,               
		   FileScan     am1,                
		   FileScan     am2,                
		   
		   boolean     in1_sorted,                
		   boolean     in2_sorted,                
		   TupleOrder order,
		   
		   CondExpr  outFilter[],                
		   FldSpec   proj_list[],
		   int       n_out_flds
		   )
    throws JoinNewFailed ,
	   JoinLowMemory,
	   SortException,
	   TupleUtilsException,
	   IOException
		   
    {
      _in1 = new AttrType[in1.length];
      _in2 = new AttrType[in2.length];
      System.arraycopy(in1,0,_in1,0,in1.length);
      System.arraycopy(in2,0,_in2,0,in2.length);
      in1_len = len_in1;
      in2_len = len_in2;
      
      Jtuple = new Tuple();
      AttrType[] Jtypes = new AttrType[n_out_flds];
      short[]    ts_size = null;
      perm_mat = proj_list;
      nOutFlds = n_out_flds;
      try {
	ts_size = TupleUtils.setup_op_tuple(Jtuple, Jtypes,
					    in1, len_in1, in2, len_in2,
					    s1_sizes, s2_sizes, 
					    proj_list,n_out_flds );
      }catch (Exception e){
	throw new TupleUtilsException (e, "Exception is caught by TrueBetterNestedJoin.java");
      }
      
      int n_strs2 = 0;
      
      for (int i = 0; i < len_in2; i++) if (_in2[i].attrType == AttrType.attrString) n_strs2++;
      inner_str_sizes = new short [n_strs2];
    
      for (int i = 0; i < n_strs2; i++)    inner_str_sizes[i] = s2_sizes[i];
        
      p_i1 = am1;
      p_i2 = am2;
      
      OutputFilter = outFilter;
      _order       = order;
      jc_in1       = join_col_in1;
      jc_in2       = join_col_in2;
      get_from_in1 = true;
      get_from_in2 = true;
      
      
      // Allocate memory for the temporary tuples
      try{
      tuple1 = p_i1.get_next();
      }catch(Exception e){
        e.printStackTrace();
      }
      
      
      done               = false;

      
      sortFldType = _in1[jc_in1-1];
      
      // Now, that stuff is setup, all we have to do is a get_next !!!!
    }
  
  /**
   *  The tuple is returned
   * All this function has to do is to get 1 tuple from one of the Iterators
   * (from both initially), use the sorting order to determine which one
   * gets sent up. Amit)
   * Hmmm it seems that some thing more has to be done in order to account
   * for duplicates.... => I am following Raghu's 564 notes in order to
   * obtain an algorithm for this merging. Some funda about 
   *"equivalence classes"
   *@return the joined tuple is returned
   *@exception IOException I/O errors
   *@exception JoinsException some join exception
   *@exception IndexException exception from super class
   *@exception InvalidTupleSizeException invalid tuple size
   *@exception InvalidTypeException tuple type not valid
   *@exception PageNotReadException exception from lower layer
   *@exception TupleUtilsException exception from using tuple utilities
   *@exception PredEvalException exception from PredEval class
   *@exception SortException sort exception
   *@exception LowMemException memory error
   *@exception UnknowAttrType attribute type unknown
   *@exception UnknownKeyTypeException key type unknown
   *@exception Exception other exceptions
   */

  public Tuple get_next() 
    throws IOException,
	   JoinsException ,
	   IndexException,
	   InvalidTupleSizeException,
	   InvalidTypeException, 
	   PageNotReadException,
	   TupleUtilsException, 
	   PredEvalException,
	   SortException,
	   LowMemException,
	   UnknowAttrType,
	   UnknownKeyTypeException,
	   Exception
    {
      
      int    comp_res;
      Tuple  _tuple2;
      while(true){
    	  if (done) return null;

        if((_tuple2 = p_i2.get_next()) == null){
          if((tuple1 = p_i1.get_next()) == null){
              done = true;
              return null;
          }
          p_i2.restart();
          if((_tuple2 = p_i2.get_next()) == null){
            done = true;
            return null;
          }
         }else{
          if(tuple1 == null){
            done = true;
            return null;
          }
         }

         comp_res = TupleUtils.CompareTupleWithTuple(sortFldType, tuple1,
              jc_in1, _tuple2, jc_in2);

         if(comp_res != 0) continue;
	  
    	  if (PredEval.Eval(OutputFilter, tuple1, _tuple2, _in1, _in2) == true)
    		    {
    		      Projection.Join(tuple1, _in1, 
    				      _tuple2, _in2, 
    				      Jtuple, perm_mat, nOutFlds);
              p_i2.restart();
              tuple1 = p_i1.get_next();
    		      return Jtuple;
    		    }
    		}
  }

  /** 
   *implement the abstract method close() from super class Iterator
   *to finish cleaning up
   *@exception IOException I/O error from lower layers
   *@exception JoinsException join error from lower layers
   *@exception IndexException index access error 
   */
  public void close() 
    throws JoinsException, 
	   IOException,
	   IndexException 
    {
      if (!closeFlag) {
	
	try {
	  p_i1.close();
	  p_i2.close();
	}catch (Exception e) {
	  throw new JoinsException(e, "TrueBetterNestedJoin.java: error in closing iterator.");
	}
	closeFlag = true;
      }
    }
  
}


