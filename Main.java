public class Main {
    public static void main(String[] args) {
        Sender s1 = new Sender(0);
        Reciever r1 = new Reciever(0);
        Sender s2 = new Sender(1);
        Reciever r2 = new Reciever(1);
        s1.start();
        r1.start();
        s2.start();
        r2.start();
        System.out.println("Hello");
    }
}