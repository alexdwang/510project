/* File Edge.java */

package edgeheap;

import java.io.*;
import java.lang.*;
import global.*;


public class Edge extends Tuple{

	private NID source;
	private NID destination;
	private String label;
	private int weight;

	public static int EdgeFld_Src_SN = 1;
	public static int EdgeFld_Src_PN = 2;
	public static int EdgeFld_Dst_SN = 3;
	public static int EdgeFld_Dst_PN = 4;
	public static int EdgeFld_Wgt = 5;
	public static int EdgeFld_Lbl = 6;

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

	public NID getSource()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		NID source = new NID();
		source.pageNo.pid = getIntFld(EdgeFld_Src_PN);
		source.slotNo = getIntFld(EdgeFld_Src_SN);
		return source;
	}

	public NID getDestination()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		NID destination = new NID();;
		destination.pageNo.pid = getIntFld(EdgeFld_Dst_PN);
		destination.slotNo = getIntFld(EdgeFld_Dst_SN);
		return destination;
	}

	public String getLabel()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		String label = getStrFld(EdgeFld_Lbl);
		return label;
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

	public Edge setSource(NID sourceID)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setIntFld(EdgeFld_Src_PN, sourceID.pageNo.pid);
		setIntFld(EdgeFld_Src_SN, sourceID.slotNo);
		return this;
	}

	public Edge setDestination(NID destID)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setIntFld(EdgeFld_Dst_PN, destID.pageNo.pid);
		setIntFld(EdgeFld_Dst_SN, destID.slotNo);
		return this;
	}

	public byte [] getEdgeByteArray()
	{
		return getTupleByteArray(); 
	}

	public void print()
	      throws IOException
	{
		System.out.println("{" +  "}");
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