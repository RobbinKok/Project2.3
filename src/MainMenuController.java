import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class MainMenuController extends GUIController
{
    

    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button multiPlayerButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button ruleBookButton;
    @FXML
    private Button quitButton;

    @FXML
    private void initialize() throws IOException
    {
        singlePlayerButton.setOnAction(value ->
        {
                switchScene(singlePlayerButton.getScene(), System.getProperty("user.dir") + "/src/resources/SingleplayerMenu.fxml", new SpMenuController());
        });
        multiPlayerButton.setOnAction(value ->
        {
                switchScene(multiPlayerButton.getScene(), System.getProperty("user.dir") + "/src/resources/MultiplayerMenu.fxml", new MpMenuController());
        });
        settingsButton.setOnAction(value ->
        {
                switchScene(settingsButton.getScene(), System.getProperty("user.dir") + "/src/resources/Settings.fxml", new SettingsMenuController());
        });
        ruleBookButton.setOnAction(value ->
        {
                switchScene(ruleBookButton.getScene(), System.getProperty("user.dir") + "/src/resources/RuleBook.fxml", new RuleBookController());
        });
        quitButton.setOnAction(value ->
        {
                // gebruik platform.exit!!!!
                System.exit(0);
        });
    }
}
