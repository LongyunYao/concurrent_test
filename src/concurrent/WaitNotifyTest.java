package concurrent;

import java.util.concurrent.ThreadLocalRandom;

public class WaitNotifyTest {
    public static void main(String[] args) {
        Data data = new Data();
        Thread sender = new Thread(new Sender(data), "sender");
        Thread receiver = new Thread(new Receiver(data), "receiver");

        sender.start();
        receiver.start();
    }
}

class Data {
    private String packet;

    // True if receiver should wait
    // False if sender should wait
    private boolean transfer = true;

    public synchronized void send(String packet) {
        while (!transfer) {
            try {
                this.wait();
            } catch (InterruptedException e)  {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted");
                e.printStackTrace();
            }
        }
        transfer = false;

        this.packet = packet;
        this.notifyAll();
    }

    public synchronized String receive() {
        while (transfer) {
            try {
                this.wait();
            } catch (InterruptedException e)  {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted");
                e.printStackTrace();
            }
        }
        transfer = true;

        this.notifyAll();
        return packet;
    }
}

class Sender implements Runnable {
    private Data data;

    public Sender(Data data) {
        this.data = data;
    }

    // standard constructors

    public void run() {
        String[] packets = {
                "First packet",
                "Second packet",
                "Third packet",
                "Fourth packet",
                "End"
        };

        for (String packet : packets) {
            data.send(packet);
            for (int j = 0; j < Integer.MAX_VALUE; j++) {
                for (int i = 0; i < Integer.MAX_VALUE; i++);
            }

            // Thread.sleep() to mimic heavy server-side processing
            /*try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e)  {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted");
                e.printStackTrace();
            }*/
        }
    }
}

class Receiver implements Runnable {
    private Data load;

    public Receiver(Data data) {
        this.load = data;
    }

    // standard constructors

    public void run() {
        for(String receivedMessage = load.receive();
            !"End".equals(receivedMessage);
            receivedMessage = load.receive()) {

            System.out.println(receivedMessage);

            // ...
            /*try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted");
                e.printStackTrace();
            }*/
        }
    }
}