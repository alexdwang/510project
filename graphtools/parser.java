package graphtools;
import java.lang.Integer;

import global.Descriptor;

public class parser {
	
	public parser(){
		
	}
	
	public String discrmN(String token){
		
		String[] res = {"label", "descriptor"};
		char delims = ' ';
		int len = token.length();
		
		for (int i = 0; i < len; i++){
			if (token.charAt(i) == delims)
				return res[1]; 
		}
		
		return res[0];
	}

    public String discrmE(String token){
		
		String[] res = {"weight", "label"};
		String[] delims = {"W:","E:"};
		int len = token.length();
		
		if(token.substring(0, 2).equalsIgnoreCase(delims[1]))
			return res[1];
		else if(token.substring(0, 2).equalsIgnoreCase(delims[0]))
			return res[0];
		else return null;
	}
    
    public Descriptor convDesc(String token){

    	String delims = " ";
    	String[] value = token.split(delims);
    	if (value.length != 5)
    		System.err.println ("Descriptor size not match!\n");
    	
    	Descriptor descp = new Descriptor();
    	int val[] = {0,0,0,0,0};
    	for (int i = 0; i < 5; i++){
    		val[i] = Integer.parseInt(value[i]);
    	}

		descp.set(val);;
    	return descp;
    }
}