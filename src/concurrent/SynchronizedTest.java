package concurrent;

public class SynchronizedTest {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Runnable runnable = () -> {
            synchronized (lock) {
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
