import java.util.concurrent.Semaphore;

public class Main {
    private static Semaphore mutex = new Semaphore(1);
    private static BufferQueuee SR = new BufferQueuee(20, true);
    static int c = 0;

    public static void main(String[] args) {
        Sender s = new Sender(1);
        Sender s2 = new Sender(2);
        Reciever r = new Reciever(1);
        Sender s3 = new Sender(3);
        Sender s4 = new Sender(4);
        // Reciever r1 = new Reciever(2);
        // Reciever r2 = new Reciever(3);
        // Reciever r3 = new Reciever(4);
        s.start();
        s4.start();
        s2.start();
        s3.start();
        r.start();
        // r1.start();
        // r2.start();
        // r3.start();
        // s2.start();
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
                    SR.send_msg("msg" + c + " S" + number);// PRODUCE
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
        int number;

        public Reciever(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            while (true) {
                long stats[] = SR.stats();
                msg = SR.get_msgnb();
                System.out
                        .println(msg + " R" + number + "\tmessages_length:" + stats[0] + "\tnumber_messages:" + stats[1]
                                + "\tmemory usage:" + stats[2]); // CONSUME
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

}