package io.mochis.javagems.nio;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileChannelToDevNull {

  public static void main(String[] args) throws IOException {
    FileChannel fc =
        FileChannel.open(Path.of("/dev/null"), StandardOpenOption.WRITE);
  }
}
