/* File Tuple.java */

package heap;

import java.io.*;
import java.lang.*;
import global.*;


public class Node extends Tuple{
   /**
    * Class constructor
    * Creat a new tuple with length = max_size,tuple offset = 0.
    */

  public  Tuple()
  {
       // Creat a new tuple
       data = new byte[max_size];
       tuple_offset = 0;
       tuple_length = max_size;
  }
}

