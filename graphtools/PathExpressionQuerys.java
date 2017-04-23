package graphtools;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import btree.*;
import diskmgr.DB;
import global.GlobalConst;
import global.RID;

public class PathExpressionQuerys implements GlobalConst {

	/* NN/NN/NN* query */
	private List<Path> PQ1(List<List<Path>> nodelabellist) {
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

	public List<Path> PQ1a(List<List<String>> NNs) {
		List<List<Path>> nodelabellist = buildlistdoublestr(NNs);
		return PQ1(nodelabellist);
	}

	public List<Path> PQ1b(List<List<String>> NNs) {
		List<Path> result = PQ1a(NNs);
		Collections.sort(result, new DoubleStringSort());
		return result;
	}

	public List<Path> PQ1c(List<List<String>> NNs) {
		List<Path> ls = PQ1a(NNs);
		List<Path> result = new LinkedList<Path>(new LinkedHashSet<Path>(ls));
		return result;
	}

	public List<Path> PQ1a(String[][] NNs) {
		List<List<Path>> nodelabellist = buildlistdoublestr(NNs);
		return PQ1(nodelabellist);
	}

	public List<Path> PQ1b(String[][] NNs) {
		List<Path> result = PQ1a(NNs);
		Collections.sort(result, new DoubleStringSort());
		return result;
	}

	public List<Path> PQ1c(String[][] NNs) {
		List<Path> ls = PQ1a(NNs);
		List<Path> result = new LinkedList<Path>(new LinkedHashSet<Path>(ls));
		return result;
	}

	/* NN/EN(/EN)* */
	public List<Path> PQ2(List<List<Path>> nodelabellist, List edgecondition) {
		NLJHelper njlhelper = new NLJHelper();
		for (int i = 0; i < edgecondition.size(); i++) {
			List<Path> result = new LinkedList();
			if (edgecondition.get(i) instanceof String) {
				result = njlhelper.nodeToAllwithEdgeLabel(nodelabellist.get(i), (String) edgecondition.get(i));
			} else if (edgecondition.get(i) instanceof Integer) {
				result = njlhelper.nodeToAllwithEdgeWeight(nodelabellist.get(i), (int) edgecondition.get(i));
			} else {
				System.err.println("Wrong edgecondition type!");
			}
			if (result != null) {
				nodelabellist.add(i + 1, result);
			}
		}
		List<Path> result = new LinkedList();
		for (Path sa : nodelabellist.get(nodelabellist.size() - 1)) {
			result.add(new Path(sa.head, sa.tail));
		}
		return result;
	}

	public List<Path> PQ2a(List<String> NN, List edgecondition) {
		List<List<Path>> nodelabellist = buildlistdoublestr_singlelist(NN);
		return PQ2(nodelabellist, edgecondition);
	}

	public List<Path> PQ2b(List<String> NN, List edgecondition) {
		List<Path> result = PQ2a(NN, edgecondition);
		Collections.sort(result, new DoubleStringSort());
		return result;
	}

	public List<Path> PQ2c(List<String> NN, List edgecondition) {
		List<Path> ls = PQ2a(NN, edgecondition);
		List<Path> result = new LinkedList<Path>(new LinkedHashSet<Path>(ls));
		return result;
	}

	public List<Path> PQ2a(String[] NN, List edgecondition) {
		List<List<Path>> nodelabellist = buildlistdoublestr(new String[][] { NN, });
		return PQ2(nodelabellist, edgecondition);
	}

	public List<Path> PQ2b(String[] NN, List edgecondition) {
		List<Path> result = PQ2a(NN, edgecondition);
		Collections.sort(result, new DoubleStringSort());
		return result;
	}

	public List<Path> PQ2c(String[] NN, List edgecondition) {
		List<Path> ls = PQ2a(NN, edgecondition);
		List<Path> result = new LinkedList<Path>(new LinkedHashSet<Path>(ls));
		return result;
	}

	/* NN//Bound_max_num_edge */
	public List<Path> PQ3_max_num_edge(List<List<Path>> nodelabellist, int max_num_edge) {
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
		return result;
	}

	/* NN//Bound_max_weight */
	public List<Path> PQ3_max_weight(List<List<Path>> nodelabellist, int max_weight) {
		NLJHelper njlhelper = new NLJHelper();
		boolean done = false;
		for (int i = 0; !done; i++) {
			List<Path> result = njlhelper.nodeToAllwithMaxWeight(nodelabellist.get(i), max_weight);
			if (result.size() == 0) {
				done = true;
			} else {
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
		// printResult(result);
		return result;
	}

	public List<Path> PQ3a_max_num_edge(List<String> NN, int max_num_edge) {
		List<List<Path>> nodelabellist = buildlistdoublestr_singlelist(NN);

		return PQ3_max_num_edge(nodelabellist, max_num_edge);
	}

	public List<Path> PQ3a_max_weight(List<String> NN, int max_weight) {
		List<List<Path>> nodelabellist = buildlistdoublestr_singlelist(NN);
		return PQ3_max_weight(nodelabellist, max_weight);
	}

	public List<Path> PQ3b_max_num_edge(List<String> NNs, int max_num_edge) {
		List<Path> result = PQ3a_max_num_edge(NNs, max_num_edge);
		Collections.sort(result, new DoubleStringSort());
		return result;
	}

	public List<Path> PQ3b_max_weight(List<String> NNs, int max_weight) {
		List<Path> result = PQ3a_max_weight(NNs, max_weight);
		Collections.sort(result, new DoubleStringSort());
		return result;
	}

	public List<Path> PQ3c_max_num_edge(List<String> NNs, int max_num_edge) {
		List<Path> ls = PQ3a_max_num_edge(NNs, max_num_edge);
		List<Path> result = new LinkedList<Path>(new LinkedHashSet<Path>(ls));
		return result;
	}

	public List<Path> PQ3c_max_weight(List<String> NNs, int max_weight) {
		List<Path> ls = PQ3a_max_weight(NNs, max_weight);
		List<Path> result = new LinkedList<Path>(new LinkedHashSet<Path>(ls));
		return result;
	}

	public List<Path> PQ3a_max_num_edge(String[] NNs, int max_num_edge) {
		List<List<Path>> nodelabellist = buildlistdoublestr(new String[][] { NNs, });

		return PQ3_max_num_edge(nodelabellist, max_num_edge);
	}

	public List<Path> PQ3a_max_weight(String[] NNs, int max_weight) {
		List<List<Path>> nodelabellist = buildlistdoublestr(new String[][] { NNs, });
		return PQ3_max_weight(nodelabellist, max_weight);
	}

	public List<Path> PQ3b_max_num_edge(String[] NNs, int max_num_edge) {
		List<Path> result = PQ3a_max_num_edge(NNs, max_num_edge);
		Collections.sort(result, new DoubleStringSort());
		return result;
	}

	public List<Path> PQ3b_max_weight(String[] NNs, int max_weight) {
		List<Path> result = PQ3a_max_weight(NNs, max_weight);
		Collections.sort(result, new DoubleStringSort());
		return result;
	}

	public List<Path> PQ3c_max_num_edge(String[] NNs, int max_num_edge) {
		List<Path> ls = PQ3a_max_num_edge(NNs, max_num_edge);
		List<Path> result = new LinkedList<Path>(new LinkedHashSet<Path>(ls));
		return result;
	}

	public List<Path> PQ3c_max_weight(String[] NNs, int max_weight) {
		List<Path> ls = PQ3a_max_weight(NNs, max_weight);
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

	private List<List<Path>> buildlistdoublestr(List<List<String>> NNs) {
		List<List<Path>> list = new LinkedList();
		for (int i = 0; i < NNs.size(); i++) {
			List<Path> NN = new LinkedList();
			NN.clear();
			for (int j = 0; j < NNs.get(i).size(); j++)
				NN.add(new Path(NNs.get(i).get(j), NNs.get(i).get(j)));
			list.add(NN);
		}
		return list;
	}

	private List<List<Path>> buildlistdoublestr_singlelist(List<String> NN) {
		List<List<Path>> list = new LinkedList();
		List<Path> NNp = new LinkedList();
		NNp.clear();
		for (int i = 0; i < NN.size(); i++)
			NNp.add(new Path(NN.get(i), NN.get(i)));
		list.add(NNp);
		return list;
	}

	public void printResult(List<Path> result) {
		if (result.isEmpty()) {
			System.out.println("No valid result!");
		}
		for (Path r : result) {
			System.out.println("head:" + r.head + " tail:" + r.tail);
		}
	}

	public List<RID> getNIDFromResult(List<Path> result) {
		List<RID> ridlist = new LinkedList();
		RID rid = new RID();
		KeyClass key;
		for (Path r : result) {
			key = new StringKey(r.tail);
			try {
				BTFileScan s = db.btmgr.getNodelabelbtree().new_scan(key, key);
				rid = ((LeafData) s.get_next().data).getData();
				ridlist.add(rid);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		return ridlist;
	}
	
	public void printRids(List<RID> ridlist){
		for(RID rid:ridlist){
			System.out.println(rid.toString());
		}
	}

	public void printResultwithWeight(List<Path> result) {
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
