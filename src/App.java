import java.io.IOException;
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class App extends Application
{
    Stage thestage;

    public static void main(String[] args) throws Exception
    {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage stage) throws IOException
    {
        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Path to the FXML File
        String fxmlDocPath = System.getProperty("user.dir") + "/src/resources/MainMenuv1.fxml";

        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

        MainMenuController controller = new MainMenuController();
        loader.setController(controller);
        AnchorPane root = (AnchorPane) loader.load(fxmlStream);

        thestage = stage;

        Scene scene = new Scene(root);
        // Set the Scene to the Stage
        thestage.setScene(scene);
        // Set the Title to the Stage
        thestage.setTitle("game");
        // Display the Stage
        thestage.show();
    }
}
