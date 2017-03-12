package graphtools;

import java.io.*;
import java.util.*;
import java.lang.*;
import heap.*;
import nodeheap.*;
import edgeheap.*;
import bufmgr.*;
import diskmgr.*;
import global.*;
import chainexception.*;
import java.util.Scanner;


class HFManager implements  GlobalConst{
	public static String nodefilename = "nodefile";
	public static String edgefilename = "edgefile";
	private Heapfile nodefile;
	private Heapfile edgefile;
	private Scan nscan;
	private RID rid;
	Tuple tuple = null;

	public HFManager(){
		init();
	}

	public void init(){
		System.out.println ("  - Create a node heap file\n");
	    try {
	      nodefile = new Heapfile(nodefilename);
	      edgefile = new Heapfile(edgefilename);
	    }
	    catch (Exception e) {
	      System.err.println ("*** Could not create node heap file\n");
	      e.printStackTrace();
	    }
	}

	public boolean insertNodesFromFile(String fileName)
	throws FileNotFoundException,
	InvalidTypeException,
	IOException,
	InvalidTupleSizeException,
	FieldNumberOutOfBoundException,
	InvalidSlotNumberException,
	SpaceNotAvailableException,
	HFException,
	HFBufMgrException,
	HFDiskMgrException
	{
		System.out.println("start inserting nodes");
		File file = new File(fileName);
		Scanner scan = new Scanner(file);
		int cnt = 0;
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			String[] data = line.split(" ");
			Node anode = new Node();
			Descriptor adesc = new Descriptor();
			String label = data[0];
			adesc.set(Integer.parseInt(data[1]), 
				Integer.parseInt(data[2]), 
				Integer.parseInt(data[3]), 
				Integer.parseInt(data[4]),
				Integer.parseInt(data[5]));
			short[] labelsize = {(short)(label.length()+1)};
			AttrType[] types = {new AttrType(AttrType.attrInteger), new AttrType(AttrType.attrDesc), new AttrType(AttrType.attrString)};
			anode.setHdr(Node.Fld_CNT, types ,labelsize);
			anode.setLabel(label);
			anode.setDesc(adesc);
			nodefile.insertRecord(anode.getNodeByteArray());
			cnt++;
		}
		System.out.println(cnt + " nodes inserted");

		
		//test scan
		initScanNode();
		Node node = scanNextNode();
		while(node != null){
			node.print();
			node = scanNextNode();
		}
		return true;
	}

	public boolean insertEdgesFromFile(String fileName)
	throws FileNotFoundException,
	InvalidTypeException,
	IOException,
	InvalidTupleSizeException,
	FieldNumberOutOfBoundException,
	InvalidSlotNumberException,
	SpaceNotAvailableException,
	HFException,
	HFBufMgrException,
	HFDiskMgrException
	{
		System.out.println("start inserting edges");
		File file = new File(fileName);
		Scanner scan = new Scanner(file);
		int cnt = 0;
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			String[] data = line.split(" ");
			Edge aedge = new Edge();
			int weight = Integer.valueOf(data[3]);
			short[] labelsize = {(short)data[0].length(), (short)data[1].length(), (short)data[2].length()};
			AttrType[] types = {
		      new AttrType(AttrType.attrInteger), 
		      new AttrType(AttrType.attrInteger), 
		      new AttrType(AttrType.attrInteger), 
		      new AttrType(AttrType.attrInteger), 
		      new AttrType(AttrType.attrString),
		      new AttrType(AttrType.attrString),
		      new AttrType(AttrType.attrString)
		    };
			aedge.setHdr(Edge.Fld_CNT, types ,labelsize);

			aedge.setWeight(weight);
			aedge.setSource(data[0]);
			aedge.setDestination(data[1]);
			aedge.setLabel(data[2]);

			System.out.println(aedge.getLength());
			edgefile.insertRecord(aedge.getEdgeByteArray());
			cnt++;
		}
		System.out.println(cnt + " edges inserted");

		
		//test scan
		initScanEdge();
		Edge edge = scanNextEdge();
		while(edge != null){
			edge.print();
			edge = scanNextEdge();
		}
		return true;
		
	}

	public void initScanNode(){
		try{
			rid = new RID();
			nscan = nodefile.openScan();
		}catch(Exception e){
			System.err.println ("*** Error opening scan\n");
			e.printStackTrace();
		}

	}

	public Node scanNextNode(){
		try{
			tuple = nscan.getNext(rid);
			Node node = null;
			if(tuple != null) node = new Node(tuple);
			return node;
		}catch(Exception e){
			System.err.println ("*** Error Scan next node");
			e.printStackTrace();
		}
		return null;
	}

	public void initScanEdge(){
		try{
			rid = new RID();
			nscan = edgefile.openScan();
		}catch(Exception e){
			System.err.println ("*** Error opening scan\n");
			e.printStackTrace();
		}

	}

	public Edge scanNextEdge(){
		try{
			tuple = nscan.getNext(rid);
			Edge edge = null;
			if(tuple != null) edge = new Edge(tuple);
			return edge;
		}catch(Exception e){
			System.err.println ("*** Error Scan next node");
			e.printStackTrace();
		}
		return null;
	}

}