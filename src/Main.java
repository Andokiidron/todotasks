import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Main
        extends Application {          // REF: http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI

    private ListView<Todo> listViewToDo;
    private ObservableList<Todo> toDoList;
    private TextField nameText;
    private ListView<String> listViewReminder;
    private ObservableList<String> reminderTimes;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {   //the main stage
        primaryStage.setTitle("Todo App");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setPadding(new Insets(25, 25, 25, 25));

        listViewToDo = new ListView<>();                    // REF: http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI
        listViewToDo.getSelectionModel().selectedIndexProperty().addListener(
                new ListSelectChangeListener());        //making new listener to save selection REF: http://stackoverflow.com/questions/13264017/getting-selected-element-from-listview
        toDoList = FXCollections.observableList(new ArrayList<>());
        listViewToDo.setItems(toDoList);
        listViewToDo.getSelectionModel().selectFirst();     // selects first from the list
        grid.add(listViewToDo, 1, 1);

        Label namelbl = new Label("Todo nimi:");
        nameText = new TextField();
        nameText.setMinHeight(30.0);
        nameText.setPromptText("Lisa ToDo (kohustuslik väli).");
        nameText.setPrefColumnCount(20);
        nameText.setTooltip(new Tooltip("Todo nimi (5 kuni 50 tähmärki)"));
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(namelbl, nameText);

        listViewReminder = new ListView<String>();              //REF: how to make a listview : https://docs.oracle.com/javafx/2/ui_controls/list-view.htm
        reminderTimes = FXCollections.observableArrayList("30 sekundit", "1 päev", "1 nädal", "2 nädalat");
        listViewReminder.setItems(reminderTimes);
        grid.add(listViewReminder, 2, 1);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(hbox);
        grid.add(vbox, 2, 0);

        Button newbtn = new Button("Uus");
        newbtn.setOnAction(new NewButtonListener());
        Button delbtn = new Button("Kustuta");
        delbtn.setOnAction(new DeleteButtonListener());
        HBox hbox2 = new HBox(10);
        hbox2.getChildren().addAll(newbtn, delbtn);
        grid.add(hbox2, 1, 2);

        Button savebtn = new Button("Salvesta");
        savebtn.setOnAction(new SaveButtonListener());
        AnchorPane saveBtnAnchor = new AnchorPane();
        AnchorPane.setRightAnchor(savebtn, 0.0);
        saveBtnAnchor.getChildren().add(savebtn);
        grid.add(saveBtnAnchor, 2, 2);

        Scene scene = new Scene(grid, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class ListSelectChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> ov,
                            Number old_val, Number new_val) {

            if ((new_val.intValue() < 0) || (new_val.intValue() >= toDoList.size())) {
                return;                                          // checks the listener value and if there are any todos in the list
            }

            Todo todo = toDoList.get(new_val.intValue());
            nameText.setText(todo.getName());
            listViewReminder.getSelectionModel().select(todo.getReminder());
        }
    }

    private class NewButtonListener implements EventHandler<ActionEvent> {                  // REF: http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI

        @Override
        public void handle(ActionEvent e) {

            Todo todo = new Todo("Uus ToDo", 0);

            ReminderTask todoTask = new ReminderTask(todo.getReminder(), todo.getName());
            todo.setTask(todoTask);

            int ix = 0;
            toDoList.add(ix, todo);
            listViewToDo.getSelectionModel().clearAndSelect(ix);
            nameText.clear();
            listViewReminder.getSelectionModel().clearSelection();
            nameText.setText("Uus ToDo");
            nameText.requestFocus();
        }
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {             // REF: http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI

        @Override
        public void handle(ActionEvent ae) {

            int ix = listViewToDo.getSelectionModel().getSelectedIndex();
            if (ix < 0) {
                return;
            }

            String s1 = nameText.getText();

            Todo todo = toDoList.get(ix);
            todo.setName(s1);

            int reminderIx = listViewReminder.getSelectionModel().getSelectedIndex();
            todo.setReminder(reminderIx);

            ReminderTask todoTask = todo.getTask();
            todoTask.cancel();

            todoTask = new ReminderTask(todo.getReminder(), todo.getName());
            todo.setTask(todoTask);

            toDoList.set(ix, null);
            toDoList.set(ix, todo);
            listViewToDo.getSelectionModel().clearAndSelect(ix);
            listViewToDo.requestFocus();
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent ae) {
            int ix = listViewToDo.getSelectionModel().getSelectedIndex();
            if (ix < 0) {
                return;
            }
            Todo todo = toDoList.get(ix);
            todo.getTask().cancel();
            toDoList.remove(ix);

            if (toDoList.size() == 0) {
                nameText.clear();
                return;
            }

            listViewToDo.getSelectionModel().clearAndSelect(ix);
            Todo itemSelected = toDoList.get(ix);                    // REF: http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI
            nameText.setText(itemSelected.getName());
            listViewToDo.requestFocus();
        }
    }
}
