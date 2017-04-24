package iterator;

import bufmgr.PageNotReadException;
import edgeheap.Edge;
import global.AttrType;
import global.IndexType;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;
import index.IndexException;
import index.IndexScan;
import index.UnknownIndexTypeException;
import nodeheap.Node;

import java.io.IOException;

public class IndexNLJ_EdgeDestNode extends IndexedNestedLoopJoin {

	public static final int OUT_FLD_NODE_LABEL = 1;
	public static final int OUT_FLD_EDGE_ID = 2;
	public static final int OUT_FLD_EDGE_WGT = 3;
	public static final int OUT_FLD_EDGE_LABEL = 4;

	public static final AttrType[] OUT_ATTRTYPES = new AttrType[] {
			new AttrType(AttrType.attrString),
			new AttrType(AttrType.attrInteger),
			new AttrType(AttrType.attrInteger),
			new AttrType(AttrType.attrString)
	};

	public IndexNLJ_EdgeDestNode(CondExpr innerFilter) throws IOException, InvalidTupleSizeException, InvalidTypeException, JoinsException, FieldNumberOutOfBoundException, PageNotReadException, WrongPermat, PredEvalException, UnknowAttrType, TupleUtilsException, FileScanException, InvalidRelation, IndexException, UnknownIndexTypeException {
		super(innerFilter, "edgefile");
	}

	public IndexNLJ_EdgeDestNode(CondExpr innerFilter, String outerRel) throws IOException, InvalidTupleSizeException, InvalidTypeException, JoinsException, FieldNumberOutOfBoundException, PageNotReadException, WrongPermat, PredEvalException, UnknowAttrType, TupleUtilsException, FileScanException, InvalidRelation, IndexException, UnknownIndexTypeException {
		super(innerFilter, outerRel);
	}

	@Override
	protected IndexScan startIndexScan() throws UnknownIndexTypeException, InvalidTypeException, IndexException, IOException, InvalidTupleSizeException {
		CondExpr cond = this.innerFilter[0];
		if (cond == null) {
			return new IndexScan(
					new IndexType(IndexType.B_Index),
					"nodefile",
					"NodeLabelTree",
					Node.FLD_TYPES,
					Node.STR_FLD_SIZE,
					Node.FLD_CNT, // noInFlds
					Node.FLD_CNT, // noOutFlds
					new FldSpec[]{
							new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Desc),
							new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label)
					}, // outFlds
					null,
					Node.FldID_Label,
					false
			);
		}

		String indName = "";
		int indFldNum = 0;
		switch (cond.operand1.symbol.offset) {
			case 1: // Node.FldID_Desc
				throw new IndexException("Index scan on Node.descriptor is not supported yet");
			case 2: // Node.FldID_Label
				indName = "NodeLabelTree";
				indFldNum = Node.FldID_Label;
				break;
		}

		return new IndexScan(
				new IndexType(IndexType.B_Index),
				"nodefile",
				indName,	  // indName
				Node.FLD_TYPES,
				Node.STR_FLD_SIZE,
				Node.FLD_CNT, // noInFlds
				Node.FLD_CNT, // noOutFlds
				new FldSpec[]{
						new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Desc),
						new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label)
				}, // outFlds
				innerFilter,
				indFldNum,
				false
		);
	}

	@Override
	protected void initJoinedTuple() throws InvalidTupleSizeException, IOException, InvalidTypeException {
		joinedTuple = new Tuple();
		joinedTuple.setHdr((short) 4, new AttrType[] {
				new AttrType(AttrType.attrString),
				new AttrType(AttrType.attrInteger),
				new AttrType(AttrType.attrInteger),
				new AttrType(AttrType.attrString)
		}, new short[] {
				Node.LABEL_MAX_LENGTH, Edge.LABEL_MAX_LENGTH
		});
	}

	@Override
	protected FileScan startFileScan(String relName) throws InvalidRelation, TupleUtilsException, FileScanException, IOException {
		return new FileScan(
				relName,
				Edge.FLD_TYPES,
				Edge.STR_FLD_SIZE,
				Edge.FLD_CNT,
				Edge.FLD_CNT,
				new FldSpec[] {
						new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_ID),
						new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_WGT),
						new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_SRC_LABEL),
						new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_DST_LABEL),
						new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_LABEL),
				},
				null
		);
	}

	@Override
	public Tuple get_next() throws UnknownKeyTypeException, IndexException, IOException, JoinsException, FieldNumberOutOfBoundException, PageNotReadException, WrongPermat, InvalidTypeException, InvalidTupleSizeException, PredEvalException, UnknowAttrType, UnknownIndexTypeException {
		while (true) {
			curInner = innerItr.get_next();
			// Current round (inner) done
			if (curInner == null) {
				// End of inner relation, move to next outer tuple
				curOuter = outerItr.get_next();
				// End of outer relation
				if (curOuter == null) {
					innerItr.close();
					outerItr.close();
					return null;
				}
				// Start inner scan again
				this.innerItr.close();
				this.innerItr = startIndexScan();
				this.curInner = innerItr.get_next();
			}

			String outerLabel = curOuter.getStrFld(Edge.FLD_DST_LABEL);
			String innerLabel = curInner.getStrFld(Node.FldID_Label);
			int outEdgeId = curOuter.getIntFld(Edge.FLD_ID);
			int outEdgeWeight = curOuter.getIntFld(Edge.FLD_WGT);
			String outEdgeLabel = curOuter.getStrFld(Edge.FLD_LABEL);

			if (outerLabel.equals(innerLabel)) {
				joinedTuple.setStrFld(OUT_FLD_NODE_LABEL, innerLabel);
				joinedTuple.setIntFld(OUT_FLD_EDGE_ID, outEdgeId);
				joinedTuple.setIntFld(OUT_FLD_EDGE_WGT, outEdgeWeight);
				joinedTuple.setStrFld(OUT_FLD_EDGE_LABEL, outEdgeLabel);
				break;
			}
		}

		return joinedTuple;
	}
}
