package graphtools;

import edgeheap.Edge;
import global.AttrOperator;
import global.AttrType;
import heap.Tuple;
import iterator.*;
import nodeheap.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NLJHelper {

	public String[] nodeToNode(String sourcelabel, String destlabel) {
		String result[];
		IndexNLJ_EdgeSourceNode edges = edgeSourceNodeJoin(sourcelabel);
		Tuple t = null;
		try {
			while ((t = edges.get_next()) != null) {
//				System.out.println(t.getIntFld(2));
				IndexNLJ_NodeDestEdge findnodes = nodeDestJoinByEdgeId(t.getIntFld(2));
				Tuple n = null;
				while((n = findnodes.get_next()) !=null ){
					if(n.getStrFld(1).equals(destlabel))
						return new String[] {sourcelabel, destlabel};
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
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
				new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_ID) };
		IndexNLJ_EdgeSourceNode nlj = null;
		try {
			nlj = new IndexNLJ_EdgeSourceNode(500, cond, proj1, 2);
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
}
