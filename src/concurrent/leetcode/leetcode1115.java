package concurrent.leetcode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 交替打印FooBar
 */
public class leetcode1115 {
    public static void main(String[] args) throws InterruptedException {
        FooBar fooBar = new FooBar(3);
        Runnable runnable = () -> System.out.println(Thread.currentThread().getStackTrace()[2].getMethodName());
        Thread thread1 = new Thread(() -> {
            try {
                fooBar.foo(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                fooBar.bar(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.join();
        thread2.join();
        thread1.start();
        thread2.start();
    }
}

/**
 * 乐观锁（自旋锁）版本
 */

class FooBar1 {
    private final int n;
    private boolean flag; // false表示foo, true表示bar

    public FooBar1(int n) {
        this.n = n;
        flag = false;
    }

    public void foo(Runnable printFoo) {
        for (int i = 0; i < n; i++) {
            while (flag) Thread.yield(); // 自旋锁，注意需要释放CPU，否则可能导致超时；使用
            printFoo.run();
            flag = true; // 恢复自旋锁释放锁
        }
    }

    public void bar(Runnable printBar) {
        for (int i = 0; i < n; i++) {
            while (!flag) Thread.yield();
            printBar.run();
            flag = false;
        }
    }
}

/**
 * BlockingQueue版本
 */

class FooBar2 {
    private final int n;
    private final BlockingQueue<Integer> bar = new LinkedBlockingQueue<>(1); // 如果队列到达capacity，会阻塞等待
    private final BlockingQueue<Integer> foo = new LinkedBlockingQueue<>(1);

    public FooBar2(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            foo.put(i);
            printFoo.run();
            bar.put(i);
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            bar.take();
            printBar.run();
            foo.take();
        }
    }
}

//可重入锁 + Condition
class FooBar3 {
    private final int n;
    private final Lock lock = new ReentrantLock(true);
    private final Condition foo = lock.newCondition();
    private boolean flag; // false表示foo, true表示bar

    public FooBar3(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (flag) foo.await();
                printFoo.run();
                flag = true;
                foo.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (!flag) foo.await();
                printBar.run();
                flag = false;
                foo.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}

//CyclicBarrier 控制先后
class FooBar {
    private final int n;

    public FooBar(int n) {
        this.n = n;
    }

    CyclicBarrier cb = new CyclicBarrier(2);
    volatile boolean fin = true;

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (!fin) Thread.onSpinWait();
            printFoo.run();
            fin = false;
            try {
                cb.await();
            } catch (BrokenBarrierException ignored) {}
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            try {
                cb.await();
            } catch (BrokenBarrierException ignored) {}
            printBar.run();
            fin = true;
        }
    }
}