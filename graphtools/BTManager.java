package graphtools;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import btree.*;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;
import edgeheap.*;
import global.*;
import heap.*;
import nodeheap.*;
import zindex.DescriptorKey;
import zindex.ZEncoder;
import zindex.ZFileRangeScan;
import zindex.ZFileScan;
import zindex.ZTreeFile;

public class BTManager {

	private BTreeFile nodelabelbtree=null;
	private ZTreeFile nodeDescriptorTree=null;
	private BTreeFile edgelabelbtree=null;
	private BTreeFile edgeweightbtree=null;
	private BTreeFile edgelabelbtree_s=null;
	private BTreeFile edgelabelbtree_d=null;
	private BTreeFile edgeidbtree=null;

	public static String nodelabelbtree_filename = "NodeLabelTree";
	public static String nodeDescriptorTree_filename = "NodeDescriptorTree";
	public static String edgelabelbtree_filename = "EdgeLabelTree";
	public static String edgeweightbtree_filename = "EdgeWeightTree";
	public static String edgelabelbtree_s_filename = "EdgeLabelTree_Source";
	public static String edgelabelbtree_d_filename = "EdgeLabelTree_Destination";
	public static String edgeidbtree_filename = "EdgeIdTree";
	

	public BTManager(){

	}

	public void openAllFile(){
		try {
			nodelabelbtree = new BTreeFile("NodeLabelTree", AttrType.attrString, 20, 1);
			try {
				nodeDescriptorTree = new ZTreeFile("NodeDescriptorTree");
			} catch (PinPageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			edgelabelbtree = new BTreeFile("EdgeLabelTree", AttrType.attrString, 20, 1);
			edgeweightbtree = new BTreeFile("EdgeWeightTree", AttrType.attrInteger, 4, 1);
			edgelabelbtree_s = new BTreeFile("EdgeLabelTree_Source", AttrType.attrString, 20, 1);
			edgelabelbtree_d = new BTreeFile("EdgeLabelTree_Destination", AttrType.attrString, 20, 1);
			edgeidbtree = new BTreeFile("EdgeIdTree", AttrType.attrInteger, 4, 1);

		} catch (GetFileEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConstructPageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AddFileEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openIndexFile(String filename){
		try{
			if(filename ==nodelabelbtree_filename){
				nodelabelbtree = new BTreeFile("NodeLabelTree", AttrType.attrString, 20, 1);
			}else if(filename ==nodeDescriptorTree_filename){
				nodeDescriptorTree = new ZTreeFile("NodeDescriptorTree");
			}else if(filename ==edgelabelbtree_filename ){
				edgelabelbtree = new BTreeFile("EdgeLabelTree", AttrType.attrString, 20, 1);
			}else if(filename ==edgeweightbtree_filename ){
				edgeweightbtree = new BTreeFile("EdgeWeightTree", AttrType.attrInteger, 4, 1);
			}else if(filename ==edgelabelbtree_s_filename ){
				edgelabelbtree_s = new BTreeFile("EdgeLabelTree_Source", AttrType.attrString, 20, 1);
			}else if(filename ==edgelabelbtree_d_filename ){
				edgelabelbtree_d = new BTreeFile("EdgeLabelTree_Destination", AttrType.attrString, 20, 1);
			}else if(filename ==edgeidbtree_filename ){
				edgeidbtree = new BTreeFile("EdgeIdTree", AttrType.attrInteger, 4, 1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void closeAllFile(){
		try {
			BTreeFile[] bts = { nodelabelbtree, nodeDescriptorTree, edgelabelbtree, edgeweightbtree, edgelabelbtree_s,
					edgelabelbtree_d, edgeidbtree };
			for (int i = 0; i < bts.length; i++) {
				if (bts[i] != null)
					bts[i].close();
			}

		} catch (PageUnpinnedException | InvalidFrameNumberException | HashEntryNotFoundException
				| ReplacerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BTreeFile getNodelabelbtree() {
		return nodelabelbtree;
	}

	public void setNodelabelbtree(BTreeFile nodelabelbtree) {
		this.nodelabelbtree = nodelabelbtree;
	}

	public ZTreeFile getNodeDescriptorTree() {
		return nodeDescriptorTree;
	}

	public void setNodeDescriptorTree(ZTreeFile nodeDescriptorTree) {
		this.nodeDescriptorTree = nodeDescriptorTree;
	}

	public BTreeFile getEdgelabelbtree() {
		return edgelabelbtree;
	}

	public void setEdgelabelbtree(BTreeFile edgelabelbtree) {
		this.edgelabelbtree = edgelabelbtree;
	}

	public BTreeFile getEdgeweightbtree() {
		return edgeweightbtree;
	}

	public void setEdgeweightbtree(BTreeFile edgeweightbtree) {
		this.edgeweightbtree = edgeweightbtree;
	}

	public BTreeFile getEdgelabelbtree_s() {
		return edgelabelbtree_s;
	}

	public void setEdgelabelbtree_s(BTreeFile edgelabelbtree_s) {
		this.edgelabelbtree_s = edgelabelbtree_s;
	}

	public BTreeFile getEdgelabelbtree_d() {
		return edgelabelbtree_d;
	}

	public void setEdgelabelbtree_d(BTreeFile edgelabelbtree_d) {
		this.edgelabelbtree_d = edgelabelbtree_d;
	}

	public BTreeFile getEdgeidbtree() {
		return edgeidbtree;
	}

	public void setEdgeidbtree(BTreeFile edgeidbtree) {
		this.edgeidbtree = edgeidbtree;
	}

	public void insertNodetoNLBT(HFManager hfm, String fileName) throws Exception {

		System.out.println("start inserting nodes to B-Tree");
		RID rid = new RID();
		Node node = new Node();
		KeyClass key;
		hfm.initScanNode();

		while ((node = hfm.scanNextNode()) != null) {
			rid = hfm.getCurRID();
			key = new StringKey(node.getLabel());
			BTFileScan s = nodelabelbtree.new_scan(key, key);
			if (s.get_next() != null)
				continue;
			s.DestroyBTreeFileScan();
			nodelabelbtree.insert(key, rid);
		}
		hfm.closeScan();
	}

	public void insertEdgeBtress(HFManager hfm) throws Exception {
		System.out.println("Building edge trees");
		RID rid = new RID();
		Edge edge = new Edge();
		KeyClass key;
		hfm.initScanEdge();
		while ((edge = hfm.scanNextEdge()) != null) {
			rid = hfm.getCurRID();
			key = new IntegerKey(edge.getID());
			BTFileScan s = edgeidbtree.new_scan(key, key);
			if (s.get_next() != null)
				continue;
			s.DestroyBTreeFileScan();
			edgeidbtree.insert(key, rid);
			key = new StringKey(edge.getLabel());
			edgelabelbtree.insert(key, rid);
			key = new StringKey(edge.getSource());
			edgelabelbtree_s.insert(key, rid);
			key = new StringKey(edge.getDestination());
			edgelabelbtree_d.insert(key, rid);
			key = new IntegerKey(edge.getWeight());
			edgeweightbtree.insert(key, rid);
		}
		hfm.closeScan();
	}

	
	public void insertNodetoZT(HFManager hfm, String filename) throws Exception {
		System.out.println("start inserting nodes to Z-Tree");

			RID rid = new RID();
			Node node = new Node();
			KeyClass key;
			hfm.initScanNode();

			while ((node = hfm.scanNextNode()) != null) {
				rid = hfm.getCurRID();
				key = new StringKey(ZEncoder.encode(node.getDesc()));
				BTFileScan s = nodeDescriptorTree.new_scan(key, key);
				if (s.get_next() != null)
					continue;
				nodeDescriptorTree.insert(key, rid);
				s.DestroyBTreeFileScan();
			}
			hfm.closeScan();
	}

	public RID getRIDFromLabel_Node(String label) throws ScanIteratorException, IteratorException,
			ConstructPageException, PinPageException, UnpinPageException, IOException {
		KeyClass key = new StringKey(label);
		try {
			BTFileScan s = this.getNodelabelbtree().new_scan(key, key);
			RID rid = ((LeafData) s.get_next().data).getData();
			try {
				s.DestroyBTreeFileScan();
			} catch (InvalidFrameNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReplacerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PageUnpinnedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HashEntryNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return rid;
		} catch (KeyNotMatchException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}

	}

	public RID getRIDFromLabel_Edge(String label) throws ScanIteratorException, KeyNotMatchException, IteratorException,
			ConstructPageException, PinPageException, UnpinPageException, IOException {
		KeyClass key = new StringKey(label);
		try {
			BTFileScan s = this.getEdgelabelbtree().new_scan(key, key);
			RID rid = ((LeafData) s.get_next().data).getData();
			try {
				s.DestroyBTreeFileScan();
			} catch (InvalidFrameNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReplacerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PageUnpinnedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HashEntryNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return rid;
		} catch (KeyNotMatchException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}

	}

	public RID getRIDFromLabel_EdgeS(String label) throws ScanIteratorException, KeyNotMatchException,
			IteratorException, ConstructPageException, PinPageException, UnpinPageException, IOException {
		KeyClass key = new StringKey(label);
		try {
			BTFileScan s = this.getEdgelabelbtree_s().new_scan(key, key);
			RID rid = ((LeafData) s.get_next().data).getData();
			try {
				s.DestroyBTreeFileScan();
			} catch (InvalidFrameNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReplacerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PageUnpinnedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HashEntryNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return rid;
		} catch (KeyNotMatchException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}

	}

	public RID getRIDFromLabel_EdgeD(String label) throws ScanIteratorException, KeyNotMatchException,
			IteratorException, ConstructPageException, PinPageException, UnpinPageException, IOException {
		KeyClass key = new StringKey(label);
		try {
			BTFileScan s = this.getEdgelabelbtree_d().new_scan(key, key);
			RID rid = ((LeafData) s.get_next().data).getData();
			try {
				s.DestroyBTreeFileScan();
			} catch (InvalidFrameNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReplacerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PageUnpinnedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HashEntryNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return rid;
		} catch (KeyNotMatchException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}

	}

	public boolean deletenode(KeyClass key, RID rid) throws Exception {
		nodelabelbtree.Delete(key, rid);
		return true;
	}

	public boolean deleteedge(KeyClass key, RID rid) throws Exception {
		edgelabelbtree.Delete(key, rid);
		return true;
	}

	public boolean deleteedge_d(KeyClass key, RID rid) throws Exception {
		edgelabelbtree_d.Delete(key, rid);
		return true;
	}

	public boolean deleteedge_s(KeyClass key, RID rid) throws Exception {
		edgelabelbtree_s.Delete(key, rid);
		return true;
	}

	public boolean deleteedgeweight(KeyClass key, RID rid) throws Exception {
		edgeweightbtree.Delete(key, rid);
		return true;
	}

	public void NodeQuery1(GraphDBManager db) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException,
			HFDiskMgrException, HFBufMgrException, Exception {
		BTFileScan s = nodelabelbtree.new_scan(null, null);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Node n = new Node(db.hfmgr.getNodefile().getRecord(((LeafData) itr.data).getData()));
			n.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}

	public void NodeQuery4(GraphDBManager db, String label)
			throws FieldNumberOutOfBoundException, InvalidTypeException, InvalidTupleSizeException,
			InvalidSlotNumberException, HFException, HFDiskMgrException, HFBufMgrException, Exception {
		KeyClass k = new StringKey(label);
		BTFileScan s = db.btmgr.nodelabelbtree.new_scan(k, k);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Node n = new Node(db.hfmgr.getNodefile().getRecord(((LeafData) itr.data).getData()));
			n.print();

			BTFileScan d = db.btmgr.edgelabelbtree_d.new_scan(k, k);
			KeyDataEntry itrd = d.get_next();
			while (itrd != null) {
				Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itrd.data).getData()));
				e.print();
				itrd = d.get_next();
			}
			d.DestroyBTreeFileScan();
			BTFileScan ss = db.btmgr.edgelabelbtree_s.new_scan(k, k);
			KeyDataEntry itrs = ss.get_next();
			while (itrs != null) {
				Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itrs.data).getData()));
				e.print();
				itrs = ss.get_next();
			}
			ss.DestroyBTreeFileScan();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}

	public void EdgeQuery1(GraphDBManager db) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException,
			HFDiskMgrException, HFBufMgrException, Exception {
		BTFileScan s = db.btmgr.edgelabelbtree_s.new_scan(null, null);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itr.data).getData()));
			e.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}

	public void EdgeQuery2(GraphDBManager db) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException,
			HFDiskMgrException, HFBufMgrException, Exception {
		BTFileScan s = db.btmgr.edgelabelbtree_d.new_scan(null, null);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itr.data).getData()));
			e.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}

	public void EdgeQuery3(GraphDBManager db) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException,
			HFDiskMgrException, HFBufMgrException, Exception {
		BTFileScan s = db.btmgr.edgelabelbtree.new_scan(null, null);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itr.data).getData()));
			e.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}

	public void EdgeQuery4(GraphDBManager db) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException,
			HFDiskMgrException, HFBufMgrException, Exception {
		BTFileScan s = db.btmgr.edgeweightbtree.new_scan(null, null);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itr.data).getData()));
			e.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}

	public void EdgeQuery5(GraphDBManager db, int lowerb, int upperb) throws InvalidSlotNumberException,
			InvalidTupleSizeException, HFException, HFDiskMgrException, HFBufMgrException, Exception {
		BTFileScan s = db.btmgr.edgeweightbtree.new_scan(new IntegerKey(lowerb), new IntegerKey(upperb));
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itr.data).getData()));
			e.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}

	static BigInteger targetInt;

	public void NodeQueryIndex2(GraphDBManager db, Descriptor target) {
		String targetCode = ZEncoder.encode(target);
		List<String> zlist = new LinkedList<>();
		ZFileScan scan = new ZFileScan(this.nodeDescriptorTree);
		KeyDataEntry itr = scan.getNext();
		while (itr != null) {
			zlist.add(((StringKey) itr.key).toString());
			itr = scan.getNext();
		}
		// Sort the z-values
		targetInt = new BigInteger(targetCode);
		PriorityQueue<String> queue = new PriorityQueue<>(10, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				BigInteger int1 = new BigInteger(o1);
				BigInteger int2 = new BigInteger(o2);

				return Math.abs(int1.subtract(targetInt).intValue()) - Math.abs(int2.subtract(targetInt).intValue());
			}

		});
		for (String zval : zlist) {
			queue.offer(zval);
		}

		List<Node> nodes = new LinkedList<>();
		for (String k : queue) {
			BTFileScan zscan = this.nodeDescriptorTree.new_scan(new DescriptorKey(k), new DescriptorKey(k));
			try {
				KeyDataEntry data = zscan.get_next();
				RID nid = ((LeafData) data.data).getData();
				Tuple n = db.hfmgr.getNodefile().getRecord(nid);
				Node node = new Node(n);
				node.print();
			} catch (ScanIteratorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidSlotNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidTupleSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HFDiskMgrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HFBufMgrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void NodeQueryIndex3(GraphDBManager db, Descriptor target, int distance) throws Exception {
		ZFileRangeScan rangeScan = new ZFileRangeScan(this.nodeDescriptorTree, target, distance);
		KeyDataEntry itr = rangeScan.getNext();
		int nodeCnt = 0;
		while (itr != null) {
			Node node = new Node(db.hfmgr.getNodefile().getRecord(((LeafData) itr.data).getData()));
			System.out.println(node.getLabel());
			nodeCnt++;
			itr = rangeScan.getNext();
		}
		System.out.println("Result set count: " + nodeCnt);
		rangeScan.endScan();
	}

	public void NodeQueryIndex5(GraphDBManager db, Descriptor target, int distance) throws Exception {
		ZFileRangeScan rangeScan = new ZFileRangeScan(this.nodeDescriptorTree, target, distance);
		KeyDataEntry itr = rangeScan.getNext();
		int nodeCnt = 0;
		while (itr != null) {
			Node node = new Node(db.hfmgr.getNodefile().getRecord(((LeafData) itr.data).getData()));
			node.print();

			StringKey k = new StringKey(node.getLabel());
			BTFileScan d = db.btmgr.edgelabelbtree_d.new_scan(k, k);
			KeyDataEntry itrd = d.get_next();
			while (itrd != null) {
				Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itrd.data).getData()));
				e.print();
				itrd = d.get_next();
			}
			d.DestroyBTreeFileScan();
			BTFileScan ss = db.btmgr.edgelabelbtree_s.new_scan(k, k);
			KeyDataEntry itrs = ss.get_next();
			while (itrs != null) {
				Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itrs.data).getData()));
				e.print();
				itrs = ss.get_next();
			}
			ss.DestroyBTreeFileScan();
			System.out.println();
			nodeCnt++;
			itr = rangeScan.getNext();
		}
		System.out.println("Result set count: " + nodeCnt);
		rangeScan.endScan();
	}
}
