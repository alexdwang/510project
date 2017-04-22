package graphtools;

public class Path {
	

	public String head;
	public String tail;
	
	public int totalWeight;

	public Path(String head, String tail) {
		// TODO Auto-generated constructor stub
		this.head=head;
		this.tail=tail;
		this.totalWeight = 0;
	}
	
	public Path(String head, String tail, int totalWeight) {
		// TODO Auto-generated constructor stub
		this.head = head;
		this.tail = tail;
		this.totalWeight = totalWeight;
	}
}
