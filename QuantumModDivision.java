import java.util.Random;
import java.lang.Math;

public class QuantumModDivision{
  boolean random = true;
  int n = 11;
  int[] m = {n, 2, 0};
  int logn = (int) (Math.log(n) / Math.log(2));
  
  public void run(){
    Quantum q1 = new Quantum();
    byte[] qbits1 = new byte[7*n+logn+8];
    if(random){
      Random r = new Random();
      for(int i = 0; i < n; i++){
        qbits1[i] = (byte) r.nextInt(2);
        qbits1[i+n] += qbits1[i];
      }
    } else {
      for(int i = 0; i< 2*n; i++){
        qbits1[i] = 1;
      }
    }
    int[] f = {n-n/2, 0};
    IncrementGidney ig = new IncrementGidney(q1);
    Karatsuba ka1 = new Karatsuba(q1);
    Constmult cm1 = new Constmult(f, m, q1);
    ModKaratsuba mk1 = new ModKaratsuba(q1, cm1, ka1);
    gcdDiv gcd = new gcdDiv(q1, mk1, ig);
    
    gcd.div(n, logn, m, 0, n, 2*n, 3*n, 4*n+1, 5*n+2, 6*n+3, 7*n+4, 7*n+logn+6, 7*n+logn+7, qbits1);
    
    boolean correct = (qbits1[2*n] == 1);
    for (int i = 1; correct && i<n; i++){
      if(qbits1[2*n+i]==1){
        correct = false;
      }
    }
    System.out.println("The answer gcdDiv gives is: " + correct);
    System.out.println("TOF: " + q1.TOFc + ", CNOT: " + q1.CNOTc + ", space: " + (qbits1.length));
    
    
    
    Quantum q2 = new Quantum();
    SquareMatrix sq = new SquareMatrix(m, q2);
    Karatsuba ka2 = new Karatsuba(q2);
    Constmult cm2 = new Constmult(f, m, q2);
    ModKaratsuba mk2 = new ModKaratsuba(q2, cm2, ka2);
    fltDiv flt = new fltDiv(q2, mk2, sq);
    
    byte[] qbits2 = new byte[n*(flt.getk(flt.getks(n))+3)];
    if(random){
      Random r = new Random();
      for(int i = 0; i < n; i++){
        qbits2[i] = (byte) r.nextInt(2);
        qbits2[i+n] += qbits2[i];
      }
    } else {
      for(int i = 0; i< 2*n; i++){
        qbits2[i] = 0;
      }
      qbits2[1] = 1;
      qbits2[n+1] = 1;
    }
    
    flt.div(n, m, 0, n, 2*n, 3*n, qbits2);
    
    correct = (qbits2[2*n] == 1);
    for (int i = 1; correct && i<n; i++){
      if(qbits2[2*n+i]==1){
        correct = false;
      }
    }
    System.out.println("The answer fltDiv gives is: " + correct);
    System.out.println("TOF: " + q2.TOFc + ", CNOT: " + q2.CNOTc + ", space: " + (qbits2.length));
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
  
  public void ppa(byte[] a, int s, int k){ //print part of an array
    for(int i = 0; i< k; i++){
      System.out.print(a[s+i]);
    }
    System.out.println();
  }
  
  public static void main(String[] args){
    QuantumModDivision M = new QuantumModDivision();
    M.run();
  }
}