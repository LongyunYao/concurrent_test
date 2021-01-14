package concurrent;

public class Main {
    public static void main(String[] args) {
        ThreadLocal<Object> obj = new ThreadLocal<>();
        Runnable runnable = () -> {
            if (obj.get() == null) obj.set(new Object());
            System.out.println(System.identityHashCode(obj.get()));
        };
    }
}
