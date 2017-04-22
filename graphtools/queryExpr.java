package graphtools;
import java.lang.Integer;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


import global.*;

public class queryExpr implements GlobalConst{
	
	public void queryExprUI(){
		System.out.printf("Please input the query expression: ");
		Scanner scan = new Scanner(System.in);
		String cmd = null;
		cmd = scan.nextLine();
		
		String delimt1 = "<-";
		String delimt2 = "/";
		String delimt3 = ";";
		String[] cmds;
		String tokens1;
		String[] tokens2;
		String res1;
		cmds = cmd.split(delimt1);
		parser parser = new parser();
		
		switch (cmds[0]){
		
		case "PQ1a":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			List<List<String>> input = new LinkedList();;
			
			for (int i = 0; i < tokens2.length; i++){
				
				//decide whether it is a node_lable or node descriptor
				String res = parser.discrmN(tokens2[i]);
				if (res == "descriptor"){
					Descriptor desc = parser.convDesc(tokens2[i]);
					NLJHelper njlhelper = new NLJHelper();
					String label = njlhelper.nodeDescToLabel(desc);
					List<String> labels =new LinkedList();
					labels.add(label);
					input.add(labels);
				}
				else if (res == "label"){
					List<String> labels =new LinkedList();
					labels.add( tokens2[i]);
					input.add(labels);
				}
			}
			
			PathExpressionQuerys pathquery = new PathExpressionQuerys();
			pathquery.printResult(pathquery.PQ1a(input));
			
			break;
			
		case "PQ1b":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			for (int i = 0; i < tokens2.length; i++){
				
				//decide whether it is a node_lable or node descriptor
				String res = parser.discrmN(tokens2[i]);
				if (res == "descriptor"){
					Descriptor desc = parser.convDesc(tokens2[i]);
					
				}
				else if (res == "label"){
					
				}
				
			}
			break;
			
		case "PQ1c":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			for (int i = 0; i < tokens2.length; i++){
				
				//decide whether it is a node_lable or node descriptor
				String res = parser.discrmN(tokens2[i]);
				if (res == "descriptor"){
					Descriptor desc = parser.convDesc(tokens2[i]);
					
				}
				else if (res == "label"){
					
				}
				
			}
			break;
			
		case "PQ2a":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			res1 = parser.discrmN(tokens2[0]);
			if (res1 == "descriptor"){
				Descriptor desc = parser.convDesc(tokens2[0]);	
				
			}
			else if (res1 == "label"){
				
				
			}
			
			for (int i = 1; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if(res == "weight"){
					
				}
				
			}
			break;
			
		case "PQ2b":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			res1 = parser.discrmN(tokens2[0]);
			if (res1 == "descriptor"){
				Descriptor desc = parser.convDesc(tokens2[0]);	
				
			}
			else if (res1 == "label"){
				
				
			}
			
			for (int i = 1; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if(res == "weight"){
					
				}
				
			}
			break;
			
		case "PQ2c":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			res1 = parser.discrmN(tokens2[0]);
			if (res1 == "descriptor"){
				Descriptor desc = parser.convDesc(tokens2[0]);	
				
			}
			else if (res1 == "label"){
				
				
			}
			
			for (int i = 1; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if(res == "weight"){
					
				}
				
			}
			break;
			
		case "TQa":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt3);
			if (tokens2.length != 3){
				System.err.printf("Invalid query path");
			}
			for (int i = 0; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if (res == "weight"){
					
				}
			}
			break;
			
		case "TQb":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt3);
			if (tokens2.length != 3){
				System.err.printf("Invalid query path");
			}
			for (int i = 0; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if (res == "weight"){
					
				}
			}
			break;
			
		case "TQc":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt3);
			if (tokens2.length != 3){
				System.err.printf("Invalid query path");
			}
			for (int i = 0; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if (res == "weight"){
					
				}
			}
			break;	
		default:
			System.out.printf("Invalid query expression!");
			break;
		}
	}
	
	public static void main(String[] argvs){
		
		System.out.printf("Please input the query expression: ");
		Scanner scan = new Scanner(System.in);
		String cmd = null;
		cmd = scan.nextLine();
		
		String delimt1 = "<-";
		String delimt2 = "/";
		String delimt3 = ";";
		String[] cmds;
		String tokens1;
		String[] tokens2;
		String res1;
		cmds = cmd.split(delimt1);
		parser parser = new parser();
		
		switch (cmds[0]){
		
		case "PQ1a":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			String[][] NNs = new String[tokens2.length][];
			
			for (int i = 0; i < tokens2.length; i++){
				
				//decide whether it is a node_lable or node descriptor
				String res = parser.discrmN(tokens2[i]);
				if (res == "descriptor"){
					Descriptor desc = parser.convDesc(tokens2[i]);
					NLJHelper njlhelper = new NLJHelper();
					String labels = njlhelper.nodeDescToLabel(desc);
					NNs[i][0] = labels;
				}
				else if (res == "label"){
					NNs[i][0] = tokens2[i];
				}
			}
			
			PathExpressionQuerys pathquery = new PathExpressionQuerys();
			pathquery.printResult(pathquery.PQ1a(NNs));
			
			break;
			
		case "PQ1b":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			for (int i = 0; i < tokens2.length; i++){
				
				//decide whether it is a node_lable or node descriptor
				String res = parser.discrmN(tokens2[i]);
				if (res == "descriptor"){
					Descriptor desc = parser.convDesc(tokens2[i]);
					
				}
				else if (res == "label"){
					
				}
				
			}
			break;
			
		case "PQ1c":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			for (int i = 0; i < tokens2.length; i++){
				
				//decide whether it is a node_lable or node descriptor
				String res = parser.discrmN(tokens2[i]);
				if (res == "descriptor"){
					Descriptor desc = parser.convDesc(tokens2[i]);
					
				}
				else if (res == "label"){
					
				}
				
			}
			break;
			
		case "PQ2a":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			res1 = parser.discrmN(tokens2[0]);
			if (res1 == "descriptor"){
				Descriptor desc = parser.convDesc(tokens2[0]);	
				
			}
			else if (res1 == "label"){
				
				
			}
			
			for (int i = 1; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if(res == "weight"){
					
				}
				
			}
			break;
			
		case "PQ2b":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			res1 = parser.discrmN(tokens2[0]);
			if (res1 == "descriptor"){
				Descriptor desc = parser.convDesc(tokens2[0]);	
				
			}
			else if (res1 == "label"){
				
				
			}
			
			for (int i = 1; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if(res == "weight"){
					
				}
				
			}
			break;
			
		case "PQ2c":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt2);
			if (tokens2.length < 2){
				System.err.printf("Invalid query path");
			}
			res1 = parser.discrmN(tokens2[0]);
			if (res1 == "descriptor"){
				Descriptor desc = parser.convDesc(tokens2[0]);	
				
			}
			else if (res1 == "label"){
				
				
			}
			
			for (int i = 1; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if(res == "weight"){
					
				}
				
			}
			break;
			
		case "TQa":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt3);
			if (tokens2.length != 3){
				System.err.printf("Invalid query path");
			}
			for (int i = 0; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if (res == "weight"){
					
				}
			}
			break;
			
		case "TQb":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt3);
			if (tokens2.length != 3){
				System.err.printf("Invalid query path");
			}
			for (int i = 0; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if (res == "weight"){
					
				}
			}
			break;
			
		case "TQc":
			tokens1 = cmds[1];
			tokens2 = tokens1.split(delimt3);
			if (tokens2.length != 3){
				System.err.printf("Invalid query path");
			}
			for (int i = 0; i < tokens2.length; i++){
				String res = parser.discrmE(tokens2[i]);
				if (res == "label"){
					
				}
				else if (res == "weight"){
					
				}
			}
			break;	
		default:
			System.out.printf("Invalid query expression!");
			break;
		}
	
	}

}
