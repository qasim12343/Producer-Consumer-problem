public class Sender extends Thread {
    int m;

    public Sender(int m) {
        this.m = m;
    }

    @Override
    public void run() {
        System.out.println("thread" + m);
    }
}
