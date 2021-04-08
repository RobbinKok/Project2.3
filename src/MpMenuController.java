import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MpMenuController extends GUIController
{
    @FXML
    private Button returnButton;
    @FXML
    private Button goButton;
    @FXML
    private TextField hostTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField userNameTextField;

    private NetworkClient networkClient;

    public MpMenuController() {}

    @FXML
    private void initialize()
    {
        getProperties();
        this.networkClient = new NetworkClient(hostTextField.getText(), Integer.parseInt(portTextField.getText()));

        hostTextField.setOnAction(value ->
        {
            // do something with the input
            System.out.println(hostTextField.getText());
        });

        portTextField.setOnAction(value ->
        {
            // do something with the input
            System.out.println(portTextField.getText());
        });

        userNameTextField.setOnAction(value ->
        {
            // do something with the input
            System.out.println(userNameTextField.getText());
        });

        returnButton.setOnAction(value ->
        {
            switchScene(returnButton.getScene(), System.getProperty("user.dir") + "/src/resources/MainMenuv1.fxml", new MainMenuController());
        });

        goButton.setOnAction(value ->
        {
            this.networkClient.connect((data) -> {
                this.networkClient.login(this.userNameTextField.getText());
                switchScene(goButton.getScene(), System.getProperty("user.dir") + "/src/resources/Lobby.fxml", new LobbyController(this.networkClient));
            });
        });
    }

    private void getProperties() {
        try (InputStream input = new FileInputStream(System.getProperty("user.dir") + "/src/resources/network.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            this.hostTextField.setText(properties.getProperty("host"));
            this.portTextField.setText(properties.getProperty("port"));
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
