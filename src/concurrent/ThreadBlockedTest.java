package concurrent;

public class ThreadBlockedTest {
    public static void main(String[] args) throws InterruptedException {
        Object LOCK = new Object();
        Thread thread1 = new Thread(() -> {
            synchronized (LOCK) {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.join();
        thread1.start();
        Thread thread2 = new Thread(() -> {
            synchronized (LOCK) {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.join();
        thread2.start();
    }
}
