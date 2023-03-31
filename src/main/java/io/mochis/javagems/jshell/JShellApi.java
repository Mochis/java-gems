package io.mochis.javagems.jshell;

import jdk.jshell.JShell;

/**
 * Example of use of JShell API to execute a simple product
 */
public class JShellApi {

  public static void main(String[] args) {
    try(JShell jShell = JShell.create()) {
      System.out.print(jShell.eval("2 * 2").get(0).value());
    }
  }
}
