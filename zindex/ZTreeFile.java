package zindex;

import btree.*;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;
import global.AttrType;
import global.RID;

import java.io.IOException;

public class ZTreeFile extends BTreeFile {

    public ZTreeFile(String filename) throws ConstructPageException, GetFileEntryException, PinPageException, AddFileEntryException, IOException {
    	super(filename, AttrType.attrString, 100, 1);
    }

    public void close() throws PageUnpinnedException, InvalidFrameNumberException, HashEntryNotFoundException, ReplacerException {
        super.close();
    }

    public void destroyFile() throws DeleteFileEntryException, IteratorException, PinPageException, IOException, ConstructPageException, FreePageException, UnpinPageException {
        super.destroyFile();
    }

    public void insert(DescriptorKey desc, RID rid) {
        try {
            super.insert(desc, rid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean Delete(DescriptorKey key, RID rid) {
        boolean result = false;
        try {
            result = super.Delete(key, rid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public BTFileScan new_scan(DescriptorKey lo_key, DescriptorKey hi_key) {
        BTFileScan ret = null;
        try {
            ret = super.new_scan(lo_key, hi_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
