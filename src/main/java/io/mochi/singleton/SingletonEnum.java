package io.mochi.singleton;

/**
 * We can create singletons with enums because they are syntactic sugar
 * for extending the class {@link Enum} and declaring several constants.
 * More <a href="https://stackoverflow.com/a/32354397">here</a>.
 * <p></p>
 * As class initialization guarantee sequential execution this way of creating
 * singletons is thread-safe, but in my opinion is weird to use enum this way.
 */
public enum SingletonEnum {
  INSTANCE;
}
