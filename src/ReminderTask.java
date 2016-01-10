import java.util.Timer;
import java.util.TimerTask;

public class ReminderTask extends TimerTask {

    private String name;

    public ReminderTask(int ix, String name) {
        this.name = name;

        Timer timer = new Timer(true);
        int time = getTime(ix);
        timer.scheduleAtFixedRate(this, time, time);
    }

    public int getTime(int ix) {
        switch (ix) {
            case 0:
                return 30 * 1000;
            case 1:
                return 24 * 60 * 60 * 1000;
            case 2:
                return 7 * 24 * 60 * 60 * 1000;
            case 3:
                return 2 * 7 * 24 * 60 * 60 * 1000;
            default:
                return -1;
        }
    }

    @Override
    public void run() {
        new ReminderPopup(name);
    }
}