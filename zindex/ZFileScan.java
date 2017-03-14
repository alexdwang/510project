package zindex;

import btree.BTFileScan;
import btree.KeyDataEntry;
import btree.ScanIteratorException;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;

import java.io.IOException;

public class ZFileScan {

    private ZTreeFile zTreeFile;

    private BTFileScan scan;

    public ZFileScan(ZTreeFile zfile) {
        this.zTreeFile = zfile;
        this.scan = zTreeFile.new_scan(null, null);
    }

    public KeyDataEntry getNext() {
        try {
            return scan.get_next();
        } catch (ScanIteratorException e) {
            e.printStackTrace();
        }
        return null;
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
}
