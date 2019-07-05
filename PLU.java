import java.util.Stack;
import java.util.ArrayList;

public class PLU{
  int[] f;
  int[] m;
  int n;
  boolean[][] G;
  boolean[][] L;
  boolean[][] U;
  int[] P;
  
  public PLU(int[] a, int[] b){
    setfm(a, b);
    makematrix();
  }
  
  public void setfm(int[] a, int[] b){ //make sure f and m both contain no negatives and m is in descending order
    f = a;
    m = b;
    n = m[0];
  }
  
  public void makematrix(){
    G = new boolean[n][n];
    Stack<Integer> l;
    int k;
    for(int j = 0; j < n; j++){
      for(int i = 0; i < f.length; i++){
        l = new Stack<Integer>();
        l.push(j+f[i]);
        while(!l.empty()){
          k = l.pop();
          if(k<n){
            G[k][j] = G[k][j] ^ true;
          } else {
            for(int i1 = 1; i1 < m.length; i1++){
              l.push(k-n+m[i1]);
            }
          }
        }
      }
    }
  }
  
  public void PLUdecomposition(){
    L = new boolean[n][n];
    U = new boolean[n][n];
    P = new int[n];
    for(int i = 0; i < n; i++){
      L[i][i] = true;
      P[i] = i;
      for(int j = 0; j < n; j++){
        if(G[i][j]){
          U[i][j] = G[i][j];
        }
      }
    }
    for(int i = 0; i < n-1; i++){
      if(!U[i][i]){ //swap if Uii = 0
        int k = i;
        while(!U[k][i]){
          k++;
        }
        int t1 = P[k];
        P[k] = P[i];
        P[i] = t1;
        boolean t2;
        for(int j = 0; j < n; j++){
          if(j<i){
            t2 = L[k][j];
            L[k][j]=L[i][j];
            L[i][j] = t2;
          } else {
            t2 = U[k][j];
            U[k][j]=U[i][j];
            U[i][j] = t2;
          }
        }
      }
      
      for(int k = i+1; k < n; k++){
        if(U[k][i]){
          for(int j = i; j < n; j++){
            if(j==i){
              L[k][j] = L[k][j] ^ L[i][j];
            }
            U[k][j] = U[k][j] ^U[i][j];
          }
        }
      }
    }
  }
  
  public int[] depthCalculator(){
    int depth = 0;
    int count = 0;
    ArrayList<Integer> lastr = new ArrayList<Integer>();
    ArrayList<Integer> lastc = new ArrayList<Integer>();
    for (int i = 0; i < n; i++){
      for (int j = i+1; j < n; j++){
        if(U[i][j]){
          count++;
          if (lastr.isEmpty()){
            depth++;
            lastr.add(i);
            lastc.add(j);
          } else if ((!lastr.contains(i)) & (!lastr.contains(j)) & (!lastc.contains(i)) & (!lastc.contains(j))){
            lastr.add(i);
            lastc.add(j);
          } else {
            depth++;
            lastr.clear();
            lastc.clear();
            lastr.add(i);
            lastc.add(j);
          }
        }
      }
    }
    for (int i = n-1; i >= 0; i--){
      for (int j = i-1; j >= 0; j--){
        if(L[i][j]){
          count++;
          if (lastr.isEmpty()){
            depth++;
            lastr.add(i);
            lastc.add(j);
          } else if ((!lastr.contains(i)) & (!lastr.contains(j)) & (!lastc.contains(i)) & (!lastc.contains(j))){
            lastr.add(i);
            lastc.add(j);
          } else {
            depth++;
            lastr.clear();
            lastc.clear();
            lastr.add(i);
            lastc.add(j);
          }
        }
      }
    }
    int[] r = new int[2];
    r[0] = depth;
    r[1] = count;
    return r;
  }
  
  
  
  
  
  
  
  
  public void printAllMatrix(){
    System.out.println("G:");
    System.out.print("{");
    for(int i = 0; i < n; i++){
      System.out.print("{");
      for(int j = 0; j < n; j++){
        if(G[i][j]){
          System.out.print(1);
        } else {
          System.out.print(0);
        } if(j<n-1){
          System.out.print(",");
        } else {
          System.out.print("}");
        }
      } if(i<n-1){
        System.out.println(",");
      } else {
        System.out.println("}");
      }
    }
    System.out.println("L:");
    System.out.print("{");
    for(int i = 0; i < n; i++){
      System.out.print("{");
      for(int j = 0; j < n; j++){
        if(L[i][j]){
          System.out.print(1);
        } else {
          System.out.print(0);
        } if(j<n-1){
          System.out.print(",");
        } else {
          System.out.print("}");
        }
      } if(i<n-1){
        System.out.println(",");
      } else {
        System.out.println("}");
      }
    }
    System.out.println("U:");
    System.out.print("{");
    for(int i = 0; i < n; i++){
      System.out.print("{");
      for(int j = 0; j < n; j++){
        if(U[i][j]){
          System.out.print(1);
        } else {
          System.out.print(0);
        } if(j<n-1){
          System.out.print(",");
        } else {
          System.out.print("}");
        }
      } if(i<n-1){
        System.out.println(",");
      } else {
        System.out.println("}");
      }
    }
    System.out.print("P:");
    for(int i = 0; i < n; i++){
      System.out.print(" " + (P[i]+1));
    }
    System.out.println();
  }
  
  public void printOneMatrix(int k){
    if (k==0){
      System.out.println("G:");
    System.out.print("{");
    for(int i = 0; i < n; i++){
      System.out.print("{");
      for(int j = 0; j < n; j++){
        if(G[i][j]){
          System.out.print(1);
        } else {
          System.out.print(0);
        } if(j<n-1){
          System.out.print(",");
        } else {
          System.out.print("}");
        }
      } if(i<n-1){
        System.out.println(",");
      } else {
        System.out.println("}");
      }
    }} else if (k==1) {
    System.out.println("L:");
    System.out.print("{");
    for(int i = 0; i < n; i++){
      System.out.print("{");
      for(int j = 0; j < n; j++){
        if(L[i][j]){
          System.out.print(1);
        } else {
          System.out.print(0);
        } if(j<n-1){
          System.out.print(",");
        } else {
          System.out.print("}");
        }
      } if(i<n-1){
        System.out.println(",");
      } else {
        System.out.println("}");
      }
    }} else if (k==2) {
    System.out.println("U:");
    System.out.print("{");
    for(int i = 0; i < n; i++){
      System.out.print("{");
      for(int j = 0; j < n; j++){
        if(U[i][j]){
          System.out.print(1);
        } else {
          System.out.print(0);
        } if(j<n-1){
          System.out.print(",");
        } else {
          System.out.print("}");
        }
      } if(i<n-1){
        System.out.println(",");
      } else {
        System.out.println("}");
      }
    }} else {
    System.out.print("P:");
    for(int i = 0; i < n; i++){
      System.out.print(" " + (P[i]+1));
    }
    System.out.println();
    }
  }
}