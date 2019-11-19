import java.lang.Math;
import java.util.ArrayList;

public class fltDiv{
  Quantum q;
  ModKaratsuba mk;
  SquareMatrix sq;
  
  public fltDiv(Quantum a, ModKaratsuba b, SquareMatrix c){
    q = a;
    mk = b;
    sq = c;
  }
  
  public void changeQuantum(Quantum c){ //change the Quantum to reset gate count without recalculating LU-decomposition
    q = c;
    mk.changeQuantum(c);
  }
  
  public int[] getks(int n){ //calculate k_1,...,k_t
    int a = n-1;
    ArrayList<Integer> loglist = new ArrayList<Integer>();
    int log;
    while (a>0){
      log = (int) (Math.log(a)/Math.log(2));
      a -= Math.pow(2, log);
      loglist.add(log);
    }
    int[] r = new int[loglist.size()];
    for(int i = 0; i < r.length; i++){
      r[i] = loglist.get(i);
    }
    return r;
  }
  
  public int getk(int[] k){ //calculate k
    if (k.length > 1){
      return k.length + k[0] - 1;
    } else {
      return k[0] + 1;
    }
  }
  
  public void div(int n, int[] m, int f0, int b, int c, int fs, byte[] d){
    int[] ks = getks(n); //step 0
    int k = getk(ks);
    int[] f = new int[k+1];
    f[0] = f0;
    for(int i = 0; i < k; i++){
      f[i+1] = fs+i*n;
    }
    
    for(int i = 1; i<=ks[0];i++){ //step 1
      q.ADD(n, f[i-1], f[k], d);
      for (int j = 0; j < Math.pow(2,i-1); j++){
        sq.square(f[k],d);
      }
      mk.modmult(n, m, f[i-1], f[k], f[i], d);
      for (int j = 0; j < Math.pow(2,i-1); j++){
        sq.squareRoot(f[k],d);
      }
      q.ADD(n, f[i-1], f[k], d);
    }
    
    for(int s = 1; s<ks.length; s++){ //step 2
      for (int j = 0; j < Math.pow(2,ks[s]); j++){
        sq.square(f[ks[0]+s-1], d);
      }
      mk.modmult(n, m, f[ks[0]+s-1], f[ks[s]], f[ks[0]+s], d);
    }
    
    if (ks.length == 1){
      q.multiswap(n, f[ks[0]], f[k],d);
    }
    
    sq.square(f[k], d); //step 3
    mk.modmult(n, m, f[k], b, c, d);
    
    //uncompute
    sq.squareRoot(f[k], d); //step 3
    
    if (ks.length == 1){
      q.multiswap(n, f[ks[0]], f[k],d);
    }
    
    for(int s = ks.length -1 ; s>0; s--){ //step 2
      mk.modmult(n, m, f[ks[0]+s-1], f[ks[s]], f[ks[0]+s], d);
      for (int j = 0; j < Math.pow(2,ks[s]); j++){
        sq.squareRoot(f[ks[0]+s-1], d);
      }
    }
    
    for(int i = ks[0]; i>0;i--){ //step 1
      q.ADD(n, f[i-1], f[k], d);
      for (int j = 0; j < Math.pow(2,i-1); j++){
        sq.square(f[k],d);
      }
      mk.modmult(n, m, f[i-1], f[k], f[i], d);
      for (int j = 0; j < Math.pow(2,i-1); j++){
        sq.squareRoot(f[k],d);
      }
      q.ADD(n, f[i-1], f[k], d);
    }
  }
  
  public void test(int n, int f, byte[] d){
    for (int i= 0; i < n; i++){
      System.out.print(Math.pow(2,i) + ": ");
      ppa(d, f, n);
      sq.square(f, d);
    }
  }
      
  
  public void ppa(byte[] a, int s, int k){ //print part of an array
    for(int i = 0; i< k; i++){
      System.out.print(a[s+i]);
    }
    System.out.println();
  }
}