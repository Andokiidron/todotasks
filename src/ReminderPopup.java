import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class ReminderPopup {

    private String task;

    public ReminderPopup(String task) {
        Platform.setImplicitExit(false);
        this.task = task;

        Platform.runLater(new Runnable(){

            @Override
            public void run() {
                final Stage popUpWindow = new Stage();
                popUpWindow.initModality(Modality.WINDOW_MODAL);
                Button okButton = new Button("Sulge");
                okButton.setOnAction(new EventHandler<ActionEvent>(){

                    @Override
                    public void handle(ActionEvent arg0) {
                        popUpWindow.close();
                    }
                });

                Scene myDialogScene = new Scene(VBoxBuilder.create()
                        .children(new Text(task), okButton)
                        .alignment(Pos.CENTER)
                        .padding(new Insets(10))
                        .build());

                popUpWindow.setScene(myDialogScene);
                popUpWindow.show();
            }
        });
    }
}
