package graphtools;

import global.*;

class NodeInsert implements GlobalConst{
	public static void main(String [] argvs) {
		String dbname = argvs[1];
		String nodefilename = argvs[0];
	    try{ 
	      GraphDBManager db = new GraphDBManager();
	      db.init("asdf");
	      HFManager nodemgr = new HFManager();
	      nodemgr.insertNodesFromFile(nodefilename);
	      System.out.println(nodemgr.getNodeCnt());
	      //db.init("asdf");
	      //System.out.println(nodemgr.getNodeCnt());
	      SystemDefs.closeSystem();
	      //db.deleteDBFile();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      System.err.println ("Error encountered during initializing db\n");
	      Runtime.getRuntime().exit(1);
	    }
  }
}