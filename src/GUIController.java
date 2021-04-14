import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import java.io.FileInputStream;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class GUIController 
{
    public GUIController()
    {

    }

    protected void switchScene(Scene scene, String fxmlpath, GUIController gc)
    {
        try 
            {
                
                // Create the FXMLLoader 
                FXMLLoader loader = new FXMLLoader();
                // Path to the FXML File
                String fxmlDocPath = fxmlpath;
                FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
                loader.setController(gc);
                AnchorPane root = (AnchorPane) loader.load(fxmlStream);
                scene.setRoot(root);
                fxmlStream.close();
            } 
            catch (IOException e) {
                //TODO: handle exception
                System.out.print(e);
            }
    }
    // w/o controller
    protected void switchScene(Scene scene, String fxmlpath)
    {
        try 
            {
                
                // Create the FXMLLoader 
                FXMLLoader loader = new FXMLLoader();
                // Path to the FXML File
                String fxmlDocPath = fxmlpath;
                FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
                AnchorPane root = (AnchorPane) loader.load(fxmlStream);
                scene.setRoot(root);
                fxmlStream.close();
            } 
            catch (IOException e) {
                //TODO: handle exception
            }
    }
}
