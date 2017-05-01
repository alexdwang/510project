package graphtools;

import java.lang.Integer;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import diskmgr.PCounter;
import global.*;

public class queryExpr implements GlobalConst {

	public static void main(String[] argvs){
		//initiate database
		String dbname = "testdb";
		String nodefilename = "NodeInsertData.txt";
		String edgefilename = "EdgeInsertData.txt";
		db.init(dbname);
		
		PathExpressionQuerys pathquery = new PathExpressionQuerys();
		NLJHelper njlhelper = new NLJHelper();
		List<List<String>> input = new LinkedList();
		List<String> ninput = new LinkedList();
		List einput = new LinkedList();
		
		while (true) {
			System.out.println("Please input the query expression: ");
			Scanner scan = new Scanner(System.in);
			String cmd = null;
			cmd = scan.nextLine();

			String delimt1 = "<-";
			String delimt2 = "/";
			String delimt3 = ";";
			String delimt4 = "//";
			String[] cmds;
			String tokens1;
			String[] tokens2;
			String res1;
			cmds = cmd.split(delimt1);
			parser parser = new parser();
			
			input.clear();
			ninput.clear();
			einput.clear();
			int[] weight = new int[3];
			String[] label = new String[3];
			boolean distinct = false;
			

			switch (cmds[0]) {
			case "insert":
			{
				try {
					PCounter.initialize();
					db.btmgr.openAllFile();
					db.insertNodes(nodefilename);
					db.clearPerTask();
					db.btmgr.openAllFile();
					db.insertEdges(edgefilename);
					db.clearPerTask();
					pathquery.printRWPerStep();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case "PQ1a":
				db.btmgr.openIndexFile(BTManager.nodeDescriptorTree_filename);
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}

				PCounter.initialize();
				for (int i = 0; i < tokens2.length; i++) {

					// decide whether it is a node_lable or node descriptor
					String res = parser.discrmN(tokens2[i]);
					if (res == "descriptor") {
						Descriptor desc = parser.convDesc(tokens2[i]);
						List<String> labels = njlhelper.nodeDescToLabel(desc);
						input.add(labels);
					} else if (res == "label") {
						List<String> labels = new LinkedList();
						labels.add(tokens2[i]);
						input.add(labels);
					}
				}
				pathquery.printResult(pathquery.PQ1a(input));
				pathquery.printRWPerStep();
				db.clearPerTask();
				break;

			case "PQ1b":
				db.btmgr.openIndexFile(BTManager.nodeDescriptorTree_filename);
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}

				PCounter.initialize();
				for (int i = 0; i < tokens2.length; i++) {

					// decide whether it is a node_lable or node descriptor
					String res = parser.discrmN(tokens2[i]);
					if (res == "descriptor") {
						Descriptor desc = parser.convDesc(tokens2[i]);
						List<String> labels = njlhelper.nodeDescToLabel(desc);
						input.add(labels);
					} else if (res == "label") {
						List<String> labels = new LinkedList();
						labels.add(tokens2[i]);
						input.add(labels);
					}

				}
				pathquery.printResult(pathquery.PQ1b(input));
				pathquery.printRWPerStep();
				db.clearPerTask();
				break;

			case "PQ1c":
				db.btmgr.openIndexFile(BTManager.nodeDescriptorTree_filename);
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}

				PCounter.initialize();
				for (int i = 0; i < tokens2.length; i++) {

					// decide whether it is a node_lable or node descriptor
					String res = parser.discrmN(tokens2[i]);
					if (res == "descriptor") {
						Descriptor desc = parser.convDesc(tokens2[i]);
						List<String> labels = njlhelper.nodeDescToLabel(desc);
						input.add(labels);
					} else if (res == "label") {
						List<String> labels = new LinkedList();
						labels.add(tokens2[i]);
						input.add(labels);
					}

				}
				pathquery.printResult(pathquery.PQ1c(input));
				pathquery.printRWPerStep();
				db.clearPerTask();
				break;

			case "PQ2a":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}

				PCounter.initialize();
				res1 = parser.discrmN(tokens2[0]);
				if (res1 == "descriptor") {
					Descriptor desc = parser.convDesc(tokens2[0]);
					ninput = njlhelper.nodeDescToLabel(desc);
					
				} else if (res1 == "label") {
					ninput.add(tokens2[0]);
				}

				for (int i = 1; i < tokens2.length; i++) {
					String res = parser.discrmE(tokens2[i]);
					if (res == "label") {
						einput.add(tokens2[i].substring(2));
					} else if (res == "weight") {
						einput.add(Integer.parseInt(tokens2[i].substring(2)));
					} else if(res.isEmpty()){
						System.out.println("Invalid input");
						System.out.println("Should be like: PQ2a<-NN/E:5_2/W:20/...");
						return;
					}

				}
				pathquery.printResult(pathquery.PQ2a(ninput, einput));
				pathquery.printRWPerStep();
				db.clearPerTask();
				break;

			case "PQ2b":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}

