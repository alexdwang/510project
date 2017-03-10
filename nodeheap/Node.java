/* File Tuple.java */

package nodeheap;

import java.io.*;
import java.lang.*;
import global.*;


public class Node extends Tuple{
   /**
    * Class constructor
    * Creat a new tuple with length = max_size,tuple offset = 0.
    */

   public static int FldID_Desc = 1;
   public static int FldID_Label = 2;

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
    System.out.println(descriptor.toString());
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

