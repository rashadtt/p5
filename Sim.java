import java.io.*;
import java.util.*;

public class Sim {

  public static void run_sim(MBTA mbta, Log log) {
    List<Thread> passengerThreads = new ArrayList<>();
    List<Thread> trainThreads = new ArrayList<>();

    for (Passenger passenger : mbta.trips.keySet()) {
      passenger.mbta = mbta;
      passenger.log = log;
      Thread thread = new Thread(passenger);
      passengerThreads.add(thread);
    }

    for (Train train : mbta.lines.keySet()) {
      train.mbta = mbta;
      train.log = log;
      Thread thread = new Thread(train);
      trainThreads.add(thread);
    }

    for (Thread passengerThread : passengerThreads) {
      passengerThread.start();
    }

    for (Thread trainThread : trainThreads) {
      trainThread.start();
    }

    try {
      for (Thread passengerThread : passengerThreads) {
        passengerThread.join();
      }

      for (Thread trainTread : trainThreads){
        trainTread.join();
      }
    } catch (Exception e){
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("usage: ./sim <config file>");
      System.exit(1);
    }

    MBTA mbta = new MBTA();
    mbta.loadConfig(args[0]);

    Log log = new Log();

    run_sim(mbta, log);

    String s = new LogJson(log).toJson();
    PrintWriter out = new PrintWriter("log.json");
    out.print(s);
    out.close();

    mbta.reset();
    mbta.loadConfig(args[0]);
    Verify.verify(mbta, log);
  }
}
