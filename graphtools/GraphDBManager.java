package graphtools;

import java.io.*;
import java.util.*;
import java.lang.*;
import heap.*;
import bufmgr.*;
import diskmgr.*;
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