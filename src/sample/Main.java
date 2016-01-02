package sample;

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
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


// VIIDE!! kogu põhja algne viide: e

public class Main
        extends Application {

    private ListView<Todo> listView;
    private ObservableList<Todo> data;
    private TextField nametxt;
    private ListView listView2;

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
                new ListSelectChangeListener());      //panen listeneri, et salvestada/säilitada valitud variant
        data = getListData();
        listView.setItems(data);
        grid.add(listView, 1, 1); // paigutan listview endal olevasse grid aknasse 1 ritta 1 veergu

        // todo name label and text fld - in a hbox         //alanupud vertikaalselt ja horisonaalselt

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

        ObservableList names = FXCollections.observableArrayList();
        ObservableList data = FXCollections.observableArrayList();
        listView2 = new ListView(data);
        listView2.setPrefSize(100, 150);
        listView2.setEditable(true);

        names.addAll(
                "15 minutit", "1 tund", "4 tundi");

        for (int i = 0; i < 1; i++) {
            data.add("Vali aeg");
        }

        listView2.setItems(data);
        listView2.setCellFactory(ComboBoxListCell.forListView(names));
        StackPane root = new StackPane();
        root.getChildren().add(listView2);
        grid.add(listView2, 2, 1);

        // todo hbox (label + text fld), scrollpane - in a vbox

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(hbox);
        grid.add(vbox, 2, 0); // col = 2, row = 1


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

        // scene

        Scene scene = new Scene(grid, 1000, 500); // width = 750, height = 400
        primaryStage.setScene(scene);
        primaryStage.show();

        // initial selection
        listView.getSelectionModel().selectFirst(); // does nothing if no data

    } // start()

    private class ListSelectChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> ov,
                            Number old_val, Number new_val) {

            if ((new_val.intValue() < 0) || (new_val.intValue() >= data.size())) {

                return; // invalid data
            }

            // set name and desc fields for the selected todo
            Todo todo = data.get(new_val.intValue());
            nametxt.setText(todo.getName());
            //desctxt.setText(Integer.toString(todo.getReminder()));

        }
    }

    private ObservableList<Todo> getListData() {

        List<Todo> list = new ArrayList<>(); // initial list data
        list.add(new Todo("ToDo 1", 1));
        list.add(new Todo("ToDo 2", 2));
        list.add(new Todo("ToDo 3", 3));
        list.add(new Todo("ToDo 4", 4));
        ObservableList<Todo> data = FXCollections.observableList(list);

        return data;
    }

    private class NewButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {



            // creates a todo at first row with name NEW todo and selects it
            Todo todo = new Todo("NEW Todo", 0); // 0 = dummy id
            int ix = 0;
            data.add(ix, todo);
            listView.getSelectionModel().clearAndSelect(ix);
            nametxt.clear();
            //VALE  listView2.getSelectionModel().clearAndSelect(ix); //desctxt.clear();
            nametxt.setText("NEW Todo");
            nametxt.requestFocus();
        }
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent ae) {

            int ix = listView.getSelectionModel().getSelectedIndex();

            if (ix < 0) { // no data selected or no data

                return;
            }

            String s1 = nametxt.getText();
            //String s2 = desctxt.getText();

            // check if name is unique

            Todo todo = data.get(ix);
            todo.setName(s1);
            // todo.setReminder(Integer.parseInt(s2));

            // update list view with todo name, and select it
            data.set(ix, null); // required for refresh
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
                //desctxt.clear();
                return; // no selection
            }
            ix = ix - 1;
            if (ix < 0) {
                ix = 0;
            }
            listView.getSelectionModel().clearAndSelect(ix);
            Todo itemSelected = data.get(ix); // selected ix data (not set by list listener); // requires this is set
            nametxt.setText(itemSelected.getName());
            //desctxt.setText(itemSelected.getDesc());
            listView.requestFocus();
        }
    }
}
