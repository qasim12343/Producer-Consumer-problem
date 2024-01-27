import java.util.concurrent.Semaphore;

public class Main {

    private static Semaphore mutex = new Semaphore(1);
    private static BufferQueuee SR = new BufferQueuee(10, true);

    public static void main(String[] args) {
        Sender s = new Sender();
        Reciever r = new Reciever();
        s.start();
        r.start();
    }

    static class Sender extends Thread {

        @Override
        public void run() {
            int c = 0;
            while (true) {
                SR.send_msg("msg" + c);
                c++;
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
            while (true) {

                msg = SR.get_msgnb();
                System.out.println(msg);

            }

            // try {
            // Thread.sleep(1000);
            // } catch (InterruptedException e) {
            // }

        }
    }

}