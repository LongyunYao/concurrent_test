package concurrent;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "暂停自己");
            LockSupport.park(); //阻塞自己
            System.out.println(Thread.currentThread().getName() + "从park()中返回");
        }, "LockSupportThread");
        thread.start();

        Thread.sleep(2000);
        //充分运行线程LockSupportThread两秒钟后，中断该线程，
        //该线程能从park()方法返回
        // thread.interrupt();
        LockSupport.unpark(thread);
    }
}
