import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ReminderPopup {

    public ReminderPopup(String task) {
        Platform.setImplicitExit(false);

        Platform.runLater(new Runnable() {                        // new thread to run the program. Run the specified Runnable on the JavaFX Application Thread at some unspecified time in the future. REF: https://docs.oracle.com/javafx/2/api/javafx/application/Platform.html

            @Override
            public void run() {
                final Stage popUpWindow = new Stage();
                popUpWindow.initModality(Modality.WINDOW_MODAL);        // Defines a modal window that block events from being delivered to its entire owner window hierarchy. REF: https://docs.oracle.com/javafx/2/api/javafx/stage/Modality.html
                popUpWindow.setMinHeight(100);
                popUpWindow.setMinWidth(200);
                popUpWindow.setResizable(true);

                VBox vbox = new VBox();
                vbox.setAlignment(Pos.CENTER);
                vbox.setStyle("-fx-background-color: #cd5c5c; ");       // set the color of the popupwindow background REF: http://stackoverflow.com/questions/22614758/issue-with-background-color-in-javafx-8
                vbox.getChildren().addAll(new Text(task));

                Scene myDialogScene = new Scene(vbox);

                popUpWindow.setScene(myDialogScene);
                popUpWindow.show();

                PauseTransition delay = new PauseTransition(Duration.seconds(3)); // Close popup window automatically : http://stackoverflow.com/questions/27334455/how-to-close-a-stage-after-a-certain-amount-of-time-javafx
                delay.setOnFinished(event -> popUpWindow.close());
                delay.play();
            }
        });
    }
}