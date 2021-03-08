package concurrent;

public class SynchronizedTest {
    static private synchronized void test() {
        throw new IllegalMonitorStateException();
    }

    public static void main(String[] args) throws InterruptedException {
        test();
        int a = 1;
        Object lock = new Object();
        Runnable runnable = () -> {
            System.out.println(a);
            synchronized (lock) {
                int b;
                try {
                    System.out.println("Time stamp: " + System.currentTimeMillis()
                            + " " + Thread.currentThread().getName());
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.join();
        thread2.join();
        thread1.start();
        thread2.start();
    }
}
