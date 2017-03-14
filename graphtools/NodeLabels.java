package graphtools;

import java.io.*;
import java.util.*;
import java.lang.*;
import heap.*;
import nodeheap.Node;
import bufmgr.*;
import diskmgr.*;
import edgeheap.*;
import global.*;
import btree.*;
import graphtools.*;

class NodeLabelsDriver implements GlobalConst {
	public int keyType;
	public BTreeFile file;
	public HFManager hfm = null;
	public BTManager btm = null;

	protected String dbpath;

	NodeLabelsDriver() {

	}

	NodeLabelsDriver(HFManager hfms, BTManager btms) {
		this.hfm = hfms;
		this.btm = btms;
	}

	public void runTests() throws Exception {
		keyType = AttrType.attrString;
		System.out.println("start test 1!!!");
		ConstructBTNL();
		System.out.println("start test 2!!!");
		BT.printBTree(file.getHeaderPage());
		System.out.println("start test 3!!!");
		BT.printAllLeafPages(file.getHeaderPage());

	}

	public void ConstructBTNL() throws Exception {

		file = new BTreeFile("NodeLabelTree", keyType, 20, 1);// full delete
		Node node = new Node();
		KeyClass key;
		RID rid = new RID();
		hfm.initScanNode();

		while ((node = hfm.scanNextNode()) != null) {
			key = new StringKey(node.getLabel());
			rid = hfm.getCurRID();
			file.insert(key, rid);
		}
		btm.setNodelabelbtree(file);
	}

}

public class NodeLabels implements GlobalConst {
	public static void main(String[] argvs) {
		System.out.println("testing!!!");

		EdgeLabelsDriver test = new EdgeLabelsDriver();
		try {
			test.runTests();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void buildEdgeIndex(HFManager hfm) {
		EdgeLabelsDriver test = new EdgeLabelsDriver();
		try {
			test.runTests();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
