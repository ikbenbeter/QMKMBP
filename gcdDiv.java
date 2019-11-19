import java.lang.Math;

public class gcdDiv{
  Quantum q;
  ModKaratsuba mk;
  IncrementGidney ig;
  
  public gcdDiv(Quantum a, ModKaratsuba b, IncrementGidney c){
    q = a;
    mk = b;
    ig = c;
  }
  
  public void changeQuantum(Quantum c){ //change the Quantum to reset gate count without recalculating LU-decomposition
    q = c;
    mk.changeQuantum(c);
  }
  
  public void div(int n, int logn, int[] m, int g, int b, int c, int f, int r, int v, int g0, int delta, int a, int gn, byte[] d){
    int sign = delta+logn+1;
    //System.out.println("start");
    //pa(d, n);
    for(int i = 0; i < m.length; i++){ //reverse f
      q.NOT(f+n-m[i], d); 
    }
    q.NOT(sign, d); //ready delta=1
    q.NOT(r,d); //ready r=1
    
    for(int i =0; i<n/2;i++){ //reverse g
      q.swap(g+i, g+n-i-1,d);
    }
    
    int ell;
    int lambda;
    int gzero;
    for(int l=0; l<2*n-1; l++){
      ell = Math.min(2*n-2-l,n);
      lambda = Math.min(l+1, n);
      gzero = g0+l;
      if(l == n+1){
        gzero = gn;
      } else if(l>n+1){
        gzero = g+2*n-l;
      }
      
      q.rightshift(n+1, v, d); //rightshift v
      q.TOF(sign,g,a,d); //calculate a
      for(int i = 0; i < logn+2; i++){
        q.CNOT(a, delta+i, d); //CNOT(a, delta[0,...,logn+1])
      }
      q.CSWAP(false, ell, f, g, a, d);
      if(ell<n){
        q.fredkin(g+ell, f+ell, a, d); //since we say g is size n, we need a qubit at space n+1, gn, which we deal with separately but pretending it is part of g
      } else {
        q.fredkin(f+n, gn, a, d);
      }
      q.CSWAP(false, lambda+1, v, r, a, d);
      
      q.NOT(a, d);
      ig.ConIncrement(logn+2, a, delta, b, gzero, d);
      q.NOT(a, d);
      
      q.CNOT(v, a, d);
      q.CNOT(g, gzero, d);
      
      for(int i = 0; i < ell; i++){
        q.TOF(f+i, gzero, g+i, d);
      }
      
      if(ell<n){
        q.TOF(f+ell, gzero, g+ell, d);
      } else {
        q.TOF(f+n, gzero, gn, d);
      }
      
      for(int i = 0; i <= lambda; i++){
        q.TOF(v+i, gzero, r+i, d);
      }
      
      q.leftshift(ell, g, d);
      
      if(ell==0){
      } else if(ell<n){
        q.swap(g+ell,g+ell-1, d);
      } else {
        q.swap(g+n-1, gn, d);
      }
    }
    
    for(int i = 0; i < n/2; i++){
      q.swap(v+i, v+n-1-i, d);
    }
    
    mk.modmult(n, m, v, b, c, d);
    //uncomputation: 
    
    for(int i = 0; i < n/2; i++){
      q.swap(v+i, v+n-1-i, d);
    }
    for(int l=2*n-2; l>=0; l--){
      ell = Math.min(2*n-2-l,n);
      lambda = Math.min(l+1, n);
      gzero = g0+l;
      if(l == n+1){
        gzero = gn;
      } else if(l>n+1){
        gzero = g+2*n-l;
      }
      
      if(ell==0){
      } else if(ell<n){
        q.swap(g+ell,g+ell-1, d);
      } else {
        q.swap(g+n-1, gn, d);
      }
      
      q.rightshift(ell,g,d);
      
      for(int i = 0; i <= lambda; i++){
        q.TOF(v+i, gzero, r+i, d);
      }
      
      if(ell<n){
        q.TOF(f+ell, gzero, g+ell, d);
      } else {
        q.TOF(f+n, gzero, gn, d);
      }
      
      for(int i = 0; i < ell; i++){
        q.TOF(f+i, gzero, g+i, d);
      }
      
      q.CNOT(v, a, d);
      q.CNOT(g, gzero, d);
      
      q.NOT(a, d);
      ig.ConDecrease(logn+2, a, delta, b, gzero, d);
      q.NOT(a, d);
      
      q.CSWAP(false, lambda+1, v, r, a, d);
      if(ell<n){
        q.fredkin(g+ell, f+ell, a, d);
      } else {
        q.fredkin(f+n, gn, a, d);
      }
      q.CSWAP(false, ell, f, g, a, d);
      
      for(int i = 0; i < logn+2; i++){
        q.CNOT(a, delta+i, d); //CNOT(a, delta[0,...,logn+1])
      }
      q.TOF(sign,g,a,d); 
      q.leftshift(n+1, v, d); 
      
    }
    
    for(int i =0; i<n/2;i++){ //reverse g
      q.swap(g+i, g+n-i-1,d);
    }
    q.NOT(r,d); //uncompute r=1
    q.NOT(sign, d); //uncompute delta=1
    for(int i = 0; i < m.length; i++){ //reverse f
      q.NOT(f+n-m[i], d); 
    }
    
  }
  
  public void pa(byte[] a, int n){ //print an array if necessary
    System.out.print("|");
    for(int i = 0; i < a.length; i++){
      System.out.print(a[i]);
      if ((i+1)%n==0 && i < 3*n){
        System.out.print("|");
      } else if ((i-3*n+1)%(n+1)==0 && i >= 3*n && i <7*n+4){
        System.out.print("|");
      } else if (i==a.length-3){
        System.out.print("|");
      } else if (i==a.length-1){
        System.out.print("|");
      } else {
        System.out.print(",");
      }
    }
    System.out.println();
  }
}