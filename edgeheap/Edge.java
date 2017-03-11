/* File Edge.java */

package edgeheap;

import java.io.*;
import java.lang.*;
import global.*;
import heap.*;


public class Edge extends Tuple{

	private NID source;
	private NID destination;
	private String label;
	private int weight;


	public static int EdgeFld_Wgt = 1;
	public static int EdgeFld_Src_ll = 2;
	public static int EdgeFld_Dst_ll = 3;
	public static int EdgeFld_ll = 4;
	public static int EdgeFld_src_Lbl = 5;
	public static int EdgeFld_dst_lbl = 6;
	public static int EdgeFld_Lbl = 7;
	public static short Fld_CNT = 7;

	public Edge(){
		super();
	}

	public Edge(byte[] aedge, int offset){
		super(aedge, offset,max_size);
	}

	public Edge(Edge fromEdge)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		super(fromEdge);
	}

	public Edge(Tuple fromTuple)
	throws IOException, 
	InvalidTypeException, 
	InvalidTupleSizeException,
	FieldNumberOutOfBoundException
	{
		super(fromTuple.getTupleByteArray(),0, fromTuple.getLength());
		int leftsize = fromTuple.getLength() -  4 * 4;
		short[] labelSize = {10, 10, 10};
		AttrType[] types = {
	      new AttrType(AttrType.attrInteger), 
	      new AttrType(AttrType.attrInteger), 
	      new AttrType(AttrType.attrInteger), 
	      new AttrType(AttrType.attrInteger), 
	      new AttrType(AttrType.attrString),
	      new AttrType(AttrType.attrString),
	      new AttrType(AttrType.attrString)
	    };
	    setHdr(Fld_CNT, types, labelSize);
	    labelSize[0] = (short)getIntFld(EdgeFld_Src_ll);
	    labelSize[1] = (short)getIntFld(EdgeFld_Dst_ll);
	    labelSize[2] = (short)getIntFld(EdgeFld_ll);
	    setHdr(Fld_CNT, types, labelSize);
	}

	public String getSource()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		String slbl = getStrFld(EdgeFld_src_Lbl);
		return slbl;
	}

	public String getDestination()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		String slbl = getStrFld(EdgeFld_dst_lbl);
		return slbl;
	}

	public String getLabel()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		String slbl = getStrFld(EdgeFld_Lbl);
		return slbl;
	}

	public int getWeight()
	throws IOException,
	FieldNumberOutOfBoundException{
		int weight = getIntFld(EdgeFld_Wgt);
		return weight;
	}

	public Edge setLabel(String alabel)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setIntFld(EdgeFld_ll, alabel.length());
		setStrFld(EdgeFld_Lbl, alabel);
		return this;
	}

	public Edge setWeight(int aweight)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setIntFld(EdgeFld_Wgt, aweight);
		return this;
	}

	public Edge setSource(String sourcelbl)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setIntFld(EdgeFld_Src_ll, sourcelbl.length());
		setStrFld(EdgeFld_src_Lbl, sourcelbl);
		return this;
	}

	public Edge setDestination(String dstlbl)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setIntFld(EdgeFld_Dst_ll, dstlbl.length());
		setStrFld(EdgeFld_dst_lbl, dstlbl);
		return this;
	}

	public byte [] getEdgeByteArray()
	{
		return getTupleByteArray(); 
	}

	public void print()
	      throws IOException,
	FieldNumberOutOfBoundException
	{
		System.out.println("{" + getLabel() + " " + getSource()+ " "  + getDestination()+ " "  + getWeight() + "}");
	}

	public void edgeCopy(Edge fromEdge){
		super.tupleCopy(fromEdge);
	}

	public void edgeInit(byte[] aedge, int offset){
		super.tupleInit(aedge, offset, max_size);
	}

	public void edgeSet(byte[] fromedge, int offset){
		super.tupleSet(fromedge, offset, max_size);
	}
}