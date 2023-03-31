package io.mochis.javagems;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class AcquireLockBenchmark {

  private static UnsyncCounter unsyncCounter;
  private static SyncCounter syncCounter;
  private static SyncCounterWithLock syncCounterWithLock;

  public AcquireLockBenchmark() {
    unsyncCounter = new UnsyncCounter();
    syncCounter = new SyncCounter();
    syncCounterWithLock = new SyncCounterWithLock();
  }

  public static void main(String[] args) throws RunnerException {
    Options opts = new OptionsBuilder()
        .include(AcquireLockBenchmark.class.getSimpleName())
        .warmupIterations(1)
        .warmupTime(TimeValue.seconds(10))
        .measurementIterations(3)
        .forks(1)
        .build();
    new Runner(opts).run();
  }

  @Benchmark
  public long enterUnsyncMethod() {
    return unsyncCounter.inc();
  }

  @Benchmark
  public long syncInc() {
    return syncCounter.inc();
  }

  @Benchmark
  public long syncIncWithLock() {
    return syncCounterWithLock.inc();
  }

  private static class SyncCounter {
    private int counter;
    SyncCounter() {
      counter = 0;
    }
    public synchronized long inc() {
      return ++counter;
    }
  }

  private static class UnsyncCounter {
    private int counter;
    UnsyncCounter() {
      counter = 0;
    }
    public long inc() {
      return ++counter;
    }
  }

  private static class SyncCounterWithLock {
    private final Lock lock = new ReentrantLock();
    private int counter;

    SyncCounterWithLock() {
      counter = 0;
    }

    public long inc() {
      lock.lock();
      try {
        return ++counter;
      } finally {
        lock.unlock();
      }
    }
  }

}
