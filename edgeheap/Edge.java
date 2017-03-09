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

	public Edge(){
		super();
	}

	public Edge(byte[] aedge, int offset){
		super(aedge, offset,max_size);
	}

	public Edge(Edge fromEdge){
		super(fromEdge);
		source = fromEdge.getSource();
		destination = fromEdge.getDestination();
		label = fromEdge.getLabel();
		weight = fromEdge.getWeight();
	}

	public NID getSource(){
		return source;
	}

	public NID getDestination(){
		return destination;
	}

	public String getLabel(){
		return label;
	}

	public int getWeight(){
		return weight;
	}

	public Edge setLabel(String alabel){
		label = alabel;
		return this;
	}

	public Edge setWeight(int aweight){
		weight = aweight;
		return this;
	}

	public Edge setSource(NID sourceID){
		source = sourceID;
		return this;
	}

	public Edge setDestination(NID destID){
		destination = destID;
		return this;
	}

	public byte [] getEdgeByteArray(){
		return getTupleByteArray(); 
	}

	public void print()
	      throws IOException
	{
		System.out.println("{" +  "}");
	}

	public double getSize(){
		return 10;
	}

	public void edgeCopy(Edge fromEdge){
		super.tupleCopy(fromEdge);
		source = fromEdge.getSource();
		destination = fromEdge.getDestination();
		label = fromEdge.getLabel();
		weight = fromEdge.getWeight();
	}

	public void edgeInit(byte[] aedge, int offset){
		super.tupleInit(aedge, offset, max_size);
	}

	public void edgeSet(byte[] fromedge, int offset){
		super.tupleSet(fromedge, offset, max_size);
	}
}