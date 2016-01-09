// REF:!! The hole GUI base http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI

public class Todo {

    private String name;
    private int reminder;
    private ReminderTask timerTask;

    public Todo(String s1, int reminder) {
        name = s1;
        this.reminder = reminder;
    }

    String getName() {
        return name;
    }

    void setName(String s) {
        name = s;
    }

    int getReminder() {
        return reminder;
    }

    void setReminder(int reminder) {
        this.reminder = reminder;
    }

    void setTask(ReminderTask task) {
        timerTask = task;
    }

    ReminderTask getTask() {
        return timerTask;
    }

    @Override
    public String toString() { //lets give the todo name to listview on the left
        return name;
    }
}