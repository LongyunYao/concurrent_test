package concurrent.alibaba;

public class pro1 {
    public static void main(String[] args) throws InterruptedException {
        // 12A34B56C
        PRINT print = new PRINT("12A34B");
        print.print();
    }
}

class PRINT {
    private int pos; // 每一步
    private final String input; // 输入的字符串
    private boolean flag; // 打印

    PRINT(String input) {
        this.input = input;
        this.pos = 0;
        this.flag = false;
    }

    public void print() {
        Thread thread1 = new Thread(() -> {
            while (pos <= input.length()) {
                while (!isNum()) { // 打印数字
                    Thread.onSpinWait();
                }
                if (!flag) {
                    System.out.println(Thread.currentThread().getName() + ":" + input.charAt(pos++));
                }
                if (isChar()) flag = true;
            }
        });
        Thread thread2 = new Thread(() -> {
            while (pos <= input.length()) {
                while (!(isChar())) // 打印字符
                    Thread.onSpinWait();
                if (flag) {
                    System.out.println(Thread.currentThread().getName() + ":" + input.charAt(pos++));
                }
                if (isNum()) flag = false;
            }
        });
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1.start();
        thread2.start();
    }

    private boolean isChar() {
        if (pos >= input.length()) return false;
        return (input.charAt(pos) >= 'A' && input.charAt(pos) <= 'Z')
                || (input.charAt(pos) >= 'a' && input.charAt(pos) <= 'z');
    }

    private boolean isNum() {
        if (pos >= input.length()) return false;
        return input.charAt(pos) >= '0' && input.charAt(pos) <= '9';
    }
}