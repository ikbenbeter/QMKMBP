import java.util.Random;

public class QMKMBP{ //quantum modulo karatsuba multiplier for binary polynomials
  //use random polynomials for multiplication if true
  boolean random = true;
  int n =1024; //size of field
  int[] m = {n, 333,135,73, 0}; //fixed field polynomial. primitive & descending order. 
  
  public void run(){
    //setup
    Quantum q = new Quantum();
    int k = n - n/2;
    int[] f = {k, 0};
    Karatsuba ka = new Karatsuba(q);
    Constmult cm = new Constmult(f, m, q);
    ModKaratsuba mk = new ModKaratsuba(q, cm, ka);
    byte[] a = {0,1,1,0,0,0,0,1,1,1}; //if random is set to false, it will multiply a by b
    byte[] b = {0,0,1,0,0,1,1,1,0,1};
      
    //setup qbits. qbits are a single array, where the first $n$ bits will by multiplied by the second $n$ bits and stored in the third $n$ bits
    if (random){
      a = new byte[n];
      b = new byte[n];
      Random r = new Random();
      for (int i =0; i < n; i++){
        a[i] = (byte) r.nextInt(2);
        b[i] = (byte) r.nextInt(2);
      }
    }
    byte[] qbits = new byte[3*n];
    for (int i = 0; i < n; i++){
      qbits[i] = a[i];
    }
    for (int i = 0; i < n; i++){
      qbits[n+i] = b[i];
    }
    byte[] checkbits = new byte[3*n];
    for (int i=0; i<3*n;i++){
      checkbits[i] = qbits[i];
    } 
    
    mk.modmult(n, m, 0, n, 2*n, qbits);
    
    check(qbits, checkbits);
    System.out.println("CNOT: " + q.CNOTc + ", TOF: " + q.TOFc + ", depth upper bound: " + q.depth);
    //System.out.println("Upper bound for depth per mult: " + cm.depth()[0] + ", CNOTs per mult: " + cm.depth()[1]); //was used for depth/CNOT count for table 1
  }
    
  public void check(byte[] left, byte[] right){ //calculate the correct answer for right and see if it matches left
    //calculate the correct answer
    byte[] l = new byte[n];
    for(int i = 0; i < n; i++){
      for(int j = 0; j < n; j++){
        if(right[i]*right[n+j] > 0){
          if(i+j < n){
            right[2*n+i+j] = (byte) ((right[2*n+i+j] + 1)%2);
          } else {
            l[i+j-n] = (byte) ((l[i+j-n] + 1)%2);
          }
        }
      }
    }
    for(int i = n-1; i >= 0; i--){
      if (l[i] > 0){
        for(int j = 1; j < m.length; j++){
          if(i+m[j]<n){
            right[2*n+i+m[j]] = (byte) ((right[2*n+i+m[j]] + 1)%2);
          } else {
            l[i+m[j]-n] = (byte) ((l[i+m[j]-n] + 1)%2);
          }
        }
      }
    }
    
    //check if right matches left
    boolean correct = true;
    for (int i = 0; (i < 3*n) & correct; i++){
      if(!(left[i] == right[i])){
        correct = false;
      }
    }
    if (!correct){
      System.out.println("wrong answer :(");
    } else {
      System.out.println("right answer :)");
    }
  }
  
  public void pa(byte[] a){ //print an array if necessary
    System.out.print("|");
    for(int i = 0; i < a.length; i++){
      System.out.print(a[i]);
      if ((i+1)%n==0){
        System.out.print("|");
      } else {
      System.out.print(",");
      }
    }
    System.out.println();
  }
  
  public static void main(String[] args){
    QMKMBP M = new QMKMBP();
    M.run();
  }
}