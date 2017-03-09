package global;

import java.lang.*;

public class Descriptor {
  int[] value = new int[5];
  public void set(int value0, int value1, int value2, int value3, int value4) {
     value[0] = value0;
     value[1] = value1;
     value[2] = value2;
     value[3] = value3;
     value[4] = value4;
  }

  public void set(int[] values){
     value[0] = values[0];
     value[1] = values[1];
     value[2] = values[2];
     value[3] = values[3];
     value[4] = values[4];
  }

  public void copyDesc(Descriptor fromDesc){
    set(fromDesc.get(0), fromDesc.get(1), fromDesc.get(2), fromDesc.get(3), fromDesc.get(4));
  }
  public int get(int idx) {
     return value[idx];
  }
  public double equal (Descriptor desc) {
    //return 1 if equal; 0 if not
    
    return 0;
  }
  public double distance (Descriptor desc) {
    //return the Euclidean distance between the descriptors
    double distance = 0;
    for(int i = 0;i < 5; ++i){
      distance += Math.pow(value[i] - desc.get(i), 2);
    }
    distance = Math.pow(distance, 0.5);
    return distance;
  } 

  public double getLength(){
    double len = 0;
    for(int i = 0;i < 5; ++i){
      len += Math.pow(value[i], 2);
    }
    len = Math.pow(len, 0.5);
    return len;
  }

  public String toString(){
    return "{ " + value[0] + ", " + value[1] + ", " + value[2] + ", " + value[3] + ", " + value[4] + " }";
  }
}