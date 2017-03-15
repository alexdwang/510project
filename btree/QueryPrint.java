package btree;

import java.io.IOException;

import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;
import diskmgr.Page;
import edgeheap.Edge;
import global.AttrType;
import global.PageId;
import global.RID;
import global.SystemDefs;
import heap.FieldNumberOutOfBoundException;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.Heapfile;
import heap.InvalidSlotNumberException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;

public class QueryPrint {
	public static final int INVALID_PAGE = -1;
	private static Heapfile thisedgefile;
	public static void printAllLeafPages(BTreeHeaderPage header, Heapfile edgefile) 
		    throws IOException, 
			   ConstructPageException, 
			   IteratorException,
			   HashEntryNotFoundException,
			   InvalidFrameNumberException,
			   PageUnpinnedException,
			   ReplacerException 
		    {
		thisedgefile=edgefile;
		      if(header.get_rootId().pid == INVALID_PAGE) {
			System.out.println("The Tree is Empty!!!");
			return;
		      }		      
		      _printAllLeafPages(header.get_rootId(), header.get_keyType());
		    }
		  
		  private static void _printAllLeafPages(PageId currentPageId,  int keyType) 
		    throws IOException, 
			   ConstructPageException, 
			   IteratorException,
			   InvalidFrameNumberException, 
			   HashEntryNotFoundException,
			   PageUnpinnedException,
			   ReplacerException
		    {
		      
		      BTSortedPage sortedPage=new BTSortedPage(currentPageId, keyType);
		      
		      if( sortedPage.getType()==NodeType.INDEX) {  
			BTIndexPage indexPage=new BTIndexPage((Page)sortedPage, keyType);
			
			_printAllLeafPages( indexPage.getPrevPage(),  keyType);
			
			RID rid=new RID();
			for( KeyDataEntry entry=indexPage.getFirst(rid); entry!=null; 
			     entry=indexPage.getNext(rid)) {
			  _printAllLeafPages( ((IndexData)entry.data).getData(),  keyType);
			}
		      }
		      
		      if( sortedPage.getType()==NodeType.LEAF) {  
			printPage(currentPageId, keyType);
		      }
		      
		      
		      SystemDefs.JavabaseBM.unpinPage(currentPageId , true/*dirty*/);
		    }
		  public static void printPage(PageId pageno, int keyType)
				    throws  IOException, 
					    IteratorException, 
					    ConstructPageException,
				            HashEntryNotFoundException, 
					    ReplacerException, 
					    PageUnpinnedException, 
				            InvalidFrameNumberException
				    {
				      BTSortedPage sortedPage=new BTSortedPage(pageno, keyType);
				      int i;
				      i=0;
				      if ( sortedPage.getType()==NodeType.INDEX ) {
				        BTIndexPage indexPage=new BTIndexPage((Page)sortedPage, keyType);
				        RID rid=new RID();
					
				        for(KeyDataEntry entry=indexPage.getFirst(rid); entry!=null; 
					    entry=indexPage.getNext(rid)){
//					  if( keyType==AttrType.attrInteger) 
//					    System.out.println(i+" (key, pageId):   ("+ 
//							       (IntegerKey)entry.key + ",  "+(IndexData)entry.data+ " )");
//					  if( keyType==AttrType.attrString) 
//					    System.out.println(i+" (key, pageId):   ("+ 
//							       (StringKey)entry.key + ",  "+(IndexData)entry.data+ " )");
					  
					  i++;    
				        }
				        System.out.println("");
				      }
				      else if ( sortedPage.getType()==NodeType.LEAF ) {
				        BTLeafPage leafPage=new BTLeafPage((Page)sortedPage, keyType);
					
				        RID rid=new RID();
					
				        for(KeyDataEntry entry=leafPage.getFirst(rid); entry!=null; 
					    entry=leafPage.getNext(rid)){
//					  if( keyType==AttrType.attrInteger) 
//					    System.out.println(i+" (key, [pageNo, slotNo]):   ("+ 
//							       (IntegerKey)entry.key+ ",  "+(LeafData)entry.data+ " )");
					  if( keyType==AttrType.attrString) {
//					    System.out.println(i+" (key, [pageNo, slotNo]):   ("+ 
//							       (StringKey)entry.key + ",  "+(LeafData)entry.data); 
						  LeafData ld = (LeafData)entry.data;
						  try {
							Edge e = new Edge(thisedgefile.getRecord((ld).getData()));
							e.print();
						} catch (InvalidTypeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidTupleSizeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FieldNumberOutOfBoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidSlotNumberException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (HFException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (HFDiskMgrException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (HFBufMgrException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					  }
					  i++;
				        }
					
					System.out.println("************** END ********");
					System.out.println("");
				      }
				      else {
					System.out.println("Sorry!!! This page is neither Index nor Leaf page.");
				      }      
				      
				      SystemDefs.JavabaseBM.unpinPage(pageno, true/*dirty*/);
				    }
				  
}
