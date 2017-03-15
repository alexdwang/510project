package graphtools;

import java.io.File;
import java.io.IOException;
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
import zindex.ZTreeFile;

public class BTManager {
	private BTreeFile nodelabelbtree;
	private ZTreeFile nodeDescriptorTree;
	private BTreeFile edgelabelbtree;
	private BTreeFile edgeweightbtree;
	private BTreeFile edgelabelbtree_s;
	private BTreeFile edgelabelbtree_d;
	
	public BTManager(){
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

	public void insertNodetoNLBT(HFManager hfm, String fileName) throws Exception {

		System.out.println("start inserting nodes");
		File file = new File(fileName);
		Scanner scan = new Scanner(file);
		int cnt = 0;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] data = line.split(" ");
			String label = data[0];

			RID rid = new RID();
			Node node = new Node();
			KeyClass key;
			hfm.initScanNode();

			while ((node = hfm.scanNextNode()) != null) {
//				System.out.println("string1: '"+node.getLabel()+"'");
//				System.out.println("string2: '"+label+"'");
				if (node.getLabel().equals(label)) {
					rid = hfm.getCurRID();
					key = new StringKey(node.getLabel());
					nodelabelbtree.insert(key, rid);
					hfm.closeScan();
					break;
				}
			}
		}
	}

	public void insertEdgetoELBT(HFManager hfm, String fileName) throws Exception {

		System.out.println("start inserting edges");
		File file = new File(fileName);
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] data = line.split(" ");
			String label = data[2];
			int weight = Integer.valueOf(data[3]);

			RID rid = new RID();
			Edge edge = new Edge();
			KeyClass key;
			hfm.initScanEdge();

			while ((edge = hfm.scanNextEdge()) != null) {
				if (edge.getLabel().equals(label)) {
					rid = hfm.getCurRID();
					key = new StringKey(edge.getLabel());
					edgelabelbtree.insert(key, rid);
				}
			}
		}
	}

	public void insertEdgetoELBT_S(HFManager hfm, String fileName) throws Exception {

		System.out.println("start inserting edges");
		File file = new File(fileName);
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] data = line.split(" ");
			String label = data[0];
			int weight = Integer.valueOf(data[3]);

			RID rid = new RID();
			Edge edge = new Edge();
			KeyClass key;
			hfm.initScanEdge();

			while ((edge = hfm.scanNextEdge()) != null) {
				if (edge.getSource().equals(label)) {
					rid = hfm.getCurRID();
					key = new StringKey(edge.getSource());
					edgelabelbtree_s.insert(key, rid);
				}
			}
		}
	}

	public void insertEdgetoELBT_D(HFManager hfm, String fileName) throws Exception {

		System.out.println("start inserting edges");
		File file = new File(fileName);
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] data = line.split(" ");
			String label = data[1];
			int weight = Integer.valueOf(data[3]);

			RID rid = new RID();
			Edge edge = new Edge();
			KeyClass key;
			hfm.initScanEdge();

			while ((edge = hfm.scanNextEdge()) != null) {
				if (edge.getDestination().equals(label)) {
					rid = hfm.getCurRID();
					key = new StringKey(edge.getDestination());
					edgelabelbtree_d.insert(key, rid);
				}
			}
		}
	}

	public void insertNodetoZT(HFManager hfm, String filename) throws Exception {
		System.out.println("start inserting nodes to Z-Tree");
		File file = new File(filename);
		Scanner scan = new Scanner(file);
		int cnt = 0;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] data = line.split(" ");
			String label = data[0];

			RID rid = new RID();
			Node node = new Node();
			KeyClass key;
			hfm.initScanNode();

			while ((node = hfm.scanNextNode()) != null) {
				if (node.getLabel().equals(label)) {
					rid = hfm.getCurRID();
					key = new StringKey(ZEncoder.encode(node.getDesc()));
					nodeDescriptorTree.insert(key, rid);
					hfm.closeScan();
					break;
				}
			}
		}
	}

	public void insertEdgetoEWBT(HFManager hfm, String fileName) throws Exception {

		System.out.println("start inserting edges");
		File file = new File(fileName);
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] data = line.split(" ");
			String label = data[2];
			int weight = Integer.valueOf(data[3]);

			RID rid = new RID();
			Edge edge = new Edge();
			KeyClass key;
			hfm.initScanEdge();

			while ((edge = hfm.scanNextEdge()) != null) {
				if (edge.getWeight() == weight) {
					rid = hfm.getCurRID();
					key = new IntegerKey(edge.getWeight());
					edgeweightbtree.insert(key, rid);
				}
			}
		}

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
	public void NodeQuery1(GraphDBManager db) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException, HFDiskMgrException, HFBufMgrException, Exception{
		BTFileScan s = nodelabelbtree.new_scan(null, null);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Node n = new Node(db.hfmgr.getNodefile().getRecord(((LeafData) itr.data).getData()));
			n.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}
	
	public void NodeQuery4(GraphDBManager db, String label) throws FieldNumberOutOfBoundException, InvalidTypeException, InvalidTupleSizeException, InvalidSlotNumberException, HFException, HFDiskMgrException, HFBufMgrException, Exception{
		KeyClass k = new StringKey(label);
		BTFileScan s = db.btmgr.nodelabelbtree.new_scan(k, k);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Node n = new Node(db.hfmgr.getNodefile().getRecord(((LeafData) itr.data).getData()));
			n.print();
			
			BTFileScan d = db.btmgr.edgelabelbtree_d.new_scan(k, k);
			KeyDataEntry itrd = d.get_next();
			while(itrd!=null){
				Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itrd.data).getData()));
				e.print();
				itrd = d.get_next();
			}
			d.DestroyBTreeFileScan();
			BTFileScan ss = db.btmgr.edgelabelbtree_s.new_scan(k, k);
			KeyDataEntry itrs = ss.get_next();
			while(itrs!=null){
				Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itrs.data).getData()));
				e.print();
				itrs = ss.get_next();
			}
			ss.DestroyBTreeFileScan();
			System.out.println();
		}
		s.DestroyBTreeFileScan();
		System.out.println();
	}
	
	public void EdgeQuery1(GraphDBManager db) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException, HFDiskMgrException, HFBufMgrException, Exception{
		BTFileScan s = db.btmgr.edgelabelbtree_s.new_scan(null, null);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itr.data).getData()));
			e.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}
	
	public void EdgeQuery2(GraphDBManager db) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException, HFDiskMgrException, HFBufMgrException, Exception{
		BTFileScan s = db.btmgr.edgelabelbtree_d.new_scan(null, null);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itr.data).getData()));
			e.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}

	public void EdgeQuery3(GraphDBManager db) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException, HFDiskMgrException, HFBufMgrException, Exception{
		BTFileScan s = db.btmgr.edgelabelbtree.new_scan(null, null);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itr.data).getData()));
			e.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}
	
	public void EdgeQuery4(GraphDBManager db) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException, HFDiskMgrException, HFBufMgrException, Exception{
		BTFileScan s = db.btmgr.edgeweightbtree.new_scan(null, null);
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itr.data).getData()));
			e.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}
	
	public void EdgeQuery5(GraphDBManager db, int lowerb, int upperb) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException, HFDiskMgrException, HFBufMgrException, Exception{
		BTFileScan s = db.btmgr.edgeweightbtree.new_scan(new IntegerKey(lowerb), new IntegerKey(upperb));
		KeyDataEntry itr = s.get_next();
		while (itr != null) {
			Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itr.data).getData()));
			e.print();
			itr = s.get_next();
		}
		s.DestroyBTreeFileScan();
	}

	public void NodeQueryIndex3(GraphDBManager db, Descriptor target, int distance) throws Exception {
		DescriptorKey descKey = new DescriptorKey(ZEncoder.encode(target));
		ZFileRangeScan rangeScan = new ZFileRangeScan(this.nodeDescriptorTree, descKey, distance);
		KeyDataEntry itr = rangeScan.getNext();
		while (itr != null) {
			Node node = new Node(db.hfmgr.getNodefile().getRecord(((LeafData) itr.data).getData()));
			node.print();
			itr = rangeScan.getNext();
		}
		rangeScan.endScan();
	}

	public void NodeQueryIndex5(GraphDBManager db, Descriptor target, int distance) throws Exception {
		DescriptorKey descKey = new DescriptorKey(ZEncoder.encode(target));
		ZFileRangeScan rangeScan = new ZFileRangeScan(this.nodeDescriptorTree, descKey, distance);
		KeyDataEntry itr = rangeScan.getNext();
		while (itr != null) {
			Node node = new Node(db.hfmgr.getNodefile().getRecord(((LeafData) itr.data).getData()));
			if (node.getDesc().distance(target) == distance) {
				node.print();

				StringKey k = new StringKey(node.getLabel());

				BTFileScan d = db.btmgr.edgelabelbtree_d.new_scan(k, k);
				KeyDataEntry itrd = d.get_next();
				while(itrd!=null){
					Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itrd.data).getData()));
					e.print();
					itrd = d.get_next();
				}
				d.DestroyBTreeFileScan();
				BTFileScan ss = db.btmgr.edgelabelbtree_s.new_scan(k, k);
				KeyDataEntry itrs = ss.get_next();
				while(itrs!=null){
					Edge e = new Edge(db.hfmgr.getEdgefile().getRecord(((LeafData) itrs.data).getData()));
					e.print();
					itrs = ss.get_next();
				}
				ss.DestroyBTreeFileScan();
				System.out.println();
			}
			itr = rangeScan.getNext();
		}
		rangeScan.endScan();
	}
}
