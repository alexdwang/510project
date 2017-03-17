package zindex;

import btree.*;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;
import global.Descriptor;

import java.io.IOException;
import java.math.BigInteger;

public class ZFileRangeScan {

    private ZTreeFile zTreeFile;

    private Descriptor origin;
    private String loCode;
    private String hiCode;
    private int distance;

    private BTFileScan scan;

    public ZFileRangeScan(ZTreeFile zFile, Descriptor key, int distance) {
        this.zTreeFile = zFile;
        this.distance = distance;

        int[] target = new int[5];
        for (int i = 0; i < 5; i++) {
            target[i] = key.get(i);
        }
        int[] hi = new int[5];
        int[] lo = new int[5];
        for (int i = 0; i < 5; i++) {
            hi[i] = target[i] + distance;
            if (hi[i] < 0) hi[i] = 0;
            lo[i] = target[i] - distance;
            if (lo[i] < 0) lo[i] = 0;
        }
        this.origin = key;
        this.hiCode = ZEncoder.encodeArray(hi);
        this.loCode = ZEncoder.encodeArray(lo);
        BigInteger hiCodeInt = new BigInteger(hiCode, 2);
        BigInteger loCodeInt = new BigInteger(loCode, 2);

        this.scan = zTreeFile.new_scan(new DescriptorKey(loCode), new DescriptorKey(hiCode));
    }

    public KeyDataEntry getNext() {
        KeyDataEntry pending = null;
        // Retrieve a data from the BTree
        try {
            pending = scan.get_next();
        } catch (ScanIteratorException e) {
            e.printStackTrace();
        }
        while (pending != null) {
            // Data available
            String key = pending.key.toString();
            Descriptor desc = ZEncoder.decodeAsDesc(key);
            // In the hypercube and within distance
            if (this.isInHypercube(key, loCode, hiCode) && origin.distance(desc) <= distance) {
                break;
            }
            // Not fit in distance
            try {
                pending = scan.get_next();
            } catch (ScanIteratorException e) {
                e.printStackTrace();
            }
        }
        return pending;
    }

    public void endScan() {
        try {
            this.scan.DestroyBTreeFileScan();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFrameNumberException e) {
            e.printStackTrace();
        } catch (ReplacerException e) {
            e.printStackTrace();
        } catch (PageUnpinnedException e) {
            e.printStackTrace();
        } catch (HashEntryNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isInHypercube(String point, String lo, String hi) {
        int[] pointVec = ZEncoder.decodeAsArray(point);
        int[] loVec = ZEncoder.decodeAsArray(lo);
        int[] hiVec = ZEncoder.decodeAsArray(hi);

        for (int i = 0; i < 5; i++) {
            if (pointVec[i] > hiVec[i] || pointVec[i] < loVec[i]) {
                return false;
            }
        }
        return true;
    }
}
