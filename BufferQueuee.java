import java.util.concurrent.Semaphore;

public class BufferQueuee {
    private String buffer[];
    private int bufferSize;
    private int in = 0;
    private int out = 0;
    private int count = 0;
    private boolean isLimit = true;
    private static Semaphore mutex = new Semaphore(1);

    public BufferQueuee(int initSize, boolean isLimit) {
        bufferSize = initSize;
        buffer = new String[initSize];
        this.isLimit = isLimit;

    }

    public void send_msg(String msg) {
        if (!isLimit) {
            String temp[] = new String[bufferSize + 10];
            for (int i = 0; i < bufferSize; i++) {
                temp[i] = buffer[i];
            }
            try {
                mutex.acquire();
                bufferSize = bufferSize + 10;
                buffer = temp;
                mutex.release();
            } catch (InterruptedException e) {
            }

        }
        while (count == bufferSize)
            ;
        buffer[in] = msg;
        try {
            mutex.acquire();
            in = (in + 1) % bufferSize;
            count++;
            mutex.release();
        } catch (InterruptedException e) {
        }
    }

    public String get_msg() {
        while (count == 0)
            ;
        String msg = buffer[out];
        try {
            mutex.acquire();
            out = (out + 1) % bufferSize;
            count--;
            mutex.release();
        } catch (InterruptedException e) {

        }

        return msg;
    }

    public String get_msgnb() {
        String msg = buffer[out];
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
