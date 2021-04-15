import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class SettingsMenuController  extends GUIController
{
    @FXML
    private TextField hostTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField timeoutTextField;

    @FXML
    private Button returnButton;
    @FXML
    private Button saveButton;

    @FXML
    private void initialize()
    {
        returnButton.setOnAction(value ->
        {
            switchScene(returnButton.getScene(), System.getProperty("user.dir") + "/src/resources/MainMenuv1.fxml", new MainMenuController());
        });

        saveButton.setOnMouseClicked(event -> {
            saveProperties();
        });

        getProperties();
    }

    private void saveProperties() {
        try (OutputStream output = new FileOutputStream(System.getProperty("user.dir") + "/src/resources/network.properties")) {
            Properties properties = new Properties();

            properties.setProperty("host", this.hostTextField.getText());
            properties.setProperty("port", this.portTextField.getText());
            properties.setProperty("timeout", this.timeoutTextField.getText());

            properties.store(output, "");
        }
        catch (Exception e) {
            System.out.println(e);
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Settings");
        alert.setHeaderText("Settings successfully saved.");
    }

    private void getProperties() {
        try (InputStream input = new FileInputStream(System.getProperty("user.dir") + "/src/resources/network.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            this.hostTextField.setText(properties.getProperty("host"));
            this.portTextField.setText(properties.getProperty("port"));
            this.timeoutTextField.setText(properties.getProperty("timeout"));
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
