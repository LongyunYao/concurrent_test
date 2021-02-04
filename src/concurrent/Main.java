package concurrent;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ThreadLocal<Object> obj = new ThreadLocal<>();
        // 线程执行实体函数
        Runnable runnable = () -> {
            if (obj.get() == null) obj.set(new Object());
            System.out.println(System.identityHashCode(obj.get()));
            // 子线程中继续创建子线程
            Thread thread = new Thread(() -> {
                //for (int i = 0; i < 20000; System.out.println(Thread.currentThread().getName() + (i++)));
                System.out.println(Thread.currentThread().getThreadGroup().getName());
            });
            thread.setName("sub" + Thread.currentThread().getName());
            thread.setDaemon(false);
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            thread.start();
            System.out.println(Thread.currentThread().getThreadGroup().getName());
            //for (int i = 0; i < 10000; System.out.println(Thread.currentThread().getName() + (i++)));
        };

        // Thread方式直接进行多线程的示例创建
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        Thread thread4 = new Thread(runnable);
        Thread thread5 = new Thread(runnable);
        Thread thread6 = new Thread(runnable);
        Thread thread7 = new Thread(runnable);
        Thread thread8 = new Thread(runnable);
        System.out.println(new Thread("nullThread").getThreadGroup().getName());

        thread3.setDaemon(true);
        thread4.setDaemon(true);

        thread1.start();thread2.start();thread3.start();thread4.start();thread5.start();thread6.start();
        thread7.start();thread8.start();

        thread1.join();thread2.join();thread3.join();thread4.join();thread5.join();thread6.join();
        thread7.join();thread8.join();
    }
}
