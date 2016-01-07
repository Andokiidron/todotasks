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

// VIIDE!! kogu põhja algne viide: http://www.javacodegeeks.com/2015/01/javafx-list-example.html#Build the GUI

public class Main
        extends Application {

    private ListView<Todo> listView;
    private ObservableList<Todo> data;
    private TextField nametxt;
    private ListView<String> listView2;
    private ObservableList<String> items;

    public static void main(String [] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {   //Teen põhiakna
        primaryStage.setTitle("Todo App");

        // gridPane layout
        GridPane grid = new GridPane();   //sellega panen paika erinevad nupud ja tekstikastid
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15); // horisonaalsete ridade vahel olev vahe
        grid.setVgap(20); //vertikaalsete veerguda vahel olev vahe
        grid.setPadding(new Insets(25, 25, 25, 25)); //ümber gridi olev ruumi vahe

        // list view, listener and list data
        listView = new ListView<>();     //teen uue listview
        listView.getSelectionModel().selectedIndexProperty().addListener(
                new ListSelectChangeListener());      //panen listeneri, et salvestada/säilitada valitud variant VIIDE: http://stackoverflow.com/questions/13264017/getting-selected-element-from-listview
        data = FXCollections.observableList(new ArrayList<>());
        listView.setItems(data);
        grid.add(listView, 1, 1); // paigutan listview endal olevasse grid aknasse 1 ritta 1 veergu

        // todo name label       //alanupud vertikaalselt ja horisonaalselt

        Label namelbl = new Label("Todo nimi:");
        nametxt = new TextField();
        nametxt.setMinHeight(30.0);
        nametxt.setPromptText("Lisa ToDo (kohustuslik väli).");
        nametxt.setPrefColumnCount(20);
        nametxt.setTooltip(new Tooltip("Todo nimi (5 kuni 50 tähmärki)"));
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(namelbl, nametxt);

        // Sellega saaks variandid panna üksteise alla ritta ->

        //VIIDE!! listview oma: https://docs.oracle.com/javafx/2/ui_controls/list-view.htm

        listView2 = new ListView<String>();
        items =FXCollections.observableArrayList("1 minutit", "10 minutit", "30 minutit", "2 tundi");
        listView2.setItems(items);
        grid.add(listView2, 2, 1);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(hbox);
        grid.add(vbox, 2, 0);

        // Lisame nupud uus, kustuta ja salvesta.
        Button newbtn = new Button("Uus");
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

        // Scene'ga teeme põhiakna
        Scene scene = new Scene(grid, 1000, 500); // laius 1000 kõrgus 500
        primaryStage.setScene(scene);
        primaryStage.show();

        listView.getSelectionModel().selectFirst(); // valib automaatselt listist esimese, kui seal on todosid
    }

    private class ListSelectChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> ov,
                            Number old_val, Number new_val) {

            if ((new_val.intValue() < 0) || (new_val.intValue() >= data.size())) {
                return; // invalid data
            }

            Todo todo = data.get(new_val.intValue());
            nametxt.setText(todo.getName()); // lisab Todole nime
            listView2.getSelectionModel().select(todo.getReminder());
        }
    }

    private class NewButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            Todo todo = new Todo("Uus ToDo", 0); //lisab uue ülesande esimesele reale

            ReminderTask todoTask = new ReminderTask(todo.getReminder(), todo.getName());
            todo.setTask(todoTask);

            int ix = 0;
            data.add(ix, todo);
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

            if (ix < 0) { // Pole midagi valitud või sisestud
                return;
            }

            String s1 = nametxt.getText();

            Todo todo = data.get(ix);
            todo.setName(s1);

            int reminderIx = listView2.getSelectionModel().getSelectedIndex();
            todo.setReminder(reminderIx);

            ReminderTask todoTask = todo.getTask();
            todoTask.cancel();

            todoTask = new ReminderTask(todo.getReminder(), todo.getName());
            todo.setTask(todoTask);

            // uuenda vaadet todonimega ja tee see aktiivseks
            data.set(ix, null);
            data.set(ix, todo);
            listView.getSelectionModel().clearAndSelect(ix);
            listView.requestFocus();
        }
    }
    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent ae) {
            int ix = listView.getSelectionModel().getSelectedIndex();
            if (ix < 0) { // no data or none selected
                return;
            }
            Todo todo = data.remove(ix);

            // set next todo item after delete
            if (data.size() == 0) {
                nametxt.clear();
                return; // no selection
            }

            listView.getSelectionModel().clearAndSelect(ix);
            Todo itemSelected = data.get(ix); // selected ix data (not set by list listener); // requires this is set
            nametxt.setText(itemSelected.getName());
            listView.requestFocus();
        }
    }
}
