package concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TheadRunTimeTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadLocal<Object> obj = new ThreadLocal<>();
        Runnable runnable = () -> {
            if (obj.get() == null) obj.set(new Object());
        };
        System.out.println("multiple thread cost: " + new TestThread().testMultiTaskByThread(runnable));
        System.out.println("thread pool cost: " + new TestThreadPool().testMultiTaskByThreadPool(runnable));
        System.out.println("ForkJoinTask cost: " + new TestForkJoinTask().testMultiTaskByForkJoinPool(runnable));
        System.out.println("Stream.parallel cost: " + new TestStreamParallel().testStreamParallel(runnable));
    }
}

class TestThread {
    long testMultiTaskByThread(Runnable runnable) throws InterruptedException {
        long current = System.currentTimeMillis();
        for (int i = 0; i < 100_000; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            thread.join();
        }
        return System.currentTimeMillis() - current;
    }
}

class TestThreadPool {
    long testMultiTaskByThreadPool(Runnable runnable) {
        ExecutorService executor = Executors.newFixedThreadPool(64);
        long current = System.currentTimeMillis();
        for (int i = 0; i < 100_000; i++) {
            executor.execute(runnable);
        }
        executor.shutdownNow();
        return System.currentTimeMillis() - current;
    }
}

class TestForkJoinTask {
    long testMultiTaskByForkJoinPool(Runnable runnable) {
        Executor executor = Executors.newWorkStealingPool();
        long current = System.currentTimeMillis();
        for (int i = 0; i < 100_000; i++) {
            executor.execute(runnable);
        }
        return System.currentTimeMillis() - current;
    }
}

class TestStreamParallel {
    long testStreamParallel(Runnable runnable) {
        long current = System.currentTimeMillis();
        List<Integer> countList = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            countList.add(i);
        }
        countList.parallelStream().forEach(i -> runnable.run());
        return System.currentTimeMillis() - current;
    }
}