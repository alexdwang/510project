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
   private String label;
   private Descriptor descriptor;

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

  public String getLabel(){
    return label;
  }

  public Descriptor getDesc(){
    return descriptor;
  }

  public void setLabel(String alabel){
    label = alabel;
  }

  public void setDesc(Descriptor adesc){
    descriptor = adesc;
  }

  public byte[] getNodeByteArray(){
    return getTupleByteArray(); 
  }

  public void print()
      throws IOException
  {
    System.out.println(descriptor.toString());
  }

  public double getSize(){
    return descriptor.getLength();
  }

  public void nodeCopy(Node fromNode){
    super.tupleCopy(fromNode);
    label = fromNode.getLabel();
    descriptor.copyDesc(fromNode.getDesc());
  }

  public void nodeInit(byte[] anode, int offset){
    super.tupleInit(anode, offset, max_size);
  }

  public void nodeSet(byte[] fromnode, int offset){
    super.tupleSet(fromnode, offset, max_size);
  }
}

