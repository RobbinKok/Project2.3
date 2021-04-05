import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SettingsMenuController  extends GUIController
{
    @FXML
    private Button returnButton;
    
    @FXML
    private void initialize()
    {
        returnButton.setOnAction(value ->
        {
            switchScene(returnButton.getScene(), System.getProperty("user.dir") + "/src/resources/MainMenuv1.fxml", new MainMenuController());
        });
    }
}
