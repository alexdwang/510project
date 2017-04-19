package graphtools;

import edgeheap.Edge;
import global.AttrOperator;
import global.AttrType;
import heap.Tuple;
import iterator.CondExpr;
import iterator.FldSpec;
import iterator.IndexNLJ_NodeSourceEdge;
import iterator.RelSpec;
import nodeheap.Node;

import java.util.ArrayList;
import java.util.List;

public class NLJHelper {

	public IndexNLJ_NodeSourceEdge nodeSourceEdgeJoin(String edgeLabelFilter) {
		CondExpr cond = new CondExpr();
		cond.next = null;
		cond.op = new AttrOperator(AttrOperator.aopEQ);
		cond.type1 = new AttrType(AttrType.attrSymbol);
		cond.type2 = new AttrType(AttrType.attrString);
		cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_LABEL);
		cond.operand2.string = edgeLabelFilter;

		FldSpec[]  proj1 = {
				new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label),
				new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_ID)
		};
		IndexNLJ_NodeSourceEdge nlj = null;
		try{
			nlj = new IndexNLJ_NodeSourceEdge(500, cond, proj1, 2);
			return nlj;
		}catch(Exception e){
			System.out.println("error:"+e);
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

		FldSpec[]  proj1 = {
				new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label),
				new FldSpec(new RelSpec(RelSpec.innerRel), Edge.FLD_ID)
		};
		IndexNLJ_NodeSourceEdge nlj = null;
		try{
			nlj = new IndexNLJ_NodeSourceEdge(500, cond, proj1, 2);
			return nlj;
		}catch(Exception e){
			System.out.println("error:"+e);
		}
		return null;
	}
}
