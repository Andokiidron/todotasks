import java.util.Timer;
import java.util.TimerTask;

public class ReminderTask extends TimerTask {

    private String name;

    public ReminderTask(int ix, String name) {
        this.name = name;

        Timer timer = new Timer(true);
        int time = getTime(ix);
        if (time != -1) {
            timer.scheduleAtFixedRate(this, time, time);
        }
    }

    @Override
    public void run() {
        new ReminderPopup(name);
    }

    public int getTime(int ix) {
        switch(ix) {
            case 0: return 10 * 1000;
            case 1: return 10 * 60 * 1000;
            case 2: return 30 * 60 * 1000;
            case 3: return 120 * 60 * 1000;
            default: return -1;
        }
    }
}
