public class ModKaratsuba{
  Quantum q;
  Constmult cm;
  Karatsuba ka;
  int e;
  
  public ModKaratsuba(Quantum a, Constmult b, Karatsuba c){
    q = a;
    cm = b;
    ka = c;
  }
  
  public void changeQuantum(Quantum c){ //change the Quantum to reset gate count without recalculating LU-decomposition
    q = c;
    cm.changeQuantum(c);
    ka.changeQuantum(c);
  }
  
  public void modmult(int n, int[] m, int a, int b, int c, byte[] d){
    e = n;
    int k = n - n/2;
    for(int i = 0; i < k; i++){
      q.reverseshift(n, c, d, m);
    }
    q.ADD(n-k,a+k,a,d);
    q.ADD(n-k,b+k,b,d);
    ka.kmult(k,a, b, c, d);
    q.ADD(n-k,b+k,b,d);
    q.ADD(n-k,a+k,a,d);
    cm.multreverse(c, d);
    ka.kmult(n-k, a+k, b+k, c, d);
    for(int i = 0; i < k; i++){
      q.modshift(n, c, d, m);
    }
    ka.kmult(k, a, b, c, d);
    cm.mult(c,d);
  }
  
  public void pa(byte[] a){
    System.out.print("|");
    for(int i = 0; i < a.length; i++){
      System.out.print(a[i]);
      if ((i+1)%e==0){
        System.out.print("|");
      } else {
      System.out.print(",");
      }
    }
    System.out.println();
  }
  
}