import java.util.concurrent.Semaphore;

public class BufferQueuee {
    private String buffer[];
    private int bufferSize;
    private int in = 0;
    private int out = 0;
    private int count = 0;
    private boolean isLimit = true;
    private Semaphore mutex = new Semaphore(1);
    private Semaphore full = new Semaphore(0);
    private Semaphore empty;

    public BufferQueuee(int initSize, boolean isLimit) {
        bufferSize = initSize;
        buffer = new String[initSize];
        this.isLimit = isLimit;
        empty = new Semaphore(10);

    }

    public void send_msg(String msg) {
        try {
            empty.acquire();
            mutex.acquire();
            buffer[in] = msg;
            in = (in + 1) % bufferSize;
            count++;
            mutex.release();
            full.release();
        } catch (InterruptedException e) {
        }
    }

    public String get_msg() {
        String msg = null;
        try {
            full.acquire();
            mutex.acquire();
            msg = buffer[out];
            out = (out + 1) % bufferSize;
            count--;
            mutex.release();
            empty.release();
        } catch (InterruptedException e) {

        }
        return msg;

    }

    public String get_msgnb() {
        String msg;
        msg = buffer[out];
        try {
            mutex.acquire();
            out = (out + 1) % bufferSize;
            count--;
            mutex.release();
        } catch (InterruptedException e) {

        }
        return msg;
    }

    public long[] stats() {
        long status[] = new long[4];
        status[0] = getbBufferWordsSize();
        status[1] = bufferSize;
        status[2] = count;
        status[3] = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
        return status;
    }

    long getbBufferWordsSize() {
        long totalSize = 0;
        for (int i = out; i <= count; i = (i + 1) % bufferSize) {
            long size = buffer[i].length();
            totalSize += size;
        }
        return totalSize;
    }
}
