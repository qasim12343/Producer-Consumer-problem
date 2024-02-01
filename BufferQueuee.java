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
        empty = new Semaphore(initSize);

    }

    public void send_msg(String msg) {
        if (!isLimit && count == bufferSize) {
            try {
                empty.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int newSize = bufferSize / 2 + bufferSize;
            String temp[] = new String[newSize];
            for (int i = 0; i < bufferSize; i++) {
                temp[i] = buffer[i];
            }
            bufferSize = newSize;
            buffer = temp;
            empty.release();
        } else if (count == bufferSize)
            return;

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
        String msg = null;
        if (count == 0)
            return null;
        try {
            // full.acquire();
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

    public long[] stats() {
        long status[] = new long[3];
        status[0] = getbBufferWordsSize();
        status[1] = count;
        status[2] = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
        return status;
    }

    long getbBufferWordsSize() {
        long totalSize = 0;
        int outtemp = out;
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < count; i++) {
            long size = buffer[outtemp].length();
            outtemp = (outtemp + 1) % bufferSize;
            totalSize += size;
        }
        mutex.release();
        return totalSize;
    }
}
