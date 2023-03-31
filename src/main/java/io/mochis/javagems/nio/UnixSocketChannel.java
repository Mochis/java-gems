package io.mochis.javagems.nio;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class UnixSocketChannel {

  public static void main(String[] args) throws IOException {
    Path unixSocket = Path
        .of(System.getProperty("user.dir"))
        .resolve("the.socket");
    ServerSocketChannel serverChannel = ServerSocketChannel
        .open(StandardProtocolFamily.UNIX);
    serverChannel.bind(UnixDomainSocketAddress.of(unixSocket));
    try (SocketChannel sc =
        SocketChannel.open(UnixDomainSocketAddress.of(unixSocket))) {
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      serverChannel.close();
      Files.deleteIfExists(unixSocket);
    }

  }
}
