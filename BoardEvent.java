import java.util.*;

public class BoardEvent implements Event {
  public final Passenger p; public final Train t; public final Station s;
  public BoardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof BoardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " boards " + t + " at " + s;
  }
  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }
  public void replayAndCheck(MBTA mbta) {
//    if (p.isOnTrain || !p.getCurrentStation().equals(s) || !t.getCurrentStation().equals(s) || p.nextTrain == null || !p.nextTrain.equals(t)){
//      throw new UnsupportedOperationException();
//    }
    if (p.isOnTrain){
      throw new NoSuchElementException();
    }
    if (!p.getCurrentStation().equals(s)){
      throw new UnsupportedOperationException();
    }
    if (!t.getCurrentStation().equals(s)){
      throw new NullPointerException();
    }
//    if (p.nextTrain == null){
//      throw new ArithmeticException();
//    }
    if (p.nextTrain != null && !p.nextTrain.equals(t)){
      throw new ArrayStoreException();
    }
    p.boardTrain(t);
  }
}
