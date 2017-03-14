package graphtools;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import btree.*;
import edgeheap.*;
import global.*;
import heap.*;
import nodeheap.*;

public class BTManager {
	private BTreeFile nodelabelbtree;
	private BTreeFile edgelabelbtree;
	private BTreeFile edgeweightbtree;
	private BTreeFile edgelabelbtree_s;
	private BTreeFile edgelabelbtree_d;

	public BTreeFile getNodelabelbtree() {
		return nodelabelbtree;
	}

	public void setNodelabelbtree(BTreeFile nodelabelbtree) {
		this.nodelabelbtree = nodelabelbtree;
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
			return ((LeafData) this.getNodelabelbtree().new_scan(key, key).get_next().data).getData();
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
			return ((LeafData) this.getEdgelabelbtree().new_scan(key, key).get_next().data).getData();
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
			return ((LeafData) this.getEdgelabelbtree_s().new_scan(key, key).get_next().data).getData();
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
			return ((LeafData) this.getEdgelabelbtree_d().new_scan(key, key).get_next().data).getData();
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
		edgelabelbtree.Delete(key, rid);
		return true;
	}

	public boolean deleteedge_s(KeyClass key, RID rid) throws Exception {
		edgelabelbtree.Delete(key, rid);
		return true;
	}

}
