package sample;

public class Todo {


    private String name;
    private int reminder;
	
    public Todo () {
    }
		
    public Todo (String s1, int reminder) {


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
	
    @Override
    public String toString() {
	
        return name;
    }
}