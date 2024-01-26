import java.util.concurrent.Semaphore;

public class Main {
    private static String buffer[] = new String[10];
    private static int bufferSize = 10;
    private static int in = 0;
    private static int out = 0;
    private static int count = 0;
    private static Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) {
        String buffer[] = new String[bufferSize];

        Sender s = new Sender("msg0");
        Sender s1 = new Sender("msg1");
        Sender s2 = new Sender("msg2");
        Sender s3 = new Sender("msg3");

        Reciever r = new Reciever();
        Reciever r1 = new Reciever();
        Reciever r2 = new Reciever();
        Reciever r3 = new Reciever();
        s.start();
        s1.start();
        s2.start();
        s3.start();
        r.start();
        r1.start();
        r2.start();
        r3.start();

    }

    static class Sender extends Thread {

        String msg;

        public Sender(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            while (count == bufferSize)
                ;
            buffer[in] = msg;
            try {
                mutex.acquire();
                in = (in + 1) % bufferSize;
                count++;
                mutex.release();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
            }

            // try {
            // Thread.sleep(1000);
            // } catch (InterruptedException e) {
            // }
        }
    }

    static class Reciever extends Thread {
        String msg;

        @Override
        public void run() {
            while (count == 0)
                ;
            try {
                mutex.acquire();
                msg = buffer[out];
                System.out.println(msg);
                out = (out + 1) % bufferSize;
                count--;
                System.out.println(out);
                mutex.release();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
            }

            // try {
            // Thread.sleep(1000);
            // } catch (InterruptedException e) {
            // }

        }
    }
}