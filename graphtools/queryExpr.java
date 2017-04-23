package graphtools;

import java.lang.Integer;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import global.*;

public class queryExpr implements GlobalConst {

	public void queryExprUI() {
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
			

			switch (cmds[0]) {

			case "PQ1a":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}

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

				break;

			case "PQ1b":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}
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

				break;

			case "PQ1c":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}
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
				
				break;

			case "PQ2a":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}
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
						einput.add(tokens2[i]);
					} else if (res == "weight") {
						einput.add(Integer.parseInt(tokens2[i]));
					}

				}
				pathquery.printResult(pathquery.PQ2a(ninput, einput));
				break;

			case "PQ2b":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}
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
						einput.add(tokens2[i]);
					} else if (res == "weight") {
						einput.add(Integer.parseInt(tokens2[i]));
					}

				}
				pathquery.printResult(pathquery.PQ2b(ninput, einput));
				break;

			case "PQ2c":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt2);
				if (tokens2.length < 2) {
					System.err.println("Invalid query path");
				}
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
						einput.add(tokens2[i]);
					} else if (res == "weight") {
						einput.add(Integer.parseInt(tokens2[i]));
					}

				}
				pathquery.printResult(pathquery.PQ2c(ninput, einput));
				break;
				
			case "PQ3a":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt4);
				res1 = parser.discrmN(tokens2[0]);
				if (tokens2.length > 2) {
					System.err.println("Invalid query condition");
				}
				if (res1 == "descriptor") {
					Descriptor desc = parser.convDesc(tokens2[0]);
					ninput = njlhelper.nodeDescToLabel(desc);
					
				} else if (res1 == "label") {
					ninput.add(tokens2[0]);
				}
				if(tokens2[1].substring(0,2).equals("N:"))
					pathquery.printResult(pathquery.PQ3a_max_num_edge(ninput, Integer.parseInt(tokens2[1].substring(2))));
				else if(tokens2[1].substring(0,2).equals("W:"))
					pathquery.printResult(pathquery.PQ3a_max_weight(ninput, Integer.parseInt(tokens2[1].substring(2))));
				else{
					System.err.println("Invalid PQ3a fomat. Valid fomat should be like:");
					System.err.println("'PQ3a<-NN//N:100' for max number of edges query");
					System.err.println("'PQ3a<-NN//W:100' for max total weight query");
				}
				break;
				
			case "PQ3b":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt4);
				res1 = parser.discrmN(tokens2[0]);
				if (tokens2.length > 2) {
					System.err.println("Invalid query condition");
				}
				if (res1 == "descriptor") {
					Descriptor desc = parser.convDesc(tokens2[0]);
					ninput = njlhelper.nodeDescToLabel(desc);
					
				} else if (res1 == "label") {
					ninput.add(tokens2[0]);
				}
				if(tokens2[1].substring(0,2).equals("N:"))
					pathquery.printResult(pathquery.PQ3b_max_num_edge(ninput, Integer.parseInt(tokens2[1].substring(2))));
				else if(tokens2[1].substring(0,2).equals("W:"))
					pathquery.printResult(pathquery.PQ3b_max_weight(ninput, Integer.parseInt(tokens2[1].substring(2))));
				else{
					System.err.println("Invalid PQ3b fomat. Valid fomat should be like:");
					System.err.println("'PQ3b<-NN//N:100' for max number of edges query");
					System.err.println("'PQ3b<-NN//W:100' for max total weight query");
				}
				break;
				
			case "PQ3c":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt4);
				res1 = parser.discrmN(tokens2[0]);
				if (tokens2.length > 2) {
					System.err.println("Invalid query condition");
				}
				if (res1 == "descriptor") {
					Descriptor desc = parser.convDesc(tokens2[0]);
					ninput = njlhelper.nodeDescToLabel(desc);
					
				} else if (res1 == "label") {
					ninput.add(tokens2[0]);
				}
				if(tokens2[1].substring(0,2).equals("N:"))
					pathquery.printResult(pathquery.PQ3c_max_num_edge(ninput, Integer.parseInt(tokens2[1].substring(2))));
				else if(tokens2[1].substring(0,2).equals("W:"))
					pathquery.printResult(pathquery.PQ3c_max_weight(ninput, Integer.parseInt(tokens2[1].substring(2))));
				else{
					System.err.println("Invalid PQ3c fomat. Valid fomat should be like:");
					System.err.println("'PQ3c<-NN//N:100' for max number of edges query");
					System.err.println("'PQ3c<-NN//W:100' for max total weight query");
				}
				break;

			case "TQa":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt3);
				if (tokens2.length != 3) {
					System.err.println("Invalid query path");
				}
				for (int i = 0; i < tokens2.length; i++) {
					String res = parser.discrmE(tokens2[i]);
					if (res == "label") {

					} else if (res == "weight") {

					}
				}
				break;

			case "TQb":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt3);
				if (tokens2.length != 3) {
					System.err.println("Invalid query path");
				}
				for (int i = 0; i < tokens2.length; i++) {
					String res = parser.discrmE(tokens2[i]);
					if (res == "label") {

					} else if (res == "weight") {

					}
				}
				break;

			case "TQc":
				tokens1 = cmds[1];
				tokens2 = tokens1.split(delimt3);
				if (tokens2.length != 3) {
					System.err.println("Invalid query path");
				}
				for (int i = 0; i < tokens2.length; i++) {
					String res = parser.discrmE(tokens2[i]);
					if (res == "label") {

					} else if (res == "weight") {

					}
				}
				break;
			default:
				System.out.printf("Invalid query expression!");
				break;
				
			case "exit":
				return;
			}
		}
	}

	/*
	 * public static void main(String[] argvs){
	 * 
	 * System.out.printf("Please input the query expression: "); Scanner scan =
	 * new Scanner(System.in); String cmd = null; cmd = scan.nextLine();
	 * 
	 * String delimt1 = "<-"; String delimt2 = "/"; String delimt3 = ";";
	 * String[] cmds; String tokens1; String[] tokens2; String res1; cmds =
	 * cmd.split(delimt1); parser parser = new parser();
	 * 
	 * switch (cmds[0]){
	 * 
	 * case "PQ1a": tokens1 = cmds[1]; tokens2 = tokens1.split(delimt2); if
	 * (tokens2.length < 2){ System.err.printf("Invalid query path"); }
	 * String[][] NNs = new String[tokens2.length][];
	 * 
	 * for (int i = 0; i < tokens2.length; i++){ System.out.println(tokens2[i]);
	 * //decide whether it is a node_lable or node descriptor String res =
	 * parser.discrmN(tokens2[i]); if (res == "descriptor"){ Descriptor desc =
	 * parser.convDesc(tokens2[i]);
	 * 
	 * } else if (res == "label"){ NNs[i][0] = tokens2[i]; } }
	 * 
	 * PathExpressionQuerys pathquery = new PathExpressionQuerys();
	 * pathquery.printResult(pathquery.PQ1a(NNs));
	 * 
	 * break;
	 * 
	 * case "PQ1b": tokens1 = cmds[1]; tokens2 = tokens1.split(delimt2); if
	 * (tokens2.length < 2){ System.err.printf("Invalid query path"); } for (int
	 * i = 0; i < tokens2.length; i++){
	 * 
	 * //decide whether it is a node_lable or node descriptor String res =
	 * parser.discrmN(tokens2[i]); if (res == "descriptor"){ Descriptor desc =
	 * parser.convDesc(tokens2[i]);
	 * 
	 * } else if (res == "label"){
	 * 
	 * }
	 * 
	 * } break;
	 * 
	 * case "PQ1c": tokens1 = cmds[1]; tokens2 = tokens1.split(delimt2); if
	 * (tokens2.length < 2){ System.err.printf("Invalid query path"); } for (int
	 * i = 0; i < tokens2.length; i++){
	 * 
	 * //decide whether it is a node_lable or node descriptor String res =
	 * parser.discrmN(tokens2[i]); if (res == "descriptor"){ Descriptor desc =
	 * parser.convDesc(tokens2[i]);
	 * 
	 * } else if (res == "label"){
	 * 
	 * }
	 * 
	 * } break;
	 * 
	 * case "PQ2a": tokens1 = cmds[1]; tokens2 = tokens1.split(delimt2); if
	 * (tokens2.length < 2){ System.err.printf("Invalid query path"); } res1 =
	 * parser.discrmN(tokens2[0]); if (res1 == "descriptor"){ Descriptor desc =
	 * parser.convDesc(tokens2[0]);
	 * 
	 * } else if (res1 == "label"){
	 * 
	 * 
	 * }
	 * 
	 * for (int i = 1; i < tokens2.length; i++){ String res =
	 * parser.discrmE(tokens2[i]); if (res == "label"){
	 * 
	 * } else if(res == "weight"){
	 * 
	 * }
	 * 
	 * } break;
	 * 
	 * case "PQ2b": tokens1 = cmds[1]; tokens2 = tokens1.split(delimt2); if
	 * (tokens2.length < 2){ System.err.printf("Invalid query path"); } res1 =
	 * parser.discrmN(tokens2[0]); if (res1 == "descriptor"){ Descriptor desc =
	 * parser.convDesc(tokens2[0]);
	 * 
	 * } else if (res1 == "label"){
	 * 
	 * 
	 * }
	 * 
	 * for (int i = 1; i < tokens2.length; i++){ String res =
	 * parser.discrmE(tokens2[i]); if (res == "label"){
	 * 
	 * } else if(res == "weight"){
	 * 
	 * }
	 * 
	 * } break;
	 * 
	 * case "PQ2c": tokens1 = cmds[1]; tokens2 = tokens1.split(delimt2); if
	 * (tokens2.length < 2){ System.err.printf("Invalid query path"); } res1 =
	 * parser.discrmN(tokens2[0]); if (res1 == "descriptor"){ Descriptor desc =
	 * parser.convDesc(tokens2[0]);
	 * 
	 * } else if (res1 == "label"){
	 * 
	 * 
	 * }
	 * 
	 * for (int i = 1; i < tokens2.length; i++){ String res =
	 * parser.discrmE(tokens2[i]); if (res == "label"){
	 * 
	 * } else if(res == "weight"){
	 * 
	 * }
	 * 
	 * } break;
	 * 
	 * case "TQa": tokens1 = cmds[1]; tokens2 = tokens1.split(delimt3); if
	 * (tokens2.length != 3){ System.err.printf("Invalid query path"); } for
	 * (int i = 0; i < tokens2.length; i++){ String res =
	 * parser.discrmE(tokens2[i]); if (res == "label"){
	 * 
	 * } else if (res == "weight"){
	 * 
	 * } } break;
	 * 
	 * case "TQb": tokens1 = cmds[1]; tokens2 = tokens1.split(delimt3); if
	 * (tokens2.length != 3){ System.err.printf("Invalid query path"); } for
	 * (int i = 0; i < tokens2.length; i++){ String res =
	 * parser.discrmE(tokens2[i]); if (res == "label"){
	 * 
	 * } else if (res == "weight"){
	 * 
	 * } } break;
	 * 
	 * case "TQc": tokens1 = cmds[1]; tokens2 = tokens1.split(delimt3); if
	 * (tokens2.length != 3){ System.err.printf("Invalid query path"); } for
	 * (int i = 0; i < tokens2.length; i++){ String res =
	 * parser.discrmE(tokens2[i]); if (res == "label"){
	 * 
	 * } else if (res == "weight"){
	 * 
	 * } } break; default: System.out.printf("Invalid query expression!");
	 * break; }
	 * 
	 * }
	 */
}
