public class IncrementGidney{
  Quantum q;
  
  public IncrementGidney(Quantum a){
    q = a;
  }
  
  public void Increment(int k, int v, int g, byte[] d){ //increment the number stored in d[v],...,d[v+k-1] using the qubits in d[g],...,d[g+k-1] without altering or knowing the state of the b's
    int n = k-1;
    for(int i = 0; i <= n; i++){
      q.CNOT(g, v+i, d);
    }
    
    for(int i = 1; i<=n; i++){
      q.NOT(g+i, d);
    }
    q.NOT(v+n, d);
    
    for(int i = 0; i < n; i++){
      q.CNOT(g+i, v+i, d);
      q.CNOT(g+i+1, g+i, d);
      q.TOF(g+i,v+i,g+i+1,d);
    }
    
    q.CNOT(g+n, v+n, d);
    
    for(int i = n-1; i >= 0; i--){
      q.TOF(g+i,v+i,g+i+1,d);
      q.CNOT(g+i+1, g+i, d);
      q.CNOT(g+i+1, v+i, d);
    }
    
    for(int i = 1; i<=n; i++){
      q.NOT(g+i, d);
    }
    
    for(int i = 0; i < n; i++){
      q.CNOT(g+i, v+i, d);
      q.CNOT(g+i+1, g+i, d);
      q.TOF(g+i,v+i,g+i+1,d);
    }
    
    q.CNOT(g+n, v+n, d);
    
    for(int i = n-1; i >= 0; i--){
      q.TOF(g+i,v+i,g+i+1,d);
      q.CNOT(g+i+1, g+i, d);
      q.CNOT(g+i+1, v+i, d);
    }
    
    for(int i = 0; i <= n; i++){
      q.CNOT(g, v+i, d);
    }
  }
  
  public void ConIncrement(int k, int c, int v, int g, int an, byte[] d){ //increment the number stored in d[a],...,d[a+k-1] using the qubits in d[b],...,d[b+k-1] without altering or knowing the state of the b's controlled by d[c] while using 1 ancillary qubit d[an]
    int n = k-1;
    for(int i = 0; i <= n; i++){
      q.TOF(c, g, v+i, d);
    }
    
    for(int i = 1; i<=n; i++){
      q.CNOT(c, g+i, d);
    }
    q.CNOT(c, v+n, d);
    
    for(int i = 0; i < n; i++){
      q.TOF(c, g+i, v+i, d);
      q.TOF(c, g+i+1, g+i, d);
      CTOF(c, g+i,v+i,g+i+1, an, d);
    }
    
    q.TOF(c, g+n, v+n, d);
    
    for(int i = n-1; i >= 0; i--){
      CTOF(c, g+i,v+i,g+i+1, an, d);
      q.TOF(c, g+i+1, g+i, d);
      q.TOF(c, g+i+1, v+i, d);
    }
    
    for(int i = 1; i<=n; i++){
      q.CNOT(c, g+i, d);
    }
    
    for(int i = 0; i < n; i++){
      q.TOF(c, g+i, v+i, d);
      q.TOF(c, g+i+1, g+i, d);
      CTOF(c, g+i,v+i,g+i+1, an, d);
    }
    
    q.TOF(c, g+n, v+n, d);
    
    for(int i = n-1; i >= 0; i--){
      CTOF(c, g+i,v+i,g+i+1, an, d);
      q.TOF(c, g+i+1, g+i, d);
      q.TOF(c, g+i+1, v+i, d);
    }
    
    for(int i = 0; i <= n; i++){
      q.TOF(c, g, v+i, d);
    }
  }
  
  public void Decrease(int k, int v, int g, byte[] d){
    int n = k-1;
    for(int i = 0; i <= n; i++){
      q.CNOT(g, v+i, d);
    }
    
    for(int i = 0; i < n; i++){
      q.CNOT(g+i+1, v+i, d);
      q.CNOT(g+i+1, g+i, d);
      q.TOF(g+i,v+i,g+i+1,d);
    }
    
    q.CNOT(g+n, v+n, d);
    
    for(int i = n-1; i >= 0; i--){
      q.TOF(g+i,v+i,g+i+1,d);
      q.CNOT(g+i+1, g+i, d);
      q.CNOT(g+i, v+i, d);
    }
    
    for(int i = 1; i<=n; i++){
      q.NOT(g+i, d);
    }
    
    for(int i = 0; i < n; i++){
      q.CNOT(g+i+1, v+i, d);
      q.CNOT(g+i+1, g+i, d);
      q.TOF(g+i,v+i,g+i+1,d);
    }
    
    q.CNOT(g+n, v+n, d);
    
    for(int i = n-1; i >= 0; i--){
      q.TOF(g+i,v+i,g+i+1,d);
      q.CNOT(g+i+1, g+i, d);
      q.CNOT(g+i, v+i, d);
    }
    q.NOT(v+n, d);
    
    for(int i = 1; i<=n; i++){
      q.NOT(g+i, d);
    }
    for(int i = 0; i <= n; i++){
      q.CNOT(g, v+i, d);
    }
  }
  
  public void ConDecrease(int k, int c, int v, int g, int an, byte[] d){ //increment the number stored in d[a],...,d[a+k-1] using the qubits in d[b],...,d[b+k-1] without altering or knowing the state of the b's controlled by d[c] while using 1 ancillary qubit d[an]
    int n = k-1;
    for(int i = 0; i <= n; i++){
      q.TOF(c, g, v+i, d);
    }
    
    for(int i = 0; i < n; i++){
      q.TOF(c, g+i+1, v+i, d);
      q.TOF(c, g+i+1, g+i, d);
      CTOF(c, g+i,v+i,g+i+1, an,d);
    }
    
    q.TOF(c, g+n, v+n, d);
    
    for(int i = n-1; i >= 0; i--){
      CTOF(c, g+i,v+i,g+i+1,an,d);
      q.TOF(c, g+i+1, g+i, d);
      q.TOF(c, g+i, v+i, d);
    }
    
    for(int i = 1; i<=n; i++){
      q.CNOT(c, g+i, d);
    }
    
    for(int i = 0; i < n; i++){
      q.TOF(c, g+i+1, v+i, d);
      q.TOF(c, g+i+1, g+i, d);
      CTOF(c, g+i,v+i,g+i+1, an,d);
    }
    
    q.TOF(c,g+n, v+n, d);
    
    for(int i = n-1; i >= 0; i--){
      CTOF(c, g+i,v+i,g+i+1, an,d);
      q.TOF(c, g+i+1, g+i, d);
      q.TOF(c, g+i, v+i, d);
    }
    q.CNOT(c,v+n, d);
    
    for(int i = 1; i<=n; i++){
      q.CNOT(c, g+i, d);
    }
    for(int i = 0; i <= n; i++){
      q.TOF(c, g, v+i, d);
    }
  }

  public void CTOF(int con, int a, int b, int c, int an, byte[] d){ //controlled TOF by d[con] using ancillary qubit d[an] = 0
    q.TOF(a, b, an, d);
    q.TOF(an, con, c, d);
    q.TOF(a, b, an, d);
  }
}