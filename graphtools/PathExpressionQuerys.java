package graphtools;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PathExpressionQuerys {

	public List<String[]> PQ1(String[][] NNs) {
		List<List<String[]>> nodelabellist = buildlistdoublestr(NNs);
		NLJHelper njlhelper = new NLJHelper();
		for (int i = 0; i < nodelabellist.size() - 1; i++) {
			List<String[]> result = njlhelper.nodeToNode(nodelabellist.get(i), nodelabellist.get(i + 1));
			if (result != null) {
				nodelabellist.remove(i + 1);
				nodelabellist.add(i + 1, result);
			}
		}
		List<String[]> result = new LinkedList();
		for (String[] sa : nodelabellist.get(nodelabellist.size() - 1)) {
			result.add(new String[] { sa[0], sa[1] });
		}
		return result;
	}

	public List<String[]> PQ1b(String[][] NNs) {
		List<String[]> result = PQ1(NNs);
		Collections.sort(result, new DoubleStringSort());
		// printResult(result);
		return result;
	}

	public List<String[]> PQ1c(String[][] NNs) {
		List<String[]> ls = PQ1(NNs);
		List<String[]> result = new LinkedList();
		result.add(new String[] { ls.get(0)[0], ls.get(0)[1] });
		for (String[] l : ls) {
			boolean distinct = true;
			for (String[] r : result) {
				if (l[0].equals(r[0]) && l[1].equals(r[1])) {
					distinct = false;
					break;
				}
			}
			if (distinct)
				result.add(new String[] { l[0], l[1] });
		}
		// printResult(result);
		return result;
	}

	public List<String[]> PQ3_max_num_edge(String[] NNs, int max_num_edge) {
		String[][] extendNNs = { NNs, };
		List<List<String[]>> nodelabellist = buildlistdoublestr(extendNNs);
		NLJHelper njlhelper = new NLJHelper();
		for (int i = 0; i < max_num_edge; i++) {
			List<String[]> result = njlhelper.nodeToAll(nodelabellist.get(i));
			if (result != null) {
				nodelabellist.add(i + 1, result);
			}
		}
		List<String[]> result = new LinkedList();
		boolean firstline = true;
		for (List<String[]> sas : nodelabellist) {
			if (firstline)
				firstline = false;
			else {
				for (String[] sa : sas) {
					result.add(new String[] { sa[0], sa[1] });
				}
			}
		}
//		printResult(result);
		return result;
	}
	
	public List<String[]> PQ3b_max_num_edge(String[] NNs, int max_num_edge) {
		List<String[]> result = PQ3_max_num_edge(NNs, max_num_edge);
		Collections.sort(result, new DoubleStringSort());
//		 printResult(result);
		return result;
	}
	
	public List<String[]> PQ3c_max_num_edge(String[] NNs, int max_num_edge) {
		List<String[]> ls = PQ3_max_num_edge(NNs,max_num_edge);
		List<String[]> result = new LinkedList();
		result.add(new String[] { ls.get(0)[0], ls.get(0)[1] });
		for (String[] l : ls) {
			boolean distinct = true;
			for (String[] r : result) {
				if (l[0].equals(r[0]) && l[1].equals(r[1])) {
					distinct = false;
					break;
				}
			}
			if (distinct)
				result.add(new String[] { l[0], l[1] });
		}
//		 printResult(result);
		return result;
	}

	public List<List<String[]>> buildlistdoublestr(String[][] NNs) {
		List<List<String[]>> list = new LinkedList();
		for (int i = 0; i < NNs.length; i++) {
			List<String[]> NN = new LinkedList();
			NN.clear();
			for (int j = 0; j < NNs[i].length; j++)
				NN.add(new String[] { NNs[i][j], NNs[i][j] });
			list.add(NN);
		}
		return list;
	}

	public void printResult(List<String[]> result) {
		for (String[] r : result) {
			System.out.println("head:" + r[0] + " tail:" + r[1]);
		}
	}
	// public void printLastList(List<List<String[]>> thislist){
	// for(String[] sa : thislist.get(thislist.size()-1)){
	// System.out.println("head:"+sa[0]+" tail:"+sa[1]);
	// }
	// }

	// public List<List<String>> buildlist(String[][] NNs){
	// List<List<String>> list = new LinkedList();
	// for(int i =0; i<NNs.length;i++){
	// List<String> NN = new LinkedList();
	// NN.clear();
	// for(int j=0;j<NNs[i].length;j++)
	// NN.add(NNs[i][j]);
	// list.add(NN);
	// }
	// return list;
	// }

	// public void printList(List<List<String>> thislist){
	// for(List<String> l : thislist){
	// for(String s : l)
	// System.out.print(s+" ");
	// System.out.println();
	// }
	//// for(String result :thislist.get(thislist.size()-1))
	//// System.out.println(result);
	// }

	// public void PQ1Judgeold(List<List<String>> nodelabellist){
	//
	// NLJHelper njlhelper = new NLJHelper();
	// for(int i=0;i<nodelabellist.size()-1;i++){
	// List<String> result = njlhelper.nodeToNodeold(nodelabellist.get(i),
	// nodelabellist.get(i+1));
	// if (result !=null){
	// nodelabellist.remove(i+1);
	// nodelabellist.add(i+1, result);
	// }
	// }
	// printList(nodelabellist);
	//
	// }

}

class DoubleStringSort implements Comparator {

	@Override
	public int compare(Object re0, Object re1) {
		// TODO Auto-generated method stub
		String[] first = (String[]) re0;
		String[] second = (String[]) re1;
		if (first[0].compareTo(second[0]) != 0) {
			return first[0].compareTo(second[0]);
		} else {
			return first[1].compareTo(second[1]);
		}
	}

}