				PCounter.initialize();
				res1 = parser.discrmN(tokens2[0]);
				if (res1 == "descriptor") {
					Descriptor desc = parser.convDesc(tokens2[0]);
					ninput = njlhelper.nodeDescToLabel(desc);
					
				} else if (res1 == "label") {
					ninput.add(tokens2[0]);
				}

				for (int i = 1; i < tokens2.length; i++) {
					String res = parser.discrmE(tokens2[i]);
					if (res == "label") {
						einput.add(tokens2[i].substring(2));
					} else if (res == "weight") {
						einput.add(Integer.parseInt(tokens2[i].substring(2)));
					} else if(res.isEmpty()){
						System.out.println("Invalid input");
						System.out.println("Should be like: PQ1a<-NN/E:5_2/W:20/...");
						return;
					}
				}
				pathquery.printResult(pathquery.PQ2b(ninput, einput));
				pathquery.printRWPerStep();
				db.clearPerTask();
				break;

			case "PQ2c":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}

				PCounter.initialize();
				res1 = parser.discrmN(tokens2[0]);
				if (res1 == "descriptor") {
					Descriptor desc = parser.convDesc(tokens2[0]);
					ninput = njlhelper.nodeDescToLabel(desc);
					
				} else if (res1 == "label") {
					ninput.add(tokens2[0]);
				}

				for (int i = 1; i < tokens2.length; i++) {
					String res = parser.discrmE(tokens2[i]);
					if (res == "label") {
						einput.add(tokens2[i].substring(2));
					} else if (res == "weight") {
						einput.add(Integer.parseInt(tokens2[i].substring(2)));
					} else if(res.isEmpty()){
						System.out.println("Invalid input");
						System.out.println("Should be like: PQ1a<-NN/E:5_2/W:20/...");
						return;
					}
				}
				pathquery.printResult(pathquery.PQ2c(ninput, einput));
				pathquery.printRWPerStep();
				db.clearPerTask();
				break;
				
			case "PQ3a":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt4);
				res1 = parser.discrmN(tokens2[0]);
				if (tokens2.length !=2) {
					System.err.println("Invalid query condition");
					return;
				}

				PCounter.initialize();
				if (res1 == "descriptor") {
					Descriptor desc = parser.convDesc(tokens2[0]);
					ninput = njlhelper.nodeDescToLabel(desc);
					
				} else if (res1 == "label") {
					ninput.add(tokens2[0]);
				}
				if(tokens2[1].substring(0,2).equals("N:")){
					pathquery.printResult(pathquery.PQ3a_max_num_edge(ninput, Integer.parseInt(tokens2[1].substring(2))));
					db.clearPerTask();
				}
				else if(tokens2[1].substring(0,2).equals("W:")){
					pathquery.printResult(pathquery.PQ3a_max_weight(ninput, Integer.parseInt(tokens2[1].substring(2))));
					db.clearPerTask();
				}
				else{
					System.err.println("Invalid PQ3a format. Valid format should be like:");
					System.err.println("'PQ3a<-NN//N:100' for max number of edges query");
					System.err.println("'PQ3a<-NN//W:100' for max total weight query");
				}
				pathquery.printRWPerStep();
				break;
				
			case "PQ3b":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt4);
				res1 = parser.discrmN(tokens2[0]);
				if (tokens2.length > 2) {
					System.err.println("Invalid query condition");
				}

				PCounter.initialize();
				if (res1 == "descriptor") {
					Descriptor desc = parser.convDesc(tokens2[0]);
					ninput = njlhelper.nodeDescToLabel(desc);
					
				} else if (res1 == "label") {
					ninput.add(tokens2[0]);
				}
				if(tokens2[1].substring(0,2).equals("N:")){
					pathquery.printResult(pathquery.PQ3b_max_num_edge(ninput, Integer.parseInt(tokens2[1].substring(2))));
					db.clearPerTask();
				}
				else if(tokens2[1].substring(0,2).equals("W:")){
					pathquery.printResult(pathquery.PQ3b_max_weight(ninput, Integer.parseInt(tokens2[1].substring(2))));
				db.clearPerTask();
				}
				else{
					System.err.println("Invalid PQ3b fomat. Valid fomat should be like:");
					System.err.println("'PQ3b<-NN//N:100' for max number of edges query");
					System.err.println("'PQ3b<-NN//W:100' for max total weight query");
				}
				pathquery.printRWPerStep();
				break;
				
			case "PQ3c":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt4);
				res1 = parser.discrmN(tokens2[0]);
				if (tokens2.length > 2) {
					System.err.println("Invalid query condition");
				}

				PCounter.initialize();
				if (res1 == "descriptor") {
					Descriptor desc = parser.convDesc(tokens2[0]);
					ninput = njlhelper.nodeDescToLabel(desc);
					
				} else if (res1 == "label") {
					ninput.add(tokens2[0]);
				}
				if(tokens2[1].substring(0,2).equals("N:")){
					pathquery.printResult(pathquery.PQ3c_max_num_edge(ninput, Integer.parseInt(tokens2[1].substring(2))));
					db.clearPerTask();
				}
				else if(tokens2[1].substring(0,2).equals("W:")){
					pathquery.printResult(pathquery.PQ3c_max_weight(ninput, Integer.parseInt(tokens2[1].substring(2))));
					db.clearPerTask();
				}
				else{
					System.err.println("Invalid PQ3c fomat. Valid fomat should be like:");
					System.err.println("'PQ3c<-NN//N:100' for max number of edges query");
					System.err.println("'PQ3c<-NN//W:100' for max total weight query");
				}
				pathquery.printRWPerStep();
				break;

			case "TQa":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt3);
				if (tokens2.length != 3) {
					System.err.println("Invalid query path");
				}

				PCounter.initialize();
				for (int i = 0; i < tokens2.length; i++) {
					String res = parser.discrmE(tokens2[i]);
					if (res == "label") {
						weight[i] = -1;
						label[i] = tokens2[i].substring(2);
					} else if (res == "weight") {
						weight[i] = Integer.valueOf(tokens2[i].substring(2));
						label[i] = "";
					}
				}
				distinct = false;
				SortMergeUtil.triangleQuery(weight, label, distinct);
				pathquery.printRWPerStep();
				db.clearPerTask();
				break;

			case "TQb":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt3);
				if (tokens2.length != 3) {
					System.err.println("Invalid query path");
				}

				PCounter.initialize();
				for (int i = 0; i < tokens2.length; i++) {
					String res = parser.discrmE(tokens2[i]);
					if (res == "label") {
						weight[i] = -1;
						label[i] = tokens2[i].substring(2);
					} else if (res == "weight") {
						weight[i] = Integer.valueOf(tokens2[i].substring(2));
						label[i] = "";
					}
				}
				distinct = false;
				SortMergeUtil.triangleQuery(weight, label, distinct);
				pathquery.printRWPerStep();
				db.clearPerTask();
				break;

			case "TQc":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt3);
				if (tokens2.length != 3) {
					System.err.println("Invalid query path");
				}

				PCounter.initialize();
				for (int i = 0; i < tokens2.length; i++) {
					String res = parser.discrmE(tokens2[i]);
					if (res == "label") {
						weight[i] = -1;
						label[i] = tokens2[i].substring(2);
					} else if (res == "weight") {
						weight[i] = Integer.valueOf(tokens2[i].substring(2));
						label[i] = "";
					}
				}
				distinct = true;
				SortMergeUtil.triangleQuery(weight, label, distinct);
				pathquery.printRWPerStep();
				db.clearPerTask();
				break;
				
			default:
				System.out.printf("Invalid query expression!");
				break;
				
			case "exit":
				db.closeDB();
				return;
			}
		}
	}
}
