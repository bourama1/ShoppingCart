package uhk.fim;

import java.text.DateFormat;
import java.util.GregorianCalendar;

public class MainThreads {
    public static void main(String[] args) {

        ClockThread thread = new ClockThread();
        thread.start();

        String[] names = {"Pepa", "Honza", "Jana", "Tereza"};
        for (String name : names) {
            System.out.println(name);
        }
    }
}

class ClockThread extends Thread {
    @Override
    public void run() {
        while(true){
            try {
                // TODO: vysvětlit
                DateFormat fmt = DateFormat.getTimeInstance();
                System.out.println(fmt.format(GregorianCalendar.getInstance().getTime()));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}