public class Karatsuba{
  Quantum q;
  
  public Karatsuba(Quantum c){
    q = c;
  }
  
  public void changeQuantum(Quantum c){
    q = c;
  }
  
  public int[] divide(int k){ //returns 2 halves of k, with r[0] = r[1] + 1 if k odd and r[0] = r[1] otherwise
    int[] r = new int[2];
    r[0] = k - k/2;
    r[1] = k/2;
    return r;
  }
  
  public void kmult(int k, int a, int b, int c, byte[] d){ //multiply the qbits a..a+k-1 with b..b+k-1 and add to c..c+2k-2
    if (k==1){
      q.TOF(a, b, c, d);
    } else {
      int[] k01 = divide(k);
      int k0 = k01[0];
      int k1 = k01[1];
      subm(k0, k0, a, b, c, d); //h= h+(1+x^k)f0g0
      subm(k0, k1, a+k0, b+k0, c+ k0, d); //h= h+ x^k(1+x^k)f1g1
      q.ADD(k1, a+k0, a, d);
      q.ADD(k1, b+k0, b, d);
      kmult(k0, a, b, c+k0, d); //h= h+x^k(f0+f1)(g0+g1)
      q.ADD(k1, a+k0, a, d);
      q.ADD(k1, b+k0, b, d);
    }
  }
  
  public void subm(int k, int n, int a, int b, int c, byte[]d){ //c..c+2n+k-3 = c..c+2n+k-3 + (1+x^k)(a..a+k-1)*(b..b+k-1)
    int l;
    if (2*n-1-k>0){ //l = max(0,2n-1-k)
      l = 2*n-1-k;
    } else {
      l = 0;
    }
    if (n > 1){
      q.ADD(l, c+2*k, c+k, d);
      q.ADD(k, c+k, c, d);
      kmult(n, a, b, c+k, d);
      q.ADD(k, c+k, c, d);
      q.ADD(l, c+2*k, c+k, d);
    } else {
      q.CNOT(c+k,c,d);
      q.TOF(a,b,c+k,d);
      q.CNOT(c+k,c,d);
    }
  }
  
  
  public void pa(byte[] a){
    for(int i = 0; i < a.length; i++){
      System.out.print(a[i]);
      //System.out.print(" ");
    }
    System.out.println();
  }
}