package zindex;

import java.math.BigInteger;

import btree.StringKey;

public class DescriptorKey extends StringKey {

    /**
     * Class constructor
     *
     * @param s the value of the string key to be set
     */
    public DescriptorKey(String s) {
        super(s);
    }
    
    public int compareTo(DescriptorKey desc) {
    	BigInteger codeInt1 = new BigInteger(super.getKey(), 2);
    	BigInteger codeInt2 = new BigInteger(desc.getKey(), 2);
    	
    	return codeInt1.compareTo(codeInt2);
    }
}
