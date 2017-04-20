package iterator;

import edgeheap.Edge;
import global.AttrOperator;
import global.AttrType;
import global.IndexType;
import global.RID;
import heap.Heapfile;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Scan;
import heap.Tuple;
import index.IndexException;
import index.IndexScan;
import index.UnknownIndexTypeException;
import nodeheap.Node;

import java.io.IOException;

import bufmgr.PageNotReadException;
import org.w3c.dom.Attr;

public class IndexNLJ_EdgeDestNode extends NestedLoopsJoins {

	private static CondExpr _buildJoinCond() {
		CondExpr cond = new CondExpr();
		cond.next = null;
		cond.op = new AttrOperator(AttrOperator.aopEQ);
		cond.type1 = new AttrType(AttrType.attrSymbol);
		cond.type2 = new AttrType(AttrType.attrSymbol);
		cond.operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), Node.FldID_Label);
		cond.operand2.symbol = new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_DST_LABEL);

		return cond;
	}

	/**
	 * constructor Initialize the two relations which are joined, including
	 * relation type,
	 *
	 * @param amt_of_mem
	 *            IN PAGES
	 * @param rightFilter
	 *            reference to filter applied on right i/p
	 * @param proj_list
	 *            shows what input fields go where in the output tuple
	 * @param n_out_flds
	 *            number of outer relation fileds
	 * @throws IOException
	 *             some I/O fault
	 * @throws NestedLoopException
	 *             exception from this class
	 */
	public IndexNLJ_EdgeDestNode(int amt_of_mem, CondExpr rightFilter, FldSpec[] proj_list, int n_out_flds)
			throws IOException, NestedLoopException, UnknownIndexTypeException, InvalidTypeException, IndexException,
			InvalidTupleSizeException, FileScanException, TupleUtilsException, InvalidRelation {
		super(Edge.FLD_TYPES, Edge.FLD_CNT,
				new short[] {Node.LABEL_MAX_LENGTH, Node.LABEL_MAX_LENGTH, Edge.LABEL_MAX_LENGTH},
				Node.FLD_TYPES, Node.FLD_CNT,
				new short[] { Node.LABEL_MAX_LENGTH },
				amt_of_mem,
				new FileScan("edgefile",new AttrType[] {new AttrType(AttrType.attrInteger), new AttrType(AttrType.attrInteger), new AttrType(AttrType.attrString),
						new AttrType(AttrType.attrString), new AttrType(AttrType.attrString)},
						new short[] {Node.LABEL_MAX_LENGTH, Node.LABEL_MAX_LENGTH, Edge.LABEL_MAX_LENGTH},
						Edge.FLD_CNT,
						5,
						new FldSpec[]{
								new FldSpec(new RelSpec((RelSpec.outer)), Edge.FLD_ID),
								new FldSpec(new RelSpec((RelSpec.outer)), Edge.FLD_WGT),
								new FldSpec(new RelSpec((RelSpec.outer)), Edge.FLD_SRC_LABEL),
								new FldSpec(new RelSpec((RelSpec.outer)), Edge.FLD_DST_LABEL),
								new FldSpec(new RelSpec((RelSpec.outer)), Edge.FLD_LABEL)								
						}, null),
				"nodefile", new CondExpr[]{_buildJoinCond(), rightFilter, null}, null, proj_list, n_out_flds);
	}

	public Tuple get_next() throws Exception {
		return super.get_next();
	}
}
