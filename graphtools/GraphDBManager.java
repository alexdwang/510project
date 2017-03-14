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

class GraphDBManager implements  GlobalConst{

	public int deleteFashion;
	private String dbpath;  
	private String logpath;
	private String nodefilenname;
	private String edgefilename;
	public HFManager hfmgr;
	public BTManager btmgr;

	public void init(String dbname){
		dbpath = dbname + ".minibase-db"; 
		logpath = dbname + ".minibase-log";
		SystemDefs sysdef = new SystemDefs( dbpath, 5000 ,5000,"Clock");
		System.out.println ("\n" + "DB initializing" + "\n");
		hfmgr = new HFManager();
	}

	public void deleteDBFile(){

		// Kill anything that might be hanging around
	    String remove_logcmd;
	    String remove_dbcmd;
	    String remove_cmd = "/bin/rm -rf ";

	    remove_logcmd = remove_cmd + logpath;
	    remove_dbcmd = remove_cmd + dbpath;

		//clear database
	    try {
	      Runtime.getRuntime().exec(remove_logcmd);
	      Runtime.getRuntime().exec(remove_dbcmd);
	    }
	    catch (IOException e) {
	      System.err.println ("IO error: "+e);
	    }
	    System.out.println ("\n" + "DB deleted in disk" + "\n");
	    
	}

	public void insertNodes(String fileName)
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
		hfmgr.insertNodesFromFile(fileName);
	}

	public void insertEdges(String fileName)
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
		hfmgr.insertEdgesFromFile(fileName);
	}
	
	public void deleteNode(String fileName) throws Exception{

		System.out.println("start deleting nodes");
		File file = new File(fileName);
		Scanner scan = new Scanner(file);
		int cnt = 0;
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			RID rid_node=new RID();
			RID rid_es=new RID();
			RID rid_ed=new RID();

			String label=line;
			KeyClass key=new StringKey(label); 
			rid_node = btmgr.getRIDFromLabel_Node(label);
			rid_es = btmgr.getRIDFromLabel_EdgeS(label);
			rid_ed = btmgr.getRIDFromLabel_EdgeD(label);
			
			
			//delete node
			if(rid_node!=null){
				hfmgr.deletenode(rid_node);
				btmgr.deletenode(key, rid_node);
			}else{
				System.err.println("Delete failed: no such node");
			}

			//delete edge whose source is this node
			if(rid_es!=null){
				if(rid_ed!=null){
					hfmgr.deleteedge(rid_es);
					btmgr.deleteedge_d(key, rid_ed);
					btmgr.deleteedge_s(key, rid_es);
					Edge edge = new Edge(hfmgr.getEdgefile().getRecord(rid_es));
					String Elabel=edge.getLabel();
					KeyClass ekey = new StringKey(Elabel);
					btmgr.deleteedge(ekey, rid_es);
				}else{
					hfmgr.deleteedge(rid_es);
					btmgr.deleteedge_s(key, rid_es);
					Edge edge = new Edge(hfmgr.getEdgefile().getRecord(rid_es));
					String Elabel=edge.getLabel();
					KeyClass ekey = new StringKey(Elabel);
					btmgr.deleteedge(ekey, rid_es);
				}
			}else if(rid_ed!=null){
				hfmgr.deleteedge(rid_ed);
				btmgr.deleteedge_d(key, rid_ed);
				Edge edge = new Edge(hfmgr.getEdgefile().getRecord(rid_ed));
				String Elabel=edge.getLabel();
				KeyClass ekey = new StringKey(Elabel);
				btmgr.deleteedge(ekey, rid_ed);
			}
			cnt++;
		}
		System.out.println(cnt + " nodes deleted");
	}


	public static void main(String [] argvs) {
		String dbname = argvs[1];
		String nodefilename = argvs[0];
	    try{ 
	      GraphDBManager db = new GraphDBManager();
	      db.init(dbname);
	      //nodemgr.insertNodesFromFile(nodefilename);
	      db.insertEdges(nodefilename);
	      EdgeWeightDriver ewd=new EdgeWeightDriver(db.hfmgr,db.btmgr);
	      ewd.runTests();
	      db.deleteDBFile();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      System.err.println ("Error encountered during initializing db\n");
	      Runtime.getRuntime().exit(1);
	    }
  }
}