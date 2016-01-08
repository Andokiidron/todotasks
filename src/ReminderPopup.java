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

        Platform.runLater(new Runnable() { // new thread to run the program

            @Override
            public void run() {
                final Stage popUpWindow = new Stage();
                popUpWindow.initModality(Modality.WINDOW_MODAL);

                VBox vbox = new VBox();
                vbox.setSpacing(100);
                vbox.setAlignment(Pos.CENTER);
                vbox.getChildren().addAll(new Text(task));

                Scene myDialogScene = new Scene(vbox);
                popUpWindow.setResizable(true);

                popUpWindow.setScene(myDialogScene);
                popUpWindow.show();

                // Close popup window automatically : http://stackoverflow.com/questions/27334455/how-to-close-a-stage-after-a-certain-amount-of-time-javafx
                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(event -> popUpWindow.close());
                delay.play();
            }
        });
    }
}