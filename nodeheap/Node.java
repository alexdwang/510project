/* File Tuple.java */

package nodeheap;

import java.io.*;
import java.lang.*;
import global.*;
import heap.*;


public class Node extends Tuple{
   /**
    * Class constructor
    * Creat a new tuple with length = max_size,tuple offset = 0.
    */

   public static int FldID_LL = 1;
   public static int FldID_Desc = 2;
   public static int FldID_Label = 3;
   public static short Fld_CNT = 3;

   public  Node()
   {
        // Creat a new tuple
        super();
   }

  public Node(byte[] anode, int offset){
    super(anode, offset, max_size);
  }

  public Node(Node fromNode){
    super(fromNode);
  }

  public Node(Tuple fromTuple)
  throws IOException,
  FieldNumberOutOfBoundException,
  InvalidTypeException,
  InvalidTupleSizeException
  {
    super(fromTuple.getTupleByteArray(),0, fromTuple.getLength());
    int leftsize = fromTuple.getLength() - ( 4 + 4 * 5 );
    short[] labelSize = {(short)leftsize};
    AttrType[] types = {
      new AttrType(AttrType.attrInteger), 
      new AttrType(AttrType.attrDesc),
      new AttrType(AttrType.attrString)
    };
    setHdr(Fld_CNT, types, labelSize);
    int truesize = getIntFld(FldID_LL);
    labelSize[0] = (short)truesize;
    setHdr(Fld_CNT, types, labelSize);
  }

  public String getLabel() 
  throws IOException,
  FieldNumberOutOfBoundException
  {
    return getStrFld(FldID_Label);
  }

  public Descriptor getDesc() 
  throws IOException,
  FieldNumberOutOfBoundException
  {
    return getDescFld(FldID_Desc);
  }

  public void setLabel(String alabel) 
  throws IOException,
  FieldNumberOutOfBoundException
  {
    setIntFld(FldID_LL, alabel.length());
    setStrFld(FldID_Label, alabel);
  }

  public void setDesc(Descriptor adesc) 
  throws IOException,
  FieldNumberOutOfBoundException
  {
    setDescFld(FldID_Desc, adesc);
  }

  public byte[] getNodeByteArray(){
    return getTupleByteArray(); 
  }

  public void print()
      throws IOException,
  FieldNumberOutOfBoundException
  {
    Descriptor descriptor = getDesc();
    System.out.println(getLabel() + " : " + descriptor.toString());
  }

  public void nodeCopy(Node fromNode){
    super.tupleCopy(fromNode);
  }

  public void nodeInit(byte[] anode, int offset){
    super.tupleInit(anode, offset, max_size);
  }

  public void nodeSet(byte[] fromnode, int offset){
    super.tupleSet(fromnode, offset, max_size);
  }
}

