package zindex;

import btree.*;

public class ZFileScan extends BTFileScan {

    @Override
    public KeyDataEntry get_next() throws ScanIteratorException {
        return super.get_next();
    }

    @Override
    public void delete_current() throws ScanDeleteException {
        super.delete_current();
    }

    @Override
    public int keysize() {
        return super.keysize();
    }
}
