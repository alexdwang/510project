package graphtools;

import btree.BTFileScan;
import btree.KeyDataEntry;
import btree.LeafData;
import edgeheap.Edge;
import global.*;
import heap.*;
import iterator.*;
import nodeheap.Node;
import zindex.DescriptorKey;
import zindex.ZEncoder;
import zindex.ZTreeFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

class NLJHelper implements GlobalConst {

	private static final int PROJ_NODE_LABEL = 1;
	private static final int PROJ_EDGE_ID = 2;
	private static final int PROJ_EDGE_WEIGHT = 3;
	private static final int PROJ_EDGE_Laebl = 4;
	private static final int PROJ_EDGE_DST_Laebl = 5;

	public List<Path> nodeToAllwithEdgeWeight(List<Path> sourcelabel, int maxweight) {
		List<Path> result = new LinkedList();
		for (Path source : sourcelabel) {
			IndexNLJ_EdgeSourceNode edges = edgeSourceNodeJoin(source.tail);
			Tuple t = null;
			try {
				while ((t = edges.get_next()) != null) {
					// System.out.println(t.getIntFld(2));
					int weight = t.getIntFld(NLJHelper.PROJ_EDGE_WEIGHT);
					if (weight <= maxweight) {
						result.add(new Path(source.head, t.getStrFld(NLJHelper.PROJ_EDGE_DST_Laebl),
								weight + source.totalWeight));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
		}
		return result;

	}

	public List<Path> nodeToAllwithEdgeLabel(List<Path> sourcelabel, String edgelabel) {
		List<Path> result = new LinkedList();
		for (Path source : sourcelabel) {
			IndexNLJ_EdgeSourceNode edges = edgeSourceNodeJoin(source.tail);
			Tuple t = null;
			try {
				while ((t = edges.get_next()) != null) {
					// System.out.println(t.getIntFld(2));
					String label = t.getStrFld(NLJHelper.PROJ_EDGE_Laebl);
					if (label.equals(edgelabel)) {
						result.add(new Path(source.head, t.getStrFld(NLJHelper.PROJ_EDGE_DST_Laebl),
								t.getIntFld(NLJHelper.PROJ_EDGE_WEIGHT) + source.totalWeight));

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
		}
		return result;

	}

	public List<Path> nodeToAllwithMaxWeight(List<Path> sourcelabel, int maxweight) {
		List<Path> result = new LinkedList();
		for (Path source : sourcelabel) {
			IndexNLJ_EdgeSourceNode edges = edgeSourceNodeJoin(source.tail);
			Tuple t = null;
			try {
				while ((t = edges.get_next()) != null) {
					// System.out.println(t.getIntFld(2));
					int weight = t.getIntFld(NLJHelper.PROJ_EDGE_WEIGHT) + source.totalWeight;
					if (weight <= maxweight) {
						result.add(new Path(source.head, t.getStrFld(NLJHelper.PROJ_EDGE_DST_Laebl),
								t.getIntFld(NLJHelper.PROJ_EDGE_WEIGHT) + source.totalWeight));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
		}
		return result;

	}

	public List<Path> nodeToAll(List<Path> sourcelabel) {
		List<Path> result = new LinkedList();
		for (Path source : sourcelabel) {
			IndexNLJ_EdgeSourceNode edges = edgeSourceNodeJoin(source.tail);
			Tuple t = null;
			try {
				while ((t = edges.get_next()) != null) {
					// System.out.println(t.getIntFld(2));
					result.add(new Path(source.head, t.getStrFld(NLJHelper.PROJ_EDGE_DST_Laebl)));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
		}
		return result;
	}

	public List<Path> nodeToNode(List<Path> sourcelabel, List<Path> destlabel) {
		List<Path> result = new LinkedList();
		for (Path source : sourcelabel) {
			IndexNLJ_EdgeSourceNode edges = edgeSourceNodeJoin(source.tail);
			Tuple t = null;
			try {
				while ((t = edges.get_next()) != null) {
					for (Path dest : destlabel) {
						if (t.getStrFld(NLJHelper.PROJ_EDGE_DST_Laebl).equals(dest.tail))
							result.add(new Path(source.head, t.getStrFld(NLJHelper.PROJ_EDGE_DST_Laebl)));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
		}
		return result;
	}

	public IndexNLJ_EdgeDestNode edgeDestNodeJoin(String nodeLabelFilter) {
		CondExpr cond = null;
		if (nodeLabelFilter != null) {
			cond = new CondExpr();
			cond.next = null;
			cond.op = new AttrOperator(AttrOperator.aopEQ);
			cond.type1 = new AttrType(AttrType.attrSymbol);
			cond.type2 = new AttrType(AttrType.attrString);
			cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label);
			cond.operand2.string = nodeLabelFilter;
		}

		IndexNLJ_EdgeDestNode nlj = null;
		try {
			nlj = new IndexNLJ_EdgeDestNode(cond);
			return nlj;
		} catch (Exception e) {
			System.out.println("error:" + e);
		}
		return null;
	}

	public IndexNLJ_EdgeSourceNode edgeSourceNodeJoin(String nodeLabelFilter) {
		CondExpr cond = null;
		if (nodeLabelFilter != null) {
			cond = new CondExpr();
			cond.next = null;
			cond.op = new AttrOperator(AttrOperator.aopEQ);
			cond.type1 = new AttrType(AttrType.attrSymbol);
			cond.type2 = new AttrType(AttrType.attrString);
			cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label);
			cond.operand2.string = nodeLabelFilter;
		}

		IndexNLJ_EdgeSourceNode nlj = null;
		try {
			nlj = new IndexNLJ_EdgeSourceNode(cond);
			return nlj;
		} catch (Exception e) {
			System.out.println("error:" + e);
		}
		return null;
	}

	public IndexNLJ_NodeSourceEdge nodeSourceEdgeJoin(String edgeLabelFilter) {
		CondExpr cond = null;
		if (edgeLabelFilter != null) {
			cond = new CondExpr();
			cond.next = null;
			cond.op = new AttrOperator(AttrOperator.aopEQ);
			cond.type1 = new AttrType(AttrType.attrSymbol);
			cond.type2 = new AttrType(AttrType.attrString);
			cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_LABEL);
			cond.operand2.string = edgeLabelFilter;
		}

		IndexNLJ_NodeSourceEdge nlj = null;
		try {
			nlj = new IndexNLJ_NodeSourceEdge(cond);
			return nlj;
		} catch (Exception e) {
			System.out.println("error:" + e);
		}
		return null;
	}

	public IndexNLJ_NodeSourceEdge nodeSourceEdgeJoin(int edgeWeightFilter) {
		CondExpr cond = new CondExpr();
		cond.next = null;
		cond.op = new AttrOperator(AttrOperator.aopLE);
		cond.type1 = new AttrType(AttrType.attrSymbol);
		cond.type2 = new AttrType(AttrType.attrInteger);
		cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_WGT);
		cond.operand2.integer = edgeWeightFilter;

		IndexNLJ_NodeSourceEdge nlj = null;
		try {
			nlj = new IndexNLJ_NodeSourceEdge(cond);
			return nlj;
		} catch (Exception e) {
			System.out.println("error:" + e);
		}
		return null;
	}

	public IndexNLJ_NodeDestEdge nodeDestEdgeJoin(String edgeLabelFilter) {
		CondExpr cond = null;
		if (edgeLabelFilter != null) {
			cond = new CondExpr();
			cond.next = null;
			cond.op = new AttrOperator(AttrOperator.aopEQ);
			cond.type1 = new AttrType(AttrType.attrSymbol);
			cond.type2 = new AttrType(AttrType.attrString);
			cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_LABEL);
			cond.operand2.string = edgeLabelFilter;
		}

		IndexNLJ_NodeDestEdge nlj = null;
		try {
			nlj = new IndexNLJ_NodeDestEdge(cond);
			return nlj;
		} catch (Exception e) {
			System.out.println("error:" + e);
		}
		return null;
	}

	public IndexNLJ_NodeDestEdge nodeDestEdgeJoin(int edgeWeightFilter) {
		CondExpr cond = new CondExpr();
		cond.next = null;
		cond.op = new AttrOperator(AttrOperator.aopLE);
		cond.type1 = new AttrType(AttrType.attrSymbol);
		cond.type2 = new AttrType(AttrType.attrInteger);
		cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_WGT);
		cond.operand2.integer = edgeWeightFilter;

		IndexNLJ_NodeDestEdge nlj = null;
		try {
			nlj = new IndexNLJ_NodeDestEdge(cond);
			return nlj;
		} catch (Exception e) {
			System.out.println("error:" + e);
		}
		return null;
	}

	public IndexNLJ_NodeDestEdge nodeDestJoinByEdgeId(int edgeIdFilter) {
		CondExpr cond = new CondExpr();
		cond.next = null;
		cond.op = new AttrOperator(AttrOperator.aopEQ);
		cond.type1 = new AttrType(AttrType.attrSymbol);
		cond.type2 = new AttrType(AttrType.attrInteger);
		cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_ID);
		cond.operand2.integer = edgeIdFilter;

		IndexNLJ_NodeDestEdge nlj = null;
		try {
			nlj = new IndexNLJ_NodeDestEdge(cond);
			return nlj;
		} catch (Exception e) {
			System.out.println("error:" + e);
		}
		return null;
	}


	public List<String> nodeDescToLabel(Descriptor desc) throws IOException, FieldNumberOutOfBoundException {
		List<String> ret = new LinkedList<>();

		db.hfmgr.initScanNode();
		Node node = null;
		while ((node = db.hfmgr.scanNextNode()) != null) {
			if (node.getDesc().equals(desc))
				ret.add(node.getLabel());
		}
		db.hfmgr.closeScan();

		return ret;
	}
}
