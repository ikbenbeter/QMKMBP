public class SquareMatrix{
  Quantum q;
  PLU p;
  int n;
  
  public SquareMatrix(int[] m, Quantum c){
    p = new PLU(m);
    q = c;
    p.PLUdecomposition();
    n = p.P.length;
  }
  
  public void changeQuantum(Quantum c){
    q = c;
  }
  
  public void square(int g, byte[] d){ //applies squaring to g..g+n-1 (n is fixed by m)
    for(int i = 0; i < n; i++){ //apply U
      for(int j = i+1; j < n; j++){
        if(p.U[i][j]){
          q.CNOT(g+j,g+i, d);
        }
      }
    }
    for(int i = n-1; i >= 0; i--){ //apply L
      for(int j = i-1; j >= 0; j--){
        if(p.L[i][j]){
          q.CNOT(g+j,g+i, d);
        }
      }
    }
    int[] P = new int[n];
    for (int i = 0; i < n; i++){ //copy P
      P[i] = p.P[i];
    }
    for (int i = 0; i < n; i++){ //apply P
      if (!(P[i] == i)){
        boolean b = true;
        for (int j = i+1; (j<n) & b; j++){
          if (P[j] == i){
            q.swap(g+i,g+j,d);
            int t = P[i];
            P[i] = P[j];
            P[j] = t;
            b = false;
          }
        }
      }
    }
  }
  
  public void squareRoot(int g, byte[] d){ //the above but in reverse
    int[] P = new int[n];
    for (int i = 0; i < n; i++){ //copy & invert P
      P[p.P[i]] = i;
    }
    
    for (int i = 0; i < n; i++){ //apply P
      if (!(P[i] == i)){
        boolean b = true;
        for (int j = i+1; (j<n) & b; j++){
          if (P[j] == i){
            q.swap(g+i,g+j,d);
            int t = P[i];
            P[i] = P[j];
            P[j] = t;
            b = false;
          }
        }
      }
    }
    for(int i = 0; i < n; i++){ //apply L (in reverse)
      for(int j = 0; j < i; j++){
        if(p.L[i][j]){
          q.CNOT(g+j,g+i, d);
        }
      }
    }
    for(int i = n-1; i > -1; i--){ //apply U (in reverse)
      for(int j = n-1; j > i; j--){
        if(p.U[i][j]){
          q.CNOT(g+j,g+i, d);
        }
      }
    }
  } 
  
  public int[] depth(){
    return p.depthCalculator();
  }
  
  public void pa(int g, byte[] a){
    for(int i = g; i < a.length; i++){
      System.out.print(a[i]);
      System.out.print(" ");
    }
    System.out.println();
  }
}