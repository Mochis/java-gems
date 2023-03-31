package io.mochis.javagems;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * Benchmarking the difference of performance when using IO with directly with
 * off-heap buffers or with on-heap buffers.
 * The difference on performance relates to the need of copy the on-heap buffer
 * content in a temporary off-heap buffer before writing it to the socket.
 * See: {@link sun.nio.ch.IOUtil} read or write methods
 *
 * <p>
 * Results on Eclipse Temurin 19.0.2+7:
 * Benchmark                            Mode  Cnt   Score   Error  Units
 * IOWriteBenchmark.writeOffHeapBuffer  avgt    3  16,568 ± 0,457  ns/op
 * IOWriteBenchmark.writeOnHeapBuffer   avgt    3  62,691 ± 1,401  ns/op
 * <p>
 * <a href="https://www.evanjones.ca/java-bytebuffer-leak.html">Related memory leak.</a>
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class IOWriteBenchmark {
  private static final Path unixSocket = Path
      .of(System.getProperty("user.dir"))
      .resolve("devnull.socket");
  private static ByteBuffer heapByteBuffer;
  private static ByteBuffer offHeapByteBuffer;
  private static ServerSocketChannel serverChannel;
  private static SocketChannel nioChannel;
  
  public IOWriteBenchmark() {
    heapByteBuffer = ByteBuffer.allocate(10)
        .put("vamos!!".getBytes());
    offHeapByteBuffer = ByteBuffer.allocateDirect(10)
        .put("vamos!!".getBytes());
    try {
      Files.deleteIfExists(unixSocket);
      serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
      serverChannel.bind(UnixDomainSocketAddress.of(unixSocket));
      nioChannel = SocketChannel.open(UnixDomainSocketAddress.of(unixSocket));
      nioChannel.configureBlocking(false);
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  public static void main(String[] args) throws RunnerException {
    Options opts = new OptionsBuilder()
        .include(IOWriteBenchmark.class.getSimpleName())
        .warmupIterations(1)
        .warmupTime(TimeValue.seconds(10))
        .measurementIterations(3)
        .forks(1)
        .build();
    new Runner(opts).run();
  }

  @Benchmark
  public int writeOnHeapBuffer() throws IOException {
      return nioChannel.write(heapByteBuffer);
  }

  @Benchmark
  public int writeOffHeapBuffer() throws IOException {
      return nioChannel.write(offHeapByteBuffer);
  }

  @TearDown(Level.Trial)
  public void tearDown() throws IOException {
    Files.deleteIfExists(unixSocket);
  }
}
