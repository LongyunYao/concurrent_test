package concurrent.leetcode;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1114. 按序打印
 */
public class leetcode1114 {
}

class Foo {

    int count;

    public Foo() {
        count = 0;
    }

    public void first(Runnable printFirst) throws InterruptedException {
        while (count != 0) {
        }
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        count = 1;
    }

    public void second(Runnable printSecond) throws InterruptedException {
        while (count != 1) {
        }
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        count = 2;
    }

    public void third(Runnable printThird) throws InterruptedException {
        while (count != 2) {
        }
        // printThird.run() outputs "second". Do not change or remove this line.
        printThird.run();
        count = 0;
    }
}