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
import iterator.*;

public class GraphDBManager implements GlobalConst {

	public int deleteFashion;
	private String dbpath;
	private String logpath;
	private String nodefilenname;
	private String edgefilename;
	public HFManager hfmgr;
	public BTManager btmgr;
	/* TODO: init() will cause a problem */
	boolean firsttime = true;
	boolean firsttime_e=true;

	public void init(String dbname) {
		dbpath = dbname + ".minibase-db";
		logpath = dbname + ".minibase-log";
		SystemDefs sysdef = new SystemDefs(dbpath, 5000, 3000, "Clock");
		System.out.println("\n" + "DB initializing" + "\n");
		hfmgr = new HFManager();
		btmgr = new BTManager();
	}

	public void init(String dbname, int bufNum) {
		dbpath = dbname + ".minibase-db";
		logpath = dbname + ".minibase-log";
		SystemDefs sysdef = new SystemDefs(dbpath, 50000, bufNum, "Clock");
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
		btmgr.insertNodetoNLBT(hfmgr, nodefilename);
		btmgr.insertNodetoZT(hfmgr, nodefilename);
	}

	public void insertEdges(String edgefilename) throws Exception {
		hfmgr.insertEdgesFromFile(edgefilename);
		btmgr.insertEdgeBtress(hfmgr);
//		BT.printAllLeafPages(btmgr.getEdgelabelbtree().getHeaderPage());

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

			BTFileScan mydfilescan = btmgr.getEdgelabelbtree_d().new_scan(key, key);
			KeyDataEntry itrd = mydfilescan.get_next();
			while (itrd != null) {
				rid_edl.add(((LeafData) itrd.data).getData());
				itrd = mydfilescan.get_next();
			}
			
			mysfilescan.DestroyBTreeFileScan();
			mydfilescan.DestroyBTreeFileScan();

			// delete node
			if (rid_node != null) {
				hfmgr.deletenode(rid_node);
				btmgr.deletenode(key, rid_node);
			} else {
				System.err.println("Delete failed: no such node");
			}
			boolean delete_destination=false;
			// delete edge whose source is this node
			if (!rid_esl.isEmpty()) {
				if (!rid_edl.isEmpty()) {
					for (int i = 0; i < rid_esl.size(); i++) {
						hfmgr.deleteedge(rid_esl.get(i));
					}
					for (int j = 0; j < rid_edl.size(); j++) {
						int count = 0;
						for(int i=0;i < rid_esl.size();i++){
							if(rid_esl.get(i).equals(rid_edl.get(j))){
								count=1;
							}
						}
						if (count==0&&delete_destination){
							hfmgr.deleteedge(rid_edl.get(j));
						}
					}
				} else {
					for (int i = 0; i < rid_esl.size(); i++) {
						hfmgr.deleteedge(rid_esl.get(i));
					}
				}
			} else if (!rid_edl.isEmpty()&&delete_destination) {
				for (int i = 0; i < rid_edl.size(); i++) {
					hfmgr.deleteedge(rid_edl.get(i));
				}
			}
			cnt++;
		}
		rebuildtrees();
		System.out.println(cnt + " nodes deleted");
	}
	
	public void deleteEdge(String fileName) throws Exception {
		System.out.println("start deleting edges");
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
			List<RID> rid = new LinkedList<>();
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
			
//			System.out.println();
			for(int i=0;i<rid_s.size();i++){
				for (int j =0;j<rid_d.size();j++){
					if(rid_s.get(i).pageNo.pid==rid_d.get(j).pageNo.pid&&rid_s.get(i).slotNo==rid_d.get(j).slotNo){
						for(int k=0;k<rid_l.size();k++){
							if(rid_s.get(i).pageNo.pid==rid_l.get(k).pageNo.pid&&rid_s.get(i).slotNo==rid_l.get(k).slotNo)
								rid.add(rid_s.get(i));
						}
					}
				}
			}
			
			if(!rid.isEmpty()){
				for(int i=0;i<rid.size();i++){
					hfmgr.deleteedge(rid.get(i));
				}
				
			}
			cnt++;
			
		}
		System.out.println("deleted "+cnt+" edges");
		rebuildtrees();
//		BT.printAllLeafPages(btmgr.getEdgelabelbtree().getHeaderPage());
		}
	public void closeDB(){
		try{
			btmgr.closeAllFile();
			SystemDefs.closeSystem();
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error encountered during closing db\n");
		}
	}
	
	private void rebuildtrees(){
		try {
			btmgr.getEdgelabelbtree().destroyFile();
			btmgr.getEdgelabelbtree_d().destroyFile();
			btmgr.getEdgelabelbtree_s().destroyFile();
			btmgr.getEdgeweightbtree().destroyFile();
			btmgr.getEdgeidbtree().destroyFile();
			btmgr.insertEdgeBtress(hfmgr);
		} catch (IteratorException | UnpinPageException | FreePageException | DeleteFileEntryException
				| ConstructPageException | PinPageException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void clearPerTask(){
		try{
			btmgr.closeAllFile();
			SystemDefs.flushBMPages();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] argvs) {
		String dbname = "testdb";
		String nodefilename = "NodeInsertData.txt";
		String edgefilename = "EdgeInsertData.txt";

		try {
			db.init(dbname);
			
			db.insertNodes(nodefilename);
			db.insertEdges(edgefilename);

			db.deleteDBFile();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error encountered during initializing db\n");
			Runtime.getRuntime().exit(1);
		}
	}
}