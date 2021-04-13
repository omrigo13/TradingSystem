    package authentication;

    import org.junit.jupiter.api.Test;

    import java.util.HashMap;
    import java.util.Map;
    import java.util.concurrent.*;

    import static org.junit.jupiter.api.Assertions.assertThrows;

    public class RegisterConcurrencyTest {

        static class AlreadyRegisteredException extends Exception {}

        static class Register {
            private final Map<String, String> map;
            public Register(Map<String, String> map) {
                this.map = map;
            }
            public void register(String name, String password) throws AlreadyRegisteredException {
                if (map.containsKey(name))
                    throw new AlreadyRegisteredException();
                map.put(name, password);
            }
        }

        static class MockHashMap<K,V> extends HashMap<K,V> {
            private final CountDownLatch waitForRegister, waitForContainsKey;
            private final Thread main;
            public MockHashMap(CountDownLatch waitForRegister, CountDownLatch waitForContainsKey, Thread main) {
                this.waitForRegister = waitForRegister;
                this.waitForContainsKey = waitForContainsKey;
                this.main = main;
            }
            private void busyWaitForRegister() {
                try {
                    while (waitForRegister.await(1, TimeUnit.MILLISECONDS)) {
                        if (main.getState() == Thread.State.BLOCKED)
                            break;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public boolean containsKey(Object key) {
                boolean result = super.containsKey(key);
                if (!Thread.currentThread().getName().equals("main")) {
                    waitForContainsKey.countDown();
                    busyWaitForRegister();
                }
                return result;
            }
        }

        @Test
        void register() throws Throwable {

            ExecutorService exec = Executors.newSingleThreadExecutor();
            final CountDownLatch waitForRegister = new CountDownLatch(1);
            final CountDownLatch waitForContainsKey = new CountDownLatch(1);
            Map<String, String> map = new MockHashMap<>(waitForRegister, waitForContainsKey, Thread.currentThread());
            Register reg = new Register(map);
            Future<?> future = exec.submit(() -> {
                try {
                    reg.register("Bob", "123");
                } catch (AlreadyRegisteredException e) {
                    throw new RuntimeException(e);
                }
            });
            waitForContainsKey.await();
            assertThrows(AlreadyRegisteredException.class, () -> reg.register("Bob", "456"));
            waitForRegister.countDown();
            future.get();
        }
    }
