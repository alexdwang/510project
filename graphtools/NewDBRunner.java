package graphtools;

import java.util.*;
import java.io.*;
import global.*;
import diskmgr.*;
import nodeheap.*;
import heap.*;
import iterator.*;

public class NewDBRunner implements GlobalConst{
	public static void main(String [] argvs) {
		/*
		SystemDefs sysdef = new SystemDefs( "dbname", 300, 50, "Clock" );
		Heapfile nodefile;
		try{
			nodefile = new Heapfile("nodefile");
			System.out.println("start inserting nodes");
			File file = new File("NodeTestData.txt");
			Scanner scan = new Scanner(file);
			int cnt = 0;
			while(scan.hasNextLine() && cnt < 10){
				String line = scan.nextLine();
				String[] data = line.split(" ");
				Node anode = new Node();
				Descriptor adesc = new Descriptor();
				String label = data[0];
				adesc.set(Integer.parseInt(data[1]), 
					Integer.parseInt(data[2]), 
					Integer.parseInt(data[3]), 
					Integer.parseInt(data[4]),
					Integer.parseInt(data[5]));
				short[] labelsize = {Node.LABEL_MAX_LENGTH};
				AttrType[] types = {new AttrType(AttrType.attrDesc), new AttrType(AttrType.attrString)};
				anode.setHdr(Node.FLD_CNT, types ,labelsize);
				anode.setLabel(label);
				anode.setDesc(adesc);
				nodefile.insertRecord(anode.getNodeByteArray());
				cnt++;
			}
			System.out.println(cnt + " nodes inserted");
		}catch(Exception e){
			System.err.println ("failed to change to new db\n");
			e.printStackTrace();
		}

		FileScan node_scanner;
		Sort sort;
		AttrType[] attrType = {new AttrType(AttrType.attrDesc), new AttrType(AttrType.attrString)};
		short[] attrSize = {Node.LABEL_MAX_LENGTH};
		TupleOrder[] order = { new TupleOrder(TupleOrder.Ascending), new TupleOrder(TupleOrder.Descending) };
    	RelSpec rel = new RelSpec(RelSpec.outer); 
    	FldSpec[] projlist = { new FldSpec(rel, 1), new FldSpec(rel, 2)};
		try {
			node_scanner = new FileScan("nodefile", attrType, attrSize, (short) 2, 2, projlist, null);
			sort = new Sort(attrType, (short) 2, attrSize, node_scanner, fld_no, order[0], Node.LABEL_MAX_LENGTH, SORTPGNUM);
		}catch (Exception e) {
			e.printStackTrace();
		} 
		*/
	}
}