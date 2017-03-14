package graphtools;

import java.io.*;
import java.util.*;
import java.lang.*;
import heap.*;
import bufmgr.*;
import diskmgr.*;
import edgeheap.Edge;
import global.*;
import btree.*;

class GraphDBManager implements GlobalConst {

	public int deleteFashion;
	private String dbpath;
	private String logpath;
	private String nodefilenname;
	private String edgefilename;
	public HFManager hfmgr;
	public BTManager btmgr;
	boolean firsttime = true;
	boolean firsttime_e=true;

	public void init(String dbname) {
		dbpath = dbname + ".minibase-db";
		logpath = dbname + ".minibase-log";
		SystemDefs sysdef = new SystemDefs(dbpath, 5000, 5000, "Clock");
		System.out.println("\n" + "DB initializing" + "\n");
		hfmgr = new HFManager();
		btmgr = new BTManager();
	}

	public void init(String dbname, int bufNum) {
		dbpath = dbname + ".minibase-db";
		logpath = dbname + ".minibase-log";
		SystemDefs sysdef = new SystemDefs(dbpath, 5000, bufNum, "Clock");
		System.out.println("\n" + "DB initializing" + "\n");
		hfmgr = new HFManager();
		btmgr = new BTManager();
	}
	public void deleteDBFile() {

		// Kill anything that might be hanging around
		String remove_logcmd;
		String remove_dbcmd;
		String remove_cmd = "/bin/rm -rf ";

		remove_logcmd = remove_cmd + logpath;
		remove_dbcmd = remove_cmd + dbpath;

		// clear database
		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);
		} catch (IOException e) {
			System.err.println("IO error: " + e);
		}
		System.out.println("\n" + "DB deleted in disk" + "\n");

	}

	public void insertNodes(String nodefilename) throws Exception {
		hfmgr.insertNodesFromFile(nodefilename);
		if (firsttime) {
			NodeLabelsDriver nld = new NodeLabelsDriver(hfmgr, btmgr);
			try {
				nld.ConstructBTNL();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			firsttime = false;
		} else {
			btmgr.insertNodetoNLBT(hfmgr, nodefilename);
		}
	}

	public void insertEdges(String edgefilename) throws Exception {
		hfmgr.insertEdgesFromFile(edgefilename);
		if (firsttime_e) {
			EdgeLabelsDriver eld = new EdgeLabelsDriver(hfmgr, btmgr);
			EdgeWeightDriver ewd = new EdgeWeightDriver(hfmgr, btmgr);
			try {
				eld.ConstructBTEL();
				eld.ConstructBTEL_D();
				eld.ConstructBTEL_S();
				ewd.ConstructBTEW();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			firsttime_e = false;
		} else {
			btmgr.insertEdgetoELBT(hfmgr, edgefilename);
			btmgr.insertEdgetoELBT_S(hfmgr, edgefilename);
			btmgr.insertEdgetoELBT_D(hfmgr, edgefilename);
			btmgr.insertEdgetoEWBT(hfmgr, edgefilename);
		}
		BT.printAllLeafPages(btmgr.getEdgelabelbtree().getHeaderPage());

	}

	public void deleteNode(String fileName) throws Exception {

		System.out.println("start deleting nodes");
		File file = new File(fileName);
		Scanner scan = new Scanner(file);
		int cnt = 0;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			RID rid_node = new RID();

			String label = line;
			KeyClass key = new StringKey(label);
			rid_node = btmgr.getRIDFromLabel_Node(label);
			List<RID> rid_esl = new LinkedList<>();
			List<RID> rid_edl = new LinkedList<>();

			BTFileScan mysfilescan = btmgr.getEdgelabelbtree_s().new_scan(key, key);
			KeyDataEntry itr = mysfilescan.get_next();
			while (itr != null) {
				rid_esl.add(((LeafData) itr.data).getData());
				itr = mysfilescan.get_next();
			}

			BTFileScan mydfilescan = btmgr.getEdgelabelbtree_s().new_scan(key, key);
			KeyDataEntry itrd = mydfilescan.get_next();
			while (itrd != null) {
				rid_edl.add(((LeafData) itrd.data).getData());
				itrd = mydfilescan.get_next();
			}

			// delete node
			if (rid_node != null) {
				hfmgr.deletenode(rid_node);
				btmgr.deletenode(key, rid_node);
			} else {
				System.err.println("Delete failed: no such node");
			}

			// delete edge whose source is this node
			if (!rid_esl.isEmpty()) {
				if (!rid_edl.isEmpty()) {

					for (int i = 0; i < rid_esl.size(); i++) {
						btmgr.deleteedge_s(key, rid_esl.get(i));
						Edge edge = new Edge(hfmgr.getEdgefile().getRecord(rid_esl.get(i)));
						KeyClass ekey = new StringKey(edge.getLabel());
						btmgr.deleteedge(ekey, rid_esl.get(i));
						hfmgr.deleteedge(rid_esl.get(i));
					}
					for (int j = 0; j < rid_edl.size(); j++) {
						btmgr.deleteedge_d(key, rid_edl.get(j));
					}
				} else {
					for (int i = 0; i < rid_esl.size(); i++) {
						Edge edge = new Edge(hfmgr.getEdgefile().getRecord(rid_esl.get(i)));
						KeyClass ekey = new StringKey(edge.getLabel());
						btmgr.deleteedge(ekey, rid_esl.get(i));
						btmgr.deleteedge_s(key, rid_esl.get(i));
						hfmgr.deleteedge(rid_esl.get(i));
					}
				}
			} else if (!rid_edl.isEmpty()) {
				for (int i = 0; i < rid_edl.size(); i++) {
					Edge edge = new Edge(hfmgr.getEdgefile().getRecord(rid_esl.get(i)));
					KeyClass ekey = new StringKey(edge.getLabel());
					btmgr.deleteedge(ekey, rid_esl.get(i));
					btmgr.deleteedge_d(key, rid_edl.get(i));
					hfmgr.deleteedge(rid_esl.get(i));
				}
			}
			cnt++;
		}
		System.out.println(cnt + " nodes deleted");
	}
	
	public void deleteEdge(String fileName) throws Exception {
		System.out.println("start deleting nodes");
		File file = new File(fileName);
		Scanner scan = new Scanner(file);
		int cnt = 0;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String data[] = line.split(" ");
			String sourcelabel = data[0];
			String destinationlabel = data[1];
			String nodelabel = data[2];
			
			KeyClass[] key = {new StringKey(sourcelabel), new StringKey(destinationlabel), new StringKey(nodelabel)};
			
			List<RID> rid_s = new LinkedList<>();
			List<RID> rid_d = new LinkedList<>();
			List<RID> rid_l = new LinkedList<>();
			BTFileScan mysfilescan = btmgr.getEdgelabelbtree_s().new_scan(key[0], key[0]);
			KeyDataEntry itr = mysfilescan.get_next();
			while (itr != null) {
				rid_s.add(((LeafData) itr.data).getData());
				itr = mysfilescan.get_next();
			}
			BTFileScan mydfilescan = btmgr.getEdgelabelbtree_d().new_scan(key[1], key[1]);
			KeyDataEntry itrd = mydfilescan.get_next();
			while (itrd != null) {
				rid_d.add(((LeafData) itrd.data).getData());
				itrd = mydfilescan.get_next();
			}
			BTFileScan myefilescan = btmgr.getEdgelabelbtree().new_scan(key[2], key[2]);
			KeyDataEntry itre = myefilescan.get_next();
			while (itre != null) {
				rid_l.add(((LeafData) itre.data).getData());
				itre = myefilescan.get_next();
			}
			mysfilescan.DestroyBTreeFileScan();
			mydfilescan.DestroyBTreeFileScan();
			myefilescan.DestroyBTreeFileScan();
			
			System.out.println();
			rid_s.retainAll(rid_d);
			rid_s.retainAll(rid_l);
			
			if(!rid_s.isEmpty()){
				for(int i=0;i<rid_s.size();i++){
					btmgr.deleteedge_s(key[0], rid_s.get(i));
					btmgr.deleteedge_d(key[1], rid_s.get(i));
					btmgr.deleteedge(key[2], rid_s.get(i));
					hfmgr.deleteedge(rid_s.get(i));
					hfmgr.deleteedge(rid_s.get(i));
				}
				
			}
			cnt++;
			
		}
		System.out.println("deleted "+cnt+" edges");
		BT.printAllLeafPages(btmgr.getEdgelabelbtree().getHeaderPage());
	}

	public static void main(String[] argvs) {
		String dbname = argvs[1];
		String nodefilename = argvs[0];
		String edgefilename = argvs[2];
		String insertdeletefilename = argvs[3];
		String nodedeletefilename = argvs[4];

		try {
			GraphDBManager db = new GraphDBManager();
			db.init(dbname);
			db.insertNodes(nodefilename);
			db.insertEdges(edgefilename);
			// db.insertNodes(insertdeletefilename);

			db.deleteEdge(nodedeletefilename);

			db.deleteDBFile();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error encountered during initializing db\n");
			Runtime.getRuntime().exit(1);
		}
	}
}