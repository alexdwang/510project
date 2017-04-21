package iterator;

import bufmgr.PageNotReadException;
import edgeheap.Edge;
import global.AttrType;
import global.IndexType;
import heap.*;
import index.IndexException;
import index.IndexScan;
import index.UnknownIndexTypeException;
import nodeheap.Node;

import java.io.IOException;

public abstract class IndexedNestedLoopJoin {

    protected CondExpr[] innerFilter;

    protected FileScan outerItr;
    protected IndexScan innerItr;

    protected Tuple curOuter;
    protected Tuple curInner;
    protected Tuple joinedTuple;

    public IndexedNestedLoopJoin(CondExpr innerFilter, String outerRel) throws IOException, InvalidTupleSizeException, InvalidTypeException, JoinsException, FieldNumberOutOfBoundException, PageNotReadException, WrongPermat, PredEvalException, UnknowAttrType, TupleUtilsException, FileScanException, InvalidRelation, IndexException, UnknownIndexTypeException {
        this.innerFilter = new CondExpr[] { innerFilter, null };

        this.initJoinedTuple();
        this.outerItr = this.startFileScan(outerRel);
        // Init this.curOuter
        this.curOuter = outerItr.get_next();
        this.innerItr = this.startIndexScan();
    }

    protected void initJoinedTuple() throws InvalidTupleSizeException, IOException, InvalidTypeException {
        joinedTuple = new Tuple();
        joinedTuple.setHdr((short) 2, new AttrType[] {
                new AttrType(AttrType.attrString),
                new AttrType(AttrType.attrInteger)
        }, Node.STR_FLD_SIZE);
    }

    protected abstract IndexScan startIndexScan() throws UnknownIndexTypeException, InvalidTypeException, IndexException, IOException, InvalidTupleSizeException;

    protected abstract FileScan startFileScan(String relName) throws InvalidRelation, TupleUtilsException, FileScanException, IOException;

    public abstract Tuple get_next() throws UnknownKeyTypeException, IndexException, IOException, JoinsException, FieldNumberOutOfBoundException, PageNotReadException, WrongPermat, InvalidTypeException, InvalidTupleSizeException, PredEvalException, UnknowAttrType, UnknownIndexTypeException;
}
