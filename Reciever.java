
public class Reciever extends Thread {
    int n;

    public Reciever(int n) {
        this.n = n;
    }

    @Override
    public void run() {
        System.out.println("thread" + n);
    }
}
