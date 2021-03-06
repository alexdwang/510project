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
import iterator.*;
import chainexception.*;
import java.util.Scanner;
import btree.*;


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

	public Heapfile getNodefile() {
		return nodefile;
	}

	public void setNodefile(Heapfile nodefile) {
		this.nodefile = nodefile;
	}

	public Heapfile getEdgefile() {
		return edgefile;
	}

	public void setEdgefile(Heapfile edgefile) {
		this.edgefile = edgefile;
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
			short[] labelsize = {Node.LABEL_MAX_LENGTH};
			AttrType[] types = {new AttrType(AttrType.attrDesc), new AttrType(AttrType.attrString)};
			anode.setHdr(Node.FLD_CNT, types ,labelsize);
			anode.setLabel(label);
			anode.setDesc(adesc);
			nodefile.insertRecord(anode.getNodeByteArray());
			cnt++;
		}
		System.out.println(cnt + " nodes inserted");
		/*
		//test scan
		initScanNode();
		Node node = scanNextNode();
		while(node != null){
			node.print();
			node = scanNextNode();
		}
		*/
		return true;
	}

	public Sort SortNodes(int fld_no, short SORTPGNUM){
		FileScan node_scanner;
		Sort sort;
		AttrType[] attrType = {new AttrType(AttrType.attrDesc), new AttrType(AttrType.attrString)};
		short[] attrSize = {Node.LABEL_MAX_LENGTH};
		TupleOrder[] order = { new TupleOrder(TupleOrder.Ascending), new TupleOrder(TupleOrder.Descending) };
    	RelSpec rel = new RelSpec(RelSpec.outer); 
    	FldSpec[] projlist = { new FldSpec(rel, 1), new FldSpec(rel, 2)};
		try {
			node_scanner = new FileScan(nodefilename, attrType, attrSize, (short) 2, 2, projlist, null);
			sort = new Sort(attrType, (short) 2, attrSize, node_scanner, fld_no, order[0], Node.LABEL_MAX_LENGTH, 10);
			return sort;
		}catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public Sort SortEdges(int fld_no, short SORTPGNUM){
		FileScan edge_scanner;
		Sort sort;
		AttrType[] attrType = {
		 new AttrType(AttrType.attrInteger),
		 new AttrType(AttrType.attrInteger),
		 new AttrType(AttrType.attrString),
		 new AttrType(AttrType.attrString),
		 new AttrType(AttrType.attrString) };
		short[] attrSize = {Edge.LABEL_MAX_LENGTH,Edge.LABEL_MAX_LENGTH,Edge.LABEL_MAX_LENGTH};
		TupleOrder[] order = { new TupleOrder(TupleOrder.Ascending), new TupleOrder(TupleOrder.Descending) };
    	RelSpec rel = new RelSpec(RelSpec.outer); 
    	FldSpec[] projlist = { 
    		new FldSpec(rel, 1),
    		new FldSpec(rel, 2),
    		new FldSpec(rel, 3),
    		new FldSpec(rel, 4),
    		new FldSpec(rel, 5)
    	};
		try {
			edge_scanner = new FileScan(edgefilename, attrType, attrSize, (short) 5, 5, projlist, null);
			sort = new Sort(attrType, (short) 5, attrSize, edge_scanner, fld_no, order[0], Node.LABEL_MAX_LENGTH, SORTPGNUM);
			return sort;
		}catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
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
			short[] labelsize = {Edge.LABEL_MAX_LENGTH, Edge.LABEL_MAX_LENGTH, Edge.LABEL_MAX_LENGTH};
			AttrType[] types = {
			  new AttrType(AttrType.attrInteger),	
		      new AttrType(AttrType.attrInteger), 
		      new AttrType(AttrType.attrString),
		      new AttrType(AttrType.attrString),
		      new AttrType(AttrType.attrString)
		    };
			aedge.setHdr(Edge.FLD_CNT, types ,labelsize);

			aedge.setID(cnt);
			aedge.setWeight(weight);
			aedge.setSource(data[0]);
			aedge.setDestination(data[1]);
			aedge.setLabel(data[2]);
			edgefile.insertRecord(aedge.getEdgeByteArray());
			cnt++;
		}
		System.out.println(cnt + " edges inserted");
		//System.out.println(getLabelCnt());
		/*
		//test scan
		initScanEdge();
		Edge edge = scanNextEdge();
		while(edge != null){
			edge.print();
			edge = scanNextEdge();
		}
		*/
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

	public void closeScan(){
		if(nscan != null) nscan.closescan();
	}

	public Node scanNextNode(){
		try{
			tuple = nscan.getNext(rid);
			Node node = null;
			if(tuple != null) node = new Node(tuple);
			else{
				nscan.closescan();
			}
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
			else{
				nscan.closescan();
			}
			return edge;
		}catch(Exception e){
			System.err.println ("*** Error Scan next node");
			e.printStackTrace();
		}
		return null;
	}

	public RID getCurRID(){
		RID newrid = new RID(rid.pageNo, rid.slotNo);
		return newrid;
	}

	public int getNodeCnt(){
		initScanNode();
		int cnt = 0;
		Node node = scanNextNode();
		while(node != null){
			cnt++;
			node = scanNextNode();
		}
		return cnt;
	}

	public int getEdgeCnt(){
		initScanEdge();
		int cnt = 0;
		Edge edge = scanNextEdge();
		while(edge != null){
			cnt++;
			edge = scanNextEdge();
		}
		return cnt;
	}

	public int getSourceCnt()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		initScanEdge();
		Set<String> srcSet = new HashSet<String>();
		Edge edge = scanNextEdge();
		while(edge != null){
			String src = edge.getSource();
			if(!srcSet.contains(src)) srcSet.add(src); 
			edge = scanNextEdge();
		}
		return srcSet.size();
	}

	public int getDestinationCnt()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		initScanEdge();
		Set<String> dstSet = new HashSet<String>();
		Edge edge = scanNextEdge();
		while(edge != null){
			String src = edge.getDestination();
			if(!dstSet.contains(src)) dstSet.add(src); 
			edge = scanNextEdge();
		}
		return dstSet.size();
	}

	public void NodeQuery(int qtype, short bufnum){
		switch(qtype){
		case 0:
		{
			initScanNode();
			Node node = scanNextNode();
			try{
				while(node != null){
					node.print();
					node = scanNextNode();
				}
			}catch(Exception e){
				System.err.println ("failed when query by label\n");
				e.printStackTrace();
			}
			break;
		}
		case 1:
		{
			
			Sort sort = SortNodes(2,bufnum);
			if(sort == null){
				System.err.println ("failed when sort\n");
			}else{
				try {
					Tuple t = sort.get_next();
					Node n;
					while(t != null){
						n = new Node(t);
						n.print();
						t = sort.get_next();
					}
					sort.close();
			    }
			    catch (Exception e) {
			    	e.printStackTrace(); 
			    }
			}
			
			break;
		}

		}

	}

	public void EdgeQuery01234(int qtype, short bufnum){
		switch(qtype){
		case 0:{// sort by label
			try{
				Sort sort = SortEdges(5, bufnum);
				Tuple t = sort.get_next();
				Edge e;
				while(t != null){
					e = new Edge(t);
					e.print();
					t = sort.get_next();
				}
				sort.close();
			}catch(Exception e){
				System.err.println ("failed when query by label\n");
				e.printStackTrace();
			}
			break;
		}
		case 1:{
			try{
				Sort sort = SortEdges(3, bufnum);
				Tuple t = sort.get_next();
				Edge e;
				while(t != null){
					e = new Edge(t);
					e.print();
					t = sort.get_next();
				}
				sort.close();
			}catch(Exception e){
				System.err.println ("failed when query by label\n");
				e.printStackTrace();
			}
			break;
		}
		case 2:{
			break;
		}
		case 3:{
			break;
		}
		case 4:{
			break;
		}

		}
	}

	public void EdgeQuery5(int lb, int ub){
		List<Edge> edges = new ArrayList<Edge>();
		initScanEdge();
		Edge edge = scanNextEdge();
		try{
			while(edge != null){
				int wgt = edge.getWeight();
				if(wgt <= ub && wgt >= lb) edges.add(edge);
				edge = scanNextEdge();
			}
			for(Edge e : edges){
				e.print();
			}
		}catch(Exception e){
			System.err.println ("failed when query by label\n");
			e.printStackTrace();
		}
	}

	public static Comparator<Edge> SourceLabelComparator
                          = new Comparator<Edge>() {

	    public int compare(Edge n1, Edge n2) {
	    	String l1 = "", l2 = "";
	    	try{
	    		l1 = n1.getSource();
	    		l2 = n2.getSource();
	    	}catch(Exception e){
	    		System.err.println ("failed when query by label\n");
				e.printStackTrace();
	    	}
	      //ascending order
	      return l1.compareTo(l2);

	      //descending order
	      //return fruitName2.compareTo(fruitName1);
	    }

	};

	public static Comparator<Edge> DstLabelComparator
                          = new Comparator<Edge>() {

	    public int compare(Edge n1, Edge n2) {
	    	String l1 = "", l2 = "";
	    	try{
	    		l1 = n1.getDestination();
	    		l2 = n2.getDestination();
	    	}catch(Exception e){
	    		System.err.println ("failed when query by label\n");
				e.printStackTrace();
	    	}
	      //ascending order
	      return l1.compareTo(l2);

	      //descending order
	      //return fruitName2.compareTo(fruitName1);
	    }

	};

	public static Comparator<Edge> EdgeLabelComparator
                          = new Comparator<Edge>() {

	    public int compare(Edge n1, Edge n2) {
	    	String l1 = "", l2 = "";
	    	try{
	    		l1 = n1.getLabel();
	    		l2 = n2.getLabel();
	    	}catch(Exception e){
	    		System.err.println ("failed when query by label\n");
				e.printStackTrace();
	    	}
	      //ascending order
	      return l1.compareTo(l2);

	      //descending order
	      //return fruitName2.compareTo(fruitName1);
	    }

	};

	public static Comparator<Edge> WeightComparator
                          = new Comparator<Edge>() {

	    public int compare(Edge e1, Edge e2) {
	    	int l1 = 0, l2 = 0;
	    	try{
	    		l1 = e1.getWeight();
	    		l2 = e2.getWeight();
	    	}catch(Exception e){
	    		System.err.println ("failed when query by label\n");
				e.printStackTrace();
	    	}
	      //ascending order
	      return l1 - l2;

	      //descending order
	      //return fruitName2.compareTo(fruitName1);
	    }

	};

	public void NodeQuery2(Descriptor desc){
		List<Node> nodes = new ArrayList<Node>();
		initScanNode();
		opt_desc.copyDesc(desc);
		Node node = scanNextNode();
		while(node != null){
			nodes.add(node);
			node = scanNextNode();
		}
		//sort
		try{
			Collections.sort(nodes, DescriptorComparator);
			for(Node n : nodes){
				n.print();
			}
		}catch(Exception e){
			System.err.println ("failed when query by label\n");
			e.printStackTrace();
		}
	}

	public void NodeQuery3(Descriptor desc, double distance){
		List<String> labels = new ArrayList<String>();
		initScanNode();
		opt_desc.copyDesc(desc);
		Node node = scanNextNode();
		while(node != null){
			try{
				Descriptor adesc = node.getDesc();
				if(adesc.distance(desc) <= distance){
					String str = node.getLabel();
					labels.add(str);
				}
			}catch(Exception e){
				System.err.println ("failed when qtype = 3\n");
				e.printStackTrace();
			}
			node = scanNextNode();
		}
		//sort
		for(String s : labels){
			System.out.println(s);
		}
		System.out.println(labels.size() + " results");
	}

	public void NodeQuery4(String label){
		List<Edge> edges = new ArrayList<Edge>();
		initScanNode();
		Node node = scanNextNode();
		while(node != null){
			try{
				String lbl = node.getLabel();
				if(label.compareTo(lbl) == 0){
					node.print();
					closeScan();
					break;
				}
			}catch(Exception e){
				System.err.println ("failed when qtype = 4\n");
				e.printStackTrace();
			}
			node = scanNextNode();
		}

		initScanEdge();
		Edge edge = scanNextEdge();
		//find edges
		try{
			while(edge != null){
				String src = edge.getSource();
				String dst = edge.getDestination();
				if(src.compareTo(label) == 0 || dst.compareTo(label) == 0){
					edge.print();
				}
				edge = scanNextEdge();
			}
		}catch(Exception e){
				System.err.println ("failed when qtype = 3\n");
				e.printStackTrace();
		}
	}

	public void NodeQuery5(Descriptor desc, double distance){
		List<Node> labels = new ArrayList<Node>();
		initScanNode();
		opt_desc.copyDesc(desc);
		Node node = scanNextNode();
		while(node != null){
			try{
				Descriptor adesc = node.getDesc();
				if(adesc.distance(desc) == distance){
					labels.add(node);
				}
			}catch(Exception e){
				System.err.println ("failed when qtype = 5\n");
				e.printStackTrace();
			}
			node = scanNextNode();
		}

		initScanEdge();
		Edge edge = scanNextEdge();
		//find edges
		try{
			for(Node n : labels){
				n.print();
				String label = n.getLabel();
				while(edge != null){
					String src = edge.getSource();
					String dst = edge.getDestination();
					if(src.compareTo(label) == 0 || dst.compareTo(label) == 0){
						edge.print();
					}
					edge = scanNextEdge();
				}
			}
		}catch(Exception e){
				System.err.println ("failed when qtype = 5\n");
				e.printStackTrace();
		}
	}

	public static Comparator<Node> LabelComparator
                          = new Comparator<Node>() {

	    public int compare(Node n1, Node n2) {
	    	String l1 = "", l2 = "";
	    	try{
	    		l1 = n1.getLabel();
	    		l2 = n2.getLabel();
	    	}catch(Exception e){
	    		System.err.println ("failed when query by label\n");
				e.printStackTrace();
	    	}
	      //ascending order
	      return l1.compareTo(l2);

	      //descending order
	      //return fruitName2.compareTo(fruitName1);
	    }

	};

	public static Descriptor opt_desc = new Descriptor();
	public static Comparator<Node> DescriptorComparator
                          = new Comparator<Node>() {

	    public int compare(Node n1, Node n2) {
	    	Descriptor l1 = new Descriptor();
	    	Descriptor l2 = new Descriptor();
	    	try{
	    		l1.copyDesc(n1.getDesc());
	    		l2.copyDesc(n2.getDesc());
	    	}catch(Exception e){
	    		System.err.println ("failed when query by label\n");
				e.printStackTrace();
	    	}
	      //ascending order
	      return (int)Math.round(l1.distance(opt_desc) - l2.distance(opt_desc));

	      //descending order
	      //return fruitName2.compareTo(fruitName1);
	    }

	};

	public int getLabelCnt()
	throws IOException,
	FieldNumberOutOfBoundException
	{
		Set<String> lblSet = new HashSet<String>();
		initScanNode();
		Node node = scanNextNode();
		while(node != null){
			String src = node.getLabel();
			if(!lblSet.contains(src)) lblSet.add(src); 
			node = scanNextNode();
		}
		initScanEdge();
		Edge edge = scanNextEdge();
		while(edge != null){
			String src = edge.getLabel();
			if(!lblSet.contains(src)) lblSet.add(src); 
			edge = scanNextEdge();
		}
		return lblSet.size();
	}



	
	public boolean deletenode(RID rid)
			throws Exception
	{
		nodefile.deleteRecord(rid);
		
		return true;
		
	}

	public boolean deleteedge(RID rid)
	{
		try{
			edgefile.deleteRecord(rid);
		}catch(Exception e){

		}
		
		return true;
		
	}

}