package graphtools;

import java.lang.*;
import heap.*;
import edgeheap.Edge;
import global.*;
import iterator.*;
import index.*;

public class SortMergeUtil implements GlobalConst {

	private static IndexScan generateEdgeIndexScan(int weight, String label){
		IndexScan am = null;
		IndexType b_index = new IndexType (IndexType.B_Index);

		FldSpec [] Sprojection = {
			new FldSpec(new RelSpec(RelSpec.outer), 1),
			new FldSpec(new RelSpec(RelSpec.outer), 2),
			new FldSpec(new RelSpec(RelSpec.outer), 3),
			new FldSpec(new RelSpec(RelSpec.outer), 4),
			new FldSpec(new RelSpec(RelSpec.outer), 5)
		};

		CondExpr[] expr = {
			new CondExpr(),
			new CondExpr()
		};
		String index_file_name = "";
		int index_fld_no = 0;
		if(weight != -1){
			expr[0].next  = null;
	    	expr[0].op    = new AttrOperator(AttrOperator.aopLE);
	    	expr[0].type1 = new AttrType(AttrType.attrSymbol);
		    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),Edge.FLD_WGT);
		    expr[0].type2 = new AttrType(AttrType.attrInteger);
		    expr[0].operand2.integer = weight;
		    expr[1] = null;
		    index_file_name = BTManager.edgeweightbtree_filename;
		    index_fld_no = Edge.FLD_WGT;
		}else{
			expr[0].next  = null;
	    	expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
	    	expr[0].type1 = new AttrType(AttrType.attrSymbol);
		    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),Edge.FLD_LABEL);
		    expr[0].type2 = new AttrType(AttrType.attrString);
		    expr[0].operand2.string = label;
		    expr[1] = null;
		    index_file_name = BTManager.edgelabelbtree_filename;
		    index_fld_no = Edge.FLD_LABEL;
		}

		try {
			am  = new IndexScan( b_index, HFManager.edgefilename, index_file_name, 
					  Edge.FLD_TYPES, Edge.STR_FLD_SIZE, Edge.FLD_CNT, Edge.FLD_CNT,
					  Sprojection, expr, index_fld_no, false);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return am;

	}

	public static void tester(){

	}

	public static void triangleQuery(int[] weights, String[] labels, boolean distinct){
		System.out.println("Query Plan used:");
		System.out.println("");

		TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);

		FldSpec [] Sprojection = {
			new FldSpec(new RelSpec(RelSpec.outer), 1),
			new FldSpec(new RelSpec(RelSpec.outer), 2),
			new FldSpec(new RelSpec(RelSpec.outer), 3),
			new FldSpec(new RelSpec(RelSpec.outer), 4),
			new FldSpec(new RelSpec(RelSpec.outer), 5)
		};

		CondExpr [] expr0 = {
			new CondExpr(),
			new CondExpr()
		};

		if(weights[0] == -1){
			expr0[0].next  = null;
			expr0[0].op    = new AttrOperator(AttrOperator.aopEQ);
			expr0[0].type1 = new AttrType(AttrType.attrSymbol);
	    	expr0[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),Edge.FLD_LABEL);
		    expr0[0].type2 = new AttrType(AttrType.attrString);
	    	expr0[0].operand2.string = labels[0];
	    	expr0[1]  = null;
		}else{
			expr0[0].next  = null;
			expr0[0].op    = new AttrOperator(AttrOperator.aopLE);
			expr0[0].type1 = new AttrType(AttrType.attrSymbol);
	    	expr0[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),Edge.FLD_WGT);
		    expr0[0].type2 = new AttrType(AttrType.attrInteger);
	    	expr0[0].operand2.integer = weights[0];
	    	expr0[1]  = null;
		}
		

		FileScan am = null;

		try {
			am  = new FileScan(HFManager.edgefilename, Edge.FLD_TYPES, Edge.STR_FLD_SIZE, 
					  Edge.FLD_CNT, Edge.FLD_CNT,
					  Sprojection, expr0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		IndexScan am2 = generateEdgeIndexScan(weights[1],labels[1]);


		CondExpr [] expr = {
			new CondExpr(),
			new CondExpr(),
			new CondExpr(),
			new CondExpr(),
			new CondExpr()
		};
		
		
		expr[0].next  = null;
		expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
    	expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),Edge.FLD_DST_LABEL);
	    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    	expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),Edge.FLD_SRC_LABEL);
		expr[1] = null;
		expr[2] = null;
		expr[3] = null;
		expr[4] = null;
    	

		FldSpec [] proj_list = { 
			new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_SRC_LABEL), 
			new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_DST_LABEL), 
			new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_DST_LABEL)
		};

    	SortMerge sm = null;
		try {
			sm = new SortMerge(Edge.FLD_TYPES, Edge.FLD_CNT, Edge.STR_FLD_SIZE,
				Edge.FLD_TYPES, Edge.FLD_CNT, Edge.STR_FLD_SIZE,
				Edge.FLD_DST_LABEL, Edge.LABEL_MAX_LENGTH, 
				Edge.FLD_SRC_LABEL, Edge.LABEL_MAX_LENGTH, 
				300,
				am, am2, 
				false, false, ascending,
				expr, proj_list, 3);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// build middle tuple
		AttrType [] jtype = { 
			new AttrType (AttrType.attrString), 
			new AttrType (AttrType.attrString),
			new AttrType (AttrType.attrString)
		};

		short[] temp_str_size = { Edge.LABEL_MAX_LENGTH, Edge.LABEL_MAX_LENGTH, Edge.LABEL_MAX_LENGTH };

		String temp_file_name = "temp_jiandan_farker";
		Heapfile temp_file = null;
		try{
			temp_file = new Heapfile(temp_file_name);
		}catch (Exception e) {
			e.printStackTrace();
		}
		Tuple t;
		try {
			while ((t = sm.get_next()) != null) {
       			//generate tempfile
       			
       			//t.print(jtype);
       			temp_file.insertRecord(t.getTupleByteArray());
      		}
      		sm.close();
		}catch (Exception e) {
			e.printStackTrace();
		}


		db.clearPerTask();//flush to reduce buffer occupation

		FldSpec [] Temprojection = {
			new FldSpec(new RelSpec(RelSpec.outer), 1),
			new FldSpec(new RelSpec(RelSpec.outer), 2),
			new FldSpec(new RelSpec(RelSpec.outer), 3)
		};

		//do second join

		//generate iterator for temp file
		try {
				am  = new FileScan(temp_file_name, jtype, temp_str_size, 
						  (short)3, (short)3,
						  Temprojection, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//generate iterator for their edges
		am2 = generateEdgeIndexScan(weights[2], labels[2]);

		//second time merge expr

    	CondExpr [] expr1 = {
			new CondExpr(),
			new CondExpr(),
			new CondExpr()
		};
		expr1[0].next  = null;
		expr1[0].op    = new AttrOperator(AttrOperator.aopEQ);
		expr1[0].type1 = new AttrType(AttrType.attrSymbol);
    	expr1[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),(short)3);//use src of first edge
	    expr1[0].type2 = new AttrType(AttrType.attrSymbol);
    	expr1[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),Edge.FLD_SRC_LABEL);
    	expr1[1] = null;

    	FldSpec [] proj_list1 = { 
			new FldSpec(new RelSpec(RelSpec.outer), 1), 
			new FldSpec(new RelSpec(RelSpec.outer), 2), 
			new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_SRC_LABEL)
		};
		

		try {
			sm = new SortMerge(
				jtype, (short)3, temp_str_size,  // left file
				Edge.FLD_TYPES, Edge.FLD_CNT, Edge.STR_FLD_SIZE,
				(short)1, Edge.LABEL_MAX_LENGTH, 
				Edge.FLD_DST_LABEL, Edge.LABEL_MAX_LENGTH, 
				300,
				am, am2, 
				false, false, ascending,
				expr1, proj_list1, 3);
		}
		catch (Exception e) {
			e.printStackTrace();
		}



		AttrType [] jtype1 = { 
			new AttrType (AttrType.attrString), 
			new AttrType (AttrType.attrString),
			new AttrType (AttrType.attrString)
		};

		if(!distinct){
			try {
				while ((t = sm.get_next()) != null) {
	       			t.print(jtype1);
	      		}
	      		sm.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			String tfname = "temp_jiandan_farker1";
			Heapfile f1 = null;
			try {
				f1 = new Heapfile(tfname);
				while ((t = sm.get_next()) != null) {
	       			//t.print(jtype1);
	       			f1.insertRecord(t.getTupleByteArray() );
	      		}
	      		sm.close();
			}catch (Exception e) {
				e.printStackTrace();
			}

			// nested loop to kill duplication
			FileScan am3 = null;
			try {
				am  = new FileScan(tfname, jtype, temp_str_size, 
						  (short)3, (short)3,
						  Temprojection, null);
			}catch (Exception e) {
				e.printStackTrace();
			}

			Tuple left = null;
			Tuple right = null;
			int cntl = 0;
			try{
				while((left = am.get_next()) != null){
					cntl++;
					int cntr = 0;
					am3  = new FileScan(tfname, jtype, temp_str_size, 
						  (short)3, (short)3,
						  Temprojection, null);
					while((right = am3.get_next()) != null){
						cntr++;
						if(left.getStrFld(1).compareTo(right.getStrFld(2)) == 0
							    && left.getStrFld(2).compareTo(right.getStrFld(3)) == 0
								&& left.getStrFld(3).compareTo(right.getStrFld(1)) == 0
							|| left.getStrFld(1).compareTo(right.getStrFld(3)) == 0
							    && left.getStrFld(2).compareTo(right.getStrFld(1)) == 0
								&& left.getStrFld(3).compareTo(right.getStrFld(2)) == 0
							|| left.getStrFld(1).compareTo(right.getStrFld(1)) == 0
							    && left.getStrFld(2).compareTo(right.getStrFld(2)) == 0
								&& left.getStrFld(3).compareTo(right.getStrFld(3)) == 0){
							if(cntr >= cntl){//can print
								left.print(jtype1);
							}
							break;
						}
					}
					am3.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}	

			try{
				f1.deleteFile();
			}catch (Exception e) {
				e.printStackTrace();
			}


		}
		


		//destroy tempfile

		try{
			temp_file.deleteFile();
		}catch (Exception e) {
			e.printStackTrace();
		}
		

	}


}