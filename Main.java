import java.util.concurrent.Semaphore;

public class Main {

    private static Semaphore mutex = new Semaphore(1);
    private static BufferQueuee SR = new BufferQueuee(10, true);
    static int c = 0;

    public static void main(String[] args) {
        Sender s = new Sender(1);
        Sender s2 = new Sender(1);
        Reciever r = new Reciever();
        s.start();
        r.start();
        s2.start();
    }

    static class Sender extends Thread {
        int number;

        public Sender(int number) {
            this.number = number;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    mutex.acquire();
                    SR.send_msg("msg" + c);
                    c++;
                    mutex.release();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

    static class Reciever extends Thread {
        String msg;

        @Override
        public void run() {
            while (true) {

                msg = SR.get_msg();
                System.out.println(msg);

            }
        }
    }

}