package zindex;


import btree.BTFileScan;

public class ZFileScan {

    private ZTreeFile zTreeFile;

    public ZFileScan(String filename) {
        try {
            this.zTreeFile = new ZTreeFile(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BTFileScan startScan() {
        return zTreeFile.new_scan(null, null);
    }
}
