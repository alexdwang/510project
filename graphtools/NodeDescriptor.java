package graphtools;

import btree.BT;
import btree.BTreeFile;
import btree.KeyClass;
import btree.StringKey;
import global.AttrType;
import global.GlobalConst;
import global.RID;
import nodeheap.Node;
import zindex.ZEncoder;
import zindex.ZTreeFile;

class NodeDescriptorDriver {
	public int keyType = AttrType.attrString;
	public ZTreeFile file;
	public HFManager hfm = null;
	public BTManager btm = null;

	protected String dbpath;

	NodeDescriptorDriver() {

	}

	NodeDescriptorDriver(HFManager hfms, BTManager btms) {
		this.hfm = hfms;
		this.btm = btms;
	}

	public void runTests() throws Exception {
		keyType = AttrType.attrString;
		System.out.println("start test 1!!!");
		ConstructZT();
		System.out.println("start test 2!!!");
		BT.printBTree(btm.getNodeDescriptorTree().getHeaderPage());
		System.out.println("start test 3!!!");
		BT.printAllLeafPages(btm.getNodeDescriptorTree().getHeaderPage());

	}

	public void ConstructZT() throws Exception {

		file = new ZTreeFile("NodeDescriptorTree");// full delete
		Node node = new Node();
		KeyClass key;
		RID rid = new RID();
		hfm.initScanNode();

		while ((node = hfm.scanNextNode()) != null) {
			key = new StringKey(ZEncoder.encode(node.getDesc()));
			rid = hfm.getCurRID();
			file.insert(key, rid);
		}
		btm.setNodeDescriptorTree(file);
	}

}

public class NodeDescriptor implements GlobalConst {
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
