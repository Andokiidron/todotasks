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

    private ListView<Todo> listView;
    private ObservableList<Todo> toDoList;
    private TextField nametxt;
    private ListView<String> listView2;
    private ObservableList<String> items;

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

        listView = new ListView<>();                    // REF: http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI
        listView.getSelectionModel().selectedIndexProperty().addListener(
                new ListSelectChangeListener());        //making new listener to save selection REF: http://stackoverflow.com/questions/13264017/getting-selected-element-from-listview
        toDoList = FXCollections.observableList(new ArrayList<>());
        listView.setItems(toDoList);
        listView.getSelectionModel().selectFirst();     // selects first from the list
        grid.add(listView, 1, 1);

        Label namelbl = new Label("Todo nimi:");
        nametxt = new TextField();
        nametxt.setMinHeight(30.0);
        nametxt.setPromptText("Lisa ToDo (kohustuslik väli).");
        nametxt.setPrefColumnCount(20);
        nametxt.setTooltip(new Tooltip("Todo nimi (5 kuni 50 tähmärki)"));
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(namelbl, nametxt);

        listView2 = new ListView<String>();              //REF: how to make a listview : https://docs.oracle.com/javafx/2/ui_controls/list-view.htm
        items = FXCollections.observableArrayList("30 sekundit", "1 päev", "1 nädal", "2 nädalat");
        listView2.setItems(items);
        grid.add(listView2, 2, 1);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(hbox);
        grid.add(vbox, 2, 0);

        Button newbtn = new Button("Uus");                      // REF: http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI
        newbtn.setOnAction(new NewButtonListener());
        Button delbtn = new Button("Kustuta");
        delbtn.setOnAction(new DeleteButtonListener());
        HBox hbox2 = new HBox(10);
        hbox2.getChildren().addAll(newbtn, delbtn);
        grid.add(hbox2, 1, 2);

        Button savebtn = new Button("Salvesta");
        savebtn.setOnAction(new SaveButtonListener());
        AnchorPane anchor = new AnchorPane();
        AnchorPane.setRightAnchor(savebtn, 0.0);
        anchor.getChildren().add(savebtn);
        grid.add(anchor, 2, 2);

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
            nametxt.setText(todo.getName());
            listView2.getSelectionModel().select(todo.getReminder());
        }
    }

    private class NewButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {                         // REF: http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI

            Todo todo = new Todo("Uus ToDo", 0);

            ReminderTask todoTask = new ReminderTask(todo.getReminder(), todo.getName());
            todo.setTask(todoTask);

            int ix = 0;
            toDoList.add(ix, todo);
            listView.getSelectionModel().clearAndSelect(ix);
            nametxt.clear();
            listView2.getSelectionModel().clearSelection();
            nametxt.setText("Uus ToDo");
            nametxt.requestFocus();
        }
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent ae) {

            int ix = listView.getSelectionModel().getSelectedIndex();
            if (ix < 0) {
                return;
            }

            String s1 = nametxt.getText();

            Todo todo = toDoList.get(ix);
            todo.setName(s1);

            int reminderIx = listView2.getSelectionModel().getSelectedIndex();
            todo.setReminder(reminderIx);

            ReminderTask todoTask = todo.getTask();
            todoTask.cancel();

            todoTask = new ReminderTask(todo.getReminder(), todo.getName());
            todo.setTask(todoTask);

            toDoList.set(ix, null);
            toDoList.set(ix, todo);
            listView.getSelectionModel().clearAndSelect(ix);
            listView.requestFocus();
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent ae) {
            int ix = listView.getSelectionModel().getSelectedIndex();
            if (ix < 0) {
                return;
            }
            Todo todo = toDoList.get(ix);
            todo.getTask().cancel();
            toDoList.remove(ix);

            if (toDoList.size() == 0) {
                nametxt.clear();
                return;
            }

            listView.getSelectionModel().clearAndSelect(ix);
            Todo itemSelected = toDoList.get(ix);                    // REF: http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI
            nametxt.setText(itemSelected.getName());
            listView.requestFocus();
        }
    }
}
