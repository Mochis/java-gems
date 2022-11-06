package io.mochi.singleton;

/**
 * This is an example of lazy thread-safe singleton by using the pattern
 *  <a href="https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
 *    initialization-on-demand holder
 *  </a>.
 *  <p>
 *  The initialization of Holder class is lazy, that is, is not built until
 *  getInstance() is called in the enclosing class SingletonThreadSafe.
 *  <p></p>
 *  This implementation is thread-safe because class initialization is guaranteed
 *  to be sequential by the JLS, so when a second thread invokes getInstance(), will
 *  see a initialized version of INSTANCE.
 */
public class SingletonThreadSafe {

  private SingletonThreadSafe() {}

  private static class Holder {
    static SingletonThreadSafe INSTANCE = new SingletonThreadSafe();
  }

  public SingletonThreadSafe getInstance() {
    return Holder.INSTANCE;
  }

}
