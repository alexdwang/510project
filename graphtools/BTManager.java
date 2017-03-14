package graphtools;

import btree.BTreeFile;

public class BTManager {
	private BTreeFile nodelabelbtree;
	private BTreeFile edgelabelbtree;
	private BTreeFile edgeweightbtree;
	
	public BTreeFile getNodelabelbtree() {
		return nodelabelbtree;
	}

	public void setNodelabelbtree(BTreeFile nodelabelbtree) {
		this.nodelabelbtree = nodelabelbtree;
	}

	public BTreeFile getEdgelabelbtree() {
		return edgelabelbtree;
	}

	public void setEdgelabelbtree(BTreeFile edgelabelbtree) {
		this.edgelabelbtree = edgelabelbtree;
	}

	public BTreeFile getEdgeweightbtree() {
		return edgeweightbtree;
	}

	public void setEdgeweightbtree(BTreeFile edgeweightbtree) {
		this.edgeweightbtree = edgeweightbtree;
	}

}
