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

	public static int FLD_ID = 1;
	public static int FLD_WGT = 2;
	public static int FLD_SRC_LABEL = 3;
	public static int FLD_DST_LABEL = 4;
	public static int FLD_LABEL = 5;
	public static short FLD_CNT = 5;
	public static short LABEL_MAX_LENGTH = 32;

	public static AttrType[] FLD_TYPES = {
		new AttrType(AttrType.attrInteger),
	    new AttrType(AttrType.attrInteger), 
	    new AttrType(AttrType.attrString),
	    new AttrType(AttrType.attrString),
	    new AttrType(AttrType.attrString)
	};

	public static short[] STR_FLD_SIZE = {
		LABEL_MAX_LENGTH, 
		LABEL_MAX_LENGTH, 
		LABEL_MAX_LENGTH};

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
	    setHdr(FLD_CNT, FLD_TYPES, STR_FLD_SIZE);
	}

	public String getSource()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		String slbl = getStrFld(FLD_SRC_LABEL);
		return slbl;
	}

	public String getDestination()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		String slbl = getStrFld(FLD_DST_LABEL);
		return slbl;
	}

	public String getLabel()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		String slbl = getStrFld(FLD_LABEL);
		return slbl;
	}

	public int getWeight()
	throws IOException,
	FieldNumberOutOfBoundException{
		int weight = getIntFld(FLD_WGT);
		return weight;
	}

	public int getID()
	throws IOException,
	FieldNumberOutOfBoundException{
		int id = getIntFld(FLD_ID);
		return id;
	}

	public Edge setLabel(String alabel)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setStrFld(FLD_LABEL, alabel);
		return this;
	}

	public Edge setID(int id)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setIntFld(FLD_ID, id);
		return this;
	}

	public Edge setWeight(int aweight)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setIntFld(FLD_WGT, aweight);
		return this;
	}

	public Edge setSource(String sourcelbl)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setStrFld(FLD_SRC_LABEL, sourcelbl);
		return this;
	}

	public Edge setDestination(String dstlbl)
	throws IOException,
	FieldNumberOutOfBoundException
	{
		setStrFld(FLD_DST_LABEL, dstlbl);
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
		System.out.println( getLabel() + " " + getSource()+ " "  + getDestination()+ " "  + getWeight() );
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