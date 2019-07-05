import java.util.ArrayList;

public class Quantum{ //used for counting gates. If i applied any operation to an array of qbits the gate count would be wrong.
  int CNOTc;
  int TOFc;
  int depth;
  ArrayList<Integer> last;
  int s;
  int t;
  
  public Quantum(){
    CNOTc = 0;
    TOFc = 0;
    last = new ArrayList<Integer>();
    depth = 1;
  }
  
  public void CNOT(int a, int b, byte[] d){
    d[b] = (byte) ((d[a] + d[b]) % 2);
    CNOTc++;
    
    if ((!last.contains(a)) & (!last.contains(b))){
      last.add(a);
      last.add(b);
    } else {
      last.clear();
      last.add(a);
      last.add(b);
      depth++;
    }
  }
  
  public void ADD(int k, int a, int b, byte[] d){ //[b..b+k] = [a..a+k] + [b..b+k]
    for(int i = 0; i < k; i++){
      CNOT(a+i, b+i, d);
    }
  }
  
  public void TOF(int a, int b, int c, byte[] d){
    d[c] = (byte) ((d[c] + (d[a] * d[b]))%2);
    TOFc++;
    
    if ((!last.contains(a)) & (!last.contains(b)) & (!last.contains(c))){
      last.add(a);
      last.add(b);
      last.add(c);
    } else {
      last.clear();
      last.add(a);
      last.add(b);
      last.add(c);
      depth++;
    }
  }
  
  public void swap(int a, int b, byte[] d){
    byte c = d[a];
    d[a] = d[b];
    d[b] = c;
    
    if(last.contains(a) & (!last.contains(b))){
      t = last.indexOf(a);
      last.set(t, b);
    } else if (last.contains(b) & (!last.contains(a))){
      t = last.indexOf(b);
      last.set(t,a);
    } else if (last.contains(a) & last.contains(b)){
      s = last.indexOf(a);
      t = last.indexOf(b);
      last.set(s, b);
      last.set(t, a);
    }
      
    
  }
  
  public void multiswap(int k, int a, int b, byte[] d){
    for(int i = 0; i < k; i++){
      swap(a+i,b+i,d);
    }
  }
  
  public void modshift(int n, int a, byte[] d, int[] f){
    for(int i = n-1; i > 0; i--){
      swap(a+i, a+i-1, d);
    }
    for(int i = 1; i < f.length - 1; i++){
      CNOT(a, a+f[i], d);
    }
  }
}