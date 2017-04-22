package graphtools;

import btree.BTFileScan;
import btree.KeyDataEntry;
import btree.LeafData;
import btree.ScanIteratorException;
import edgeheap.Edge;
import global.*;
import heap.Heapfile;
import heap.Tuple;
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

	public List<Path> nodeToAllwithMaxWeight(List<Path> sourcelabel, int maxweight) {
		List<Path> result = new LinkedList();
		for (Path source : sourcelabel) {
			IndexNLJ_EdgeSourceNode edges = edgeSourceNodeJoin(source.tail);
			Tuple t = null;
			try {
				while ((t = edges.get_next()) != null) {
					// System.out.println(t.getIntFld(2));
					int weight=t.getIntFld(NLJHelper.PROJ_EDGE_WEIGHT)+source.totalWeight;
					if (weight<= maxweight) {
						IndexNLJ_NodeDestEdge findnodes = nodeDestJoinByEdgeId(t.getIntFld(NLJHelper.PROJ_EDGE_ID));
						Tuple n = null;
						while ((n = findnodes.get_next()) != null) {
							result.add(new Path(source.head, n.getStrFld(NLJHelper.PROJ_NODE_LABEL), weight));
						}
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
					IndexNLJ_NodeDestEdge findnodes = nodeDestJoinByEdgeId(t.getIntFld(NLJHelper.PROJ_EDGE_ID));
					Tuple n = null;
					while ((n = findnodes.get_next()) != null) {
						result.add(new Path(source.head, n.getStrFld(NLJHelper.PROJ_NODE_LABEL)));
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

	public List<Path> nodeToNode(List<Path> sourcelabel, List<Path> destlabel) {
		List<Path> result = new LinkedList();
		for (Path source : sourcelabel) {
			IndexNLJ_EdgeSourceNode edges = edgeSourceNodeJoin(source.tail);
			Tuple t = null;
			try {
				while ((t = edges.get_next()) != null) {
					// System.out.println(t.getIntFld(2));
					IndexNLJ_NodeDestEdge findnodes = nodeDestJoinByEdgeId(t.getIntFld(NLJHelper.PROJ_EDGE_ID));
					Tuple n = null;
					while ((n = findnodes.get_next()) != null) {
						for (Path dest : destlabel) {
							if (dest.tail.equals(n.getStrFld(NLJHelper.PROJ_NODE_LABEL)))
								result.add(new Path(source.head, dest.tail));
						}
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
			cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), Node.FldID_Label);
			cond.operand2.string = nodeLabelFilter;
		}

		FldSpec[] proj1 = { new FldSpec(new RelSpec(RelSpec.innerRel), Node.FldID_Label),
				new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_ID) };
		IndexNLJ_EdgeDestNode nlj = null;
		try {
			nlj = new IndexNLJ_EdgeDestNode(500, cond, proj1, 2);
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
			cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), Node.FldID_Label);
			cond.operand2.string = nodeLabelFilter;
		}

		FldSpec[] proj1 = { new FldSpec(new RelSpec(RelSpec.innerRel), Node.FldID_Label),
				new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_ID),
				new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_WGT) };
		IndexNLJ_EdgeSourceNode nlj = null;
		try {
			nlj = new IndexNLJ_EdgeSourceNode(500, cond, proj1, 3);
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
			cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_LABEL);
			cond.operand2.string = edgeLabelFilter;
		}

		FldSpec[] proj1 = { new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label),
				new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_ID) };
		IndexNLJ_NodeSourceEdge nlj = null;
		try {
			nlj = new IndexNLJ_NodeSourceEdge(500, cond, proj1, 2);
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
		cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_WGT);
		cond.operand2.integer = edgeWeightFilter;

		FldSpec[] proj1 = { new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label),
				new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_ID) };
		IndexNLJ_NodeSourceEdge nlj = null;
		try {
			nlj = new IndexNLJ_NodeSourceEdge(500, cond, proj1, 2);
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
			cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_LABEL);
			cond.operand2.string = edgeLabelFilter;
		}

		FldSpec[] proj1 = { new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label),
				new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_ID) };
		IndexNLJ_NodeDestEdge nlj = null;
		try {
			nlj = new IndexNLJ_NodeDestEdge(500, cond, proj1, 2);
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
		cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_WGT);
		cond.operand2.integer = edgeWeightFilter;

		FldSpec[] proj1 = { new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label),
				new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_ID) };
		IndexNLJ_NodeDestEdge nlj = null;
		try {
			nlj = new IndexNLJ_NodeDestEdge(500, cond, proj1, 2);
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
		cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_ID);
		cond.operand2.integer = edgeIdFilter;

		FldSpec[] proj1 = { new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label),
				new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_ID) };
		IndexNLJ_NodeDestEdge nlj = null;
		try {
			nlj = new IndexNLJ_NodeDestEdge(500, cond, proj1, 2);
			return nlj;
		} catch (Exception e) {
			System.out.println("error:" + e);
		}
		return null;
	}

	public String nodeDescToLabel(Descriptor desc) {
		ZTreeFile nodeDescTree = db.btmgr.getNodeDescriptorTree();
		DescriptorKey searchKey = new DescriptorKey(ZEncoder.encode(desc));
		BTFileScan scan = nodeDescTree.new_scan(searchKey, searchKey);
		try {
			KeyDataEntry data = scan.get_next();
			RID rid = ((LeafData) data.data).getData();
			Heapfile nodeHeap = db.hfmgr.getNodefile();
			Node node = new Node(nodeHeap.getRecord(rid));
			return node.getLabel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
