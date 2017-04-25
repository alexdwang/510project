package graphtools;

import java.util.*;
import global.*;
import diskmgr.*;

public class GraphDBRunner implements GlobalConst{

	public static void help(){
		System.out.println("========= command helps =========");
		System.out.println("Legal commands are as follow");
		System.out.println("Please typing following commands to test");
		System.out.println("batchnodeinsert NODEFILENAME GRAPHDBNAME	## insert nodes into graphdb");
		System.out.println("batchedgeinsert EDGEFILENAME GRAPHDBNAME	## insert edges into graphdb");
		System.out.println("batchnodedelete NODEFILENAME GRAPHDBNAME	## delete nodes from graphdb");
		System.out.println("batchedgedelete EDGEFILENAME GRAPHDBNAME	## delete edges from graphdb");
		System.out.println("nodequery GRAPHDBNAME NUMBUF QTYPE INDEX [QUERYOPTIONS] ## query nodes");
		System.out.println("edgequery GRAPHDBNAME NUMBUF QTYPE INDEX [QUERYOPTIONS]	## query edges");
		System.out.println("quit 	## quit test");
		System.out.println("=================================");
	}

	public static void main(String [] argvs) {
		Set<String> dbset = new HashSet<String>();
		String curdb = "";
		int curbufnum = 5000;
		System.out.println("=== DBGraph test program ===");
		System.out.println("=== You can type 'help' to see valid comands ===");
		Scanner scan = new Scanner(System.in);
		String cmd = null;
		PCounter.initialize();
		do{
			System.out.print("###");
			cmd = scan.nextLine();
			String[] cmds = cmd.split(" ");
			switch(cmds[0]){
				case "batchnodeinsert":
				{
					System.out.println(curdb);
					if(cmds.length < 3){
						System.out.println("invalid arguements");
						break;
					}
					if(curdb.compareTo(cmds[2]) != 0){
						try{
							if(dbset.size() > 0) db.closeDB();;
							db.init(cmds[2], curbufnum);
						}catch(Exception e){
							System.err.println ("failed to change to new db\n");
							e.printStackTrace();
						}
						
						if(!dbset.contains(cmds[2])) dbset.add(cmds[2]);
						curdb = cmds[2];
					}
					try{
						db.insertNodes(cmds[1]);
					}catch(Exception e){
						System.err.println ("Error when inserting nodes");
						e.printStackTrace();
					}

					break;
				}
				case "batchedgeinsert":
				{
					if(cmds.length < 3){
						System.out.println("invalid arguements");
						break;
					}
					if(curdb.compareTo(cmds[2]) != 0){
						try{
							if(dbset.size() > 0) db.closeDB();;
							db.init(cmds[2]);
						}catch(Exception e){
							System.err.println ("failed to change to new db\n");
							e.printStackTrace();
						}
						
						if(!dbset.contains(cmds[2])) dbset.add(cmds[2]);
						curdb = cmds[2];
					}
					try{
						db.insertEdges(cmds[1]);
					}catch(Exception e){
						System.err.println ("Error when inserting nodes");
						e.printStackTrace();
					}
					break;
				}
				case "batchnodedelete":
				{
					if(cmds.length < 3){
						System.out.println("invalid arguements");
						break;
					}
					if(curdb.compareTo(cmds[2]) != 0){
						try{
							if(dbset.size() > 0) db.closeDB();;
							db.init(cmds[2]);
						}catch(Exception e){
							System.err.println ("failed to change to new db\n");
							e.printStackTrace();
						}
						
						if(!dbset.contains(cmds[2])) dbset.add(cmds[2]);
						curdb = cmds[2];
					}
					try{
						db.deleteNode(cmds[1]);
					}catch(Exception e){
						System.err.println ("Error when inserting nodes");
						e.printStackTrace();
					}
					break;
				}
				case "batchedgedelete":
				{
					if(cmds.length < 3){
						System.out.println("invalid arguements");
						break;
					}
					if(curdb.compareTo(cmds[2]) != 0){
						try{
							if(dbset.size() > 0) db.closeDB();
							db.init(cmds[2]);
						}catch(Exception e){
							System.err.println ("failed to change to new db\n");
							e.printStackTrace();
						}
						
						if(!dbset.contains(cmds[2])) dbset.add(cmds[2]);
						curdb = cmds[2];
					}
					try{
						db.deleteEdge(cmds[1]);
					}catch(Exception e){
						System.err.println ("Error when inserting nodes");
						e.printStackTrace();
					}
					break;
				}
				case "nodequery":
				{
					if(cmds.length < 5 ||
						cmds[3] == "2" && cmds.length != 10 ||
						cmds[3] == "3" && cmds.length != 11 ||
						cmds[3] == "4" && cmds.length != 6 ||
						cmds[3] == "5" && cmds.length != 11
						){
						System.out.println("invalid arguements");
						break;
					}
					int bufnum = Integer.valueOf(cmds[2]);
					if(curdb.compareTo(cmds[1]) != 0){
						try{
							if(curdb != "") db.closeDB();
							db.init(cmds[1], bufnum);
							curbufnum = bufnum;
						}catch(Exception e){
							System.err.println ("failed to change to new db\n");
							e.printStackTrace();
						}
					}
					if(!dbset.contains(cmds[1])) dbset.add(cmds[1]);
					curdb = cmds[1];
					int sortbufnum = Integer.valueOf(cmds[2]);
					//SystemDefs.updateBM(Integer.valueOf(cmds[2]));
					int qtype = Integer.valueOf(cmds[3]);
					int index = Integer.valueOf(cmds[4]);
					switch(qtype){
						case 0:
							db.hfmgr.NodeQuery(qtype, (short)sortbufnum);
							break;
						case 1:
							if(index == 1){
								try{
									db.btmgr.NodeQuery1(db);
								}catch(Exception e){
									e.printStackTrace();
								}
							}
							else db.hfmgr.NodeQuery(qtype,(short)sortbufnum);
							break;
						case 2:
						{
							Descriptor desc = new Descriptor();
							int[] values = new int[5];
							for(int i = 5; i < 10; ++i){
								values[i-5] = Integer.valueOf(cmds[i]);
							}
							desc.set(values);
							if(index == 1){
								try{
									db.btmgr.NodeQueryIndex2(db,desc);
								}catch(Exception e){
									e.printStackTrace();
								}
							}else db.hfmgr.NodeQuery2(desc);
							break;
						}
						case 3:
						{
							Descriptor desc = new Descriptor();
							int[] values = new int[5];
							for(int i = 5; i < 10; ++i){
								values[i-5] = Integer.valueOf(cmds[i]);
							}
							desc.set(values);
							double dist = Double.valueOf(cmds[10]);
							if(index == 1){
								try{
									//System.out.println(desc.toString() + " " + dist);
									db.btmgr.NodeQueryIndex3(db, desc, (int)dist);
								}catch(Exception e){
									e.printStackTrace();
								}
								break;
							}
							db.hfmgr.NodeQuery3(desc, dist);
							break;
						}
						case 4:
						{
							String label = cmds[5];
							if(index == 1){
								try{
									db.btmgr.NodeQuery4(db,label);
								}catch(Exception e){
									e.printStackTrace();
								}
							}else db.hfmgr.NodeQuery4(label);
							break;
						}
						case 5:
						{
							Descriptor desc = new Descriptor();
							int[] values = new int[5];
							for(int i = 5; i < 10; ++i){
								values[i-5] = Integer.valueOf(cmds[i]);
							}
							desc.set(values);
							double dist = Double.valueOf(cmds[10]);
							db.hfmgr.NodeQuery5(desc, dist);
							break;
						}
					}
					break;
				}
				case "edgequery":
				{
					if(cmds.length < 5 ||
						cmds[3] == "5" && cmds.length != 7
						){
						System.out.println("invalid arguements");
						break;
					}
					if(cmds[1].compareTo(curdb) != 0){
						try{
							if(curdb != "") db.closeDB();
							db.init(cmds[1], curbufnum);
						}catch(Exception e){
							System.err.println ("failed to change to new db\n");
							e.printStackTrace();
						}
					}
					if(!dbset.contains(cmds[1])) dbset.add(cmds[1]);
					curdb = cmds[1];
					int sortbufnum = Integer.valueOf(cmds[2]);
					int qtype = Integer.valueOf(cmds[3]);
					int index = Integer.valueOf(cmds[4]);
					switch(qtype){
						case 0:
							db.hfmgr.EdgeQuery01234(qtype, (short)sortbufnum);
							break;
						case 1:
							if(index == 1){
								try{
									db.btmgr.EdgeQuery1(db);
								}catch(Exception e){
									e.printStackTrace();
								}
							}else db.hfmgr.EdgeQuery01234(qtype, (short)sortbufnum);
							break;
						case 2:
							if(index == 1){
								try{
									db.btmgr.EdgeQuery2(db);
								}catch(Exception e){
									e.printStackTrace();
								}
							}else db.hfmgr.EdgeQuery01234(qtype, (short)sortbufnum);
							break;
						case 3:
							if(index == 1){
								try{
									db.btmgr.EdgeQuery3(db);
								}catch(Exception e){
									e.printStackTrace();
								}
							}else db.hfmgr.EdgeQuery01234(qtype, (short)sortbufnum);
							break;
						case 4:
							if(index == 1){
								try{
									db.btmgr.EdgeQuery4(db);
								}catch(Exception e){
									e.printStackTrace();
								}
							}else db.hfmgr.EdgeQuery01234(qtype, (short)sortbufnum);
							break;
						case 5:
						{
							int lb = Integer.valueOf(cmds[5]);
							int ub = Integer.valueOf(cmds[6]);

							if(index == 1){
								try{
									db.btmgr.EdgeQuery5(db, lb, ub);
								}catch(Exception e){
									e.printStackTrace();
								}
							}else db.hfmgr.EdgeQuery5(lb, ub);
							break;
						}	
					}
					break;
				}
				case "help":
					GraphDBRunner.help();
					break;
				case "quit":
				{
					try{
						if(db.btmgr!=null)
							db.closeDB();
					}catch(Exception e){
							System.err.println ("failed to close db\n");
							e.printStackTrace();
						}	
					break;
				}
				default:
					System.out.println("Can't find command " + cmds[0]);
					break;
			}

			if(cmd.compareTo("quit") != 0 && db.hfmgr!=null){
				System.out.println("Node Cnts = " + db.hfmgr.getNodeCnt());
				System.out.println("Edge Cnts = " + db.hfmgr.getEdgeCnt());
				System.out.println("Read Cnt = " + PCounter.rcounter);
				System.out.println("Write Cnt = " + PCounter.wcounter);
			}
		}while(cmd.compareTo("quit") != 0);
	}
}