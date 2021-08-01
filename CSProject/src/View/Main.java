package View;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.*;
import javafx.concurrent.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;
// adapted from the example of splash screen in the notes

public class Main extends Application {
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private ImageView splash;
    private static int SPLASH_WIDTH = 680;
    private static int SPLASH_HEIGHT = 460;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void init() throws IOException {
        Random r = new Random();
        int n = r.nextInt(5) +1;
        Image img = new Image("file:" +  System.getProperty("user.dir") + "/resources/grocery-" + n +".png",660,390,false,false);
        splash = new ImageView(img);
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
        progressText = new Label("Initialisation Startup");
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(new ImageView(new Image("file:" + System.getProperty("user.dir") + "/resources/GroSure.png", 180,55,false,false)), splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle(
                "-fx-padding: 5; " +
                        "-fx-background-color: #f0fff0; " +
                        "-fx-border-width:5; " +
                        "-fx-border-color: " +
                        "linear-gradient(" +
                        "to bottom, " +
                        "darkgreen, " +
                        "derive(springgreen, 50%)" +
                        ");"
        );
        splashLayout.setEffect(new DropShadow());
    }

    @Override
    public void start(final Stage initStage) throws Exception {

        final Task<ObservableList<String>> loadingTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {

                ObservableList<String> doneMessages =
                        FXCollections.<String>observableArrayList();
                ObservableList<String> messageList =
                        FXCollections.observableArrayList(
                                "Checking stocks", "Filling shelves", "Moving trucks", "Delivering produce", "Descaling fish",
                                "Checking Users", "Getting App Version Info", "Uploading Price Lists", "Finishing Up...", "Finishing Up"
                        );

                updateMessage("Grocering . . .");
                for (int i = 0; i < messageList.size(); i++) {
                    Random r = new Random();
                    int t = r.nextInt(280) + 320;
                    Thread.sleep(t);
                    updateProgress(i + 1, messageList.size());
                    String nextMessage = messageList.get(i);
                    doneMessages.add(nextMessage);
                    updateMessage(nextMessage);
                }
                Thread.sleep(500);
                updateMessage("Initialisation complete");

                return doneMessages;
            }
        };



        showSplash(
                initStage,
                loadingTask,
                () -> showMainStage(loadingTask.valueProperty())
        );

        new Thread(loadingTask).start();
    }

    private void showMainStage(
            ReadOnlyObjectProperty<ObservableList<String>> friends
    ) {
        try {
            Stage mainStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Enter.fxml"));
            mainStage.setTitle("GroSure User Page");
            mainStage.getIcons().add(new Image("file:" +  System.getProperty("user.dir") + "/resources/GroSureMini.png"));
            mainStage.setScene(new Scene(root, 335, 600));
            mainStage.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
}

    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });

        new Thread(new Runnable() {
            @Override
            public synchronized void run(){
                try{
                    for (int a  = 0 ; a < 3; a++){
                        Random r = new Random();
                        int n = r.nextInt(5) +1;
                        Image img = new Image("file:" +  System.getProperty("user.dir") + "/resources/grocery-" + n +".png",660,390,false,false);
                        splash.setImage(img);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                        Thread.sleep(1400);
                    } }
                catch (InterruptedException e) {
                }
            }


        }).start();

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    public interface InitCompletionHandler {
        void complete();
    }
}


