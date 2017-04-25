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

public class IndexNLJ_NodeDestEdge extends IndexedNestedLoopJoin {

    public static final int OUT_FLD_NODE_LABEL = 1;
    public static final int OUT_FLD_EDGE_ID = 2;
    public static final int OUT_FLD_EDGE_WGT = 3;

    public static final AttrType[] OUT_ATTRTYPES = new AttrType[] {
            new AttrType(AttrType.attrString),
            new AttrType(AttrType.attrInteger),
            new AttrType(AttrType.attrInteger)
    };

    public IndexNLJ_NodeDestEdge(CondExpr innerFilter)
            throws IOException, InvalidTypeException, PageNotReadException, JoinsException, PredEvalException, UnknowAttrType, WrongPermat, InvalidTupleSizeException, FieldNumberOutOfBoundException, TupleUtilsException, InvalidRelation, FileScanException, IndexException, UnknownIndexTypeException {
        super(innerFilter, "nodefile");
    }

    public IndexNLJ_NodeDestEdge(CondExpr innerFilter, String outerRel)
            throws IOException, InvalidTupleSizeException, InvalidTypeException, JoinsException, FieldNumberOutOfBoundException, PageNotReadException, WrongPermat, PredEvalException, UnknowAttrType, TupleUtilsException, FileScanException, InvalidRelation, IndexException, UnknownIndexTypeException {
        super(innerFilter, outerRel);
    }

    @Override
    protected IndexScan startIndexScan() throws UnknownIndexTypeException, InvalidTypeException, IndexException, IOException, InvalidTupleSizeException {
        CondExpr cond = this.innerFilter[0];
        if (cond == null) {
            return new IndexScan(
                    new IndexType(IndexType.B_Index),
                    "edgefile",
                    "EdgeLabelTree_Destination",
                    Edge.FLD_TYPES,
                    Edge.STR_FLD_SIZE,
                    Edge.FLD_CNT, // noInFlds
                    Edge.FLD_CNT, // noOutFlds
                    new FldSpec[]{
                            new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_ID),
                            new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_WGT),
                            new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_SRC_LABEL),
                            new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_DST_LABEL),
                            new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_LABEL)
                    }, // outFlds
                    innerFilter,
                    Edge.FLD_DST_LABEL,
                    false
            );
        }

        String indName = "";
        int indFldNum = 0;
        switch (cond.operand1.symbol.offset) {
            case 1: // Edge.FLD_ID
                indName = "EdgeIdTree";
                indFldNum = Edge.FLD_ID;
                break;
            case 2: // Edge.FLD_WGT
                indName = "EdgeWeightTree";
                indFldNum = Edge.FLD_WGT;
                break;
            case 3: // Edge.FLD_SRC_LABEL
                indName = "EdgeLabelTree_Source";
                indFldNum = Edge.FLD_SRC_LABEL;
                break;
            case 4: // Edge.FLD_DST_LABEL
                indName = "EdgeLabelTree_Destination";
                indFldNum = Edge.FLD_DST_LABEL;
                break;
            case 5: // Edge.FLD_LABEL
                indName = "EdgeLabelTree";
                indFldNum = Edge.FLD_LABEL;
                break;
        }

        return new IndexScan(
                new IndexType(IndexType.B_Index),
                "edgefile",
                indName,	  // indName
                Edge.FLD_TYPES,
                Edge.STR_FLD_SIZE,
                Edge.FLD_CNT, // noInFlds
                Edge.FLD_CNT, // noOutFlds
                new FldSpec[]{
                        new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_ID),
                        new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_WGT),
                        new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_SRC_LABEL),
                        new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_DST_LABEL),
                        new FldSpec(new RelSpec(RelSpec.outer), Edge.FLD_LABEL)
                }, // outFlds
                innerFilter,
                indFldNum,
                false
        );
    }

    @Override
    protected FileScan startFileScan(String relName) throws InvalidRelation, TupleUtilsException, FileScanException, IOException {
        return new FileScan(
                relName,
                Node.FLD_TYPES,
                Node.STR_FLD_SIZE,
                Node.FLD_CNT,
                Node.FLD_CNT,
                new FldSpec[] {
                        new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Desc),
                        new FldSpec(new RelSpec(RelSpec.outer), Node.FldID_Label)
                },
                null
        );
    }
    
    @Override
    protected void initJoinedTuple() throws InvalidTupleSizeException, IOException, InvalidTypeException {
        joinedTuple = new Tuple();
        joinedTuple.setHdr((short) 3, new AttrType[] {
                new AttrType(AttrType.attrString),
                new AttrType(AttrType.attrInteger),
                new AttrType(AttrType.attrInteger)
        }, Node.STR_FLD_SIZE);
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

            String outerLabel = curOuter.getStrFld(Node.FldID_Label);
            String innerLabel = curInner.getStrFld(Edge.FLD_DST_LABEL);
            int outEdgeId = curInner.getIntFld(Edge.FLD_ID);
            int outEdgeWgt = curInner.getIntFld(Edge.FLD_WGT);

            if (outerLabel.equals(innerLabel)) {
                joinedTuple.setStrFld(OUT_FLD_NODE_LABEL, outerLabel);
                joinedTuple.setIntFld(OUT_FLD_EDGE_ID, outEdgeId);
                joinedTuple.setIntFld(OUT_FLD_EDGE_WGT, outEdgeWgt);
                break;
            }
        }

        return joinedTuple;
    }
}
