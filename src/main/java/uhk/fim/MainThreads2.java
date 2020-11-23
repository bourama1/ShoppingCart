package uhk.fim;

public class MainThreads2 {

    static int counter = 0;

    public static void main(String[] args) {
        CounterThread thread1 = new CounterThread();
        CounterThread thread2 = new CounterThread();
        thread1.start();
        thread2.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(counter);
    }
}

class CounterThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            synchronized (MainThreads2.class) {
                MainThreads2.counter++;
            }
        }
    }
}
