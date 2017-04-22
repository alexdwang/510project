package graphtools;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class PathExpressionQuerys {

	public List<Path> PQ1a(String[][] NNs) {
		List<List<Path>> nodelabellist = buildlistdoublestr(NNs);
		NLJHelper njlhelper = new NLJHelper();
		for (int i = 0; i < nodelabellist.size() - 1; i++) {
			List<Path> result = njlhelper.nodeToNode(nodelabellist.get(i), nodelabellist.get(i + 1));
			if (result != null) {
				nodelabellist.remove(i + 1);
				nodelabellist.add(i + 1, result);
			}
		}
		List<Path> result = new LinkedList();
		for (Path sa : nodelabellist.get(nodelabellist.size() - 1)) {
			result.add(new Path(sa.head, sa.tail));
		}
		return result;
	}

	public List<Path> PQ1b(String[][] NNs) {
		List<Path> result = PQ1a(NNs);
		Collections.sort(result, new DoubleStringSort());
		// printResult(result);
		return result;
	}

	public List<Path> PQ1c(String[][] NNs) {
		List<Path> ls = PQ1a(NNs);
		List<Path> result = new LinkedList<Path>(new LinkedHashSet<Path>(ls));
		return result;
	}

	public List<Path> PQ3a_max_num_edge(String[] NNs, int max_num_edge) {
		List<List<Path>> nodelabellist = buildlistdoublestr(new String[][] { NNs, });
		NLJHelper njlhelper = new NLJHelper();
		for (int i = 0; i < max_num_edge; i++) {
			List<Path> result = njlhelper.nodeToAll(nodelabellist.get(i));
			if (result != null) {
				nodelabellist.add(i + 1, result);
			}
		}
		List<Path> result = new LinkedList();
		boolean firstline = true;
		for (List<Path> sas : nodelabellist) {
			if (firstline)
				firstline = false;
			else {
				for (Path sa : sas) {
					result.add(new Path(sa.head, sa.tail));
				}
			}
		}
//		printResult(result);
		return result;
	}
	
	public List<Path> PQ3a_max_weight(String[] NNs, int max_weight){
		List<List<Path>> nodelabellist = buildlistdoublestr(new String[][] { NNs, });
		NLJHelper njlhelper = new NLJHelper();
		boolean done=false;
		for(int i=0;!done;i++){
			List<Path> result = njlhelper.nodeToAllwithMaxWeight(nodelabellist.get(i),max_weight);
			if(result.size()==0){
				done=true;
			}else {
				nodelabellist.add(i + 1, result);
			}
			
		}
		List<Path> result = new LinkedList();
		boolean firstline = true;
		for (List<Path> sas : nodelabellist) {
			if (firstline)
				firstline = false;
			else {
				for (Path sa : sas) {
					result.add(new Path(sa.head, sa.tail, sa.totalWeight));
				}
			}
		}
//		printResult(result);
		return result;
	}
	
	public List<Path> PQ3b_max_num_edge(String[] NNs, int max_num_edge) {
		List<Path> result = PQ3a_max_num_edge(NNs, max_num_edge);
		Collections.sort(result, new DoubleStringSort());
//		 printResult(result);
		return result;
	}
	
	public List<Path> PQ3b_max_weight(String[] NNs, int max_weight) {
		List<Path> result = PQ3a_max_weight(NNs, max_weight);
		Collections.sort(result, new DoubleStringSort());
//		 printResult(result);
		return result;
	}
	
	public List<Path> PQ3c_max_num_edge(String[] NNs, int max_num_edge) {
		List<Path> ls = PQ3a_max_num_edge(NNs,max_num_edge);
		List<Path> result = new LinkedList<Path>(new LinkedHashSet<Path>(ls));
		return result;
	}
	
	public List<Path> PQ3c_max_weight(String[] NNs, int max_weight) {
		List<Path> ls = PQ3a_max_weight(NNs,max_weight);
		List<Path> result = new LinkedList<Path>(new LinkedHashSet<Path>(ls));
		return result;
	}

	private List<List<Path>> buildlistdoublestr(String[][] NNs) {
		List<List<Path>> list = new LinkedList();
		for (int i = 0; i < NNs.length; i++) {
			List<Path> NN = new LinkedList();
			NN.clear();
			for (int j = 0; j < NNs[i].length; j++)
				NN.add(new Path(NNs[i][j], NNs[i][j]));
			list.add(NN);
		}
		return list;
	}

	public void printResult(List<Path> result) {
		for (Path r : result) {
			System.out.println("head:" + r.head + " tail:" + r.tail);
		}
	}
	
	public void printResultwithWeight(List<Path> result){
		for (Path r : result) {
			System.out.println("head:" + r.head + " tail:" + r.tail + " totalweight:" + r.totalWeight);
		}
	}

}

class DoubleStringSort implements Comparator {

	@Override
	public int compare(Object re0, Object re1) {
		// TODO Auto-generated method stub
		Path first = (Path) re0;
		Path second = (Path) re1;
		if (first.head.compareTo(second.head) != 0) {
			return first.head.compareTo(second.head);
		} else {
			return first.tail.compareTo(second.tail);
		}
	}

}
