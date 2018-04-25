package no.priv.kehm.bamodelapplication.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.priv.kehm.bamodelapplication.gui.MainController;

/**
 * Main class for launching the Barabási-Albert Model Application (BAModelApplication)
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/main.css");
        primaryStage.setTitle("Barabási-Albert Model Application");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        MainController controller = loader.getController();
    }

    /**
     * Main method to launch application
     *
     * @param args args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
