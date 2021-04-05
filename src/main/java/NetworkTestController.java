package main.java;

import com.sun.media.jfxmediaimpl.platform.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.java.utils.Observable;
import main.java.utils.Observer;

public class NetworkTestController extends Controller {
    @FXML
    private TextArea serverOutput;
    @FXML
    private TextField rawServerMessage;

    private NetworkClient client;

    public NetworkTestController() {
        this.client = new NetworkClient("127.0.0.1", 7789);
    }

    @FXML
    public void initialize() {

    }

    @FXML
    private void onConnect() {
        client.connect();
    }

    @FXML
    private void onDisconnect() {
        client.disconnect();
    }

    @FXML
    private void onLogin() {
        client.login("test");
        printServerOutput("Logging in as user: test");
    }

    @FXML
    private void onLogout() {
        client.logout();
        printServerOutput("Logging out");
    }

    @FXML
    private void onGetGames() {
        client.getList(NetworkClient.ListType.Gamelist);
        printServerOutput("Fetching list of Games...");
    }

    @FXML
    private void onGetPlayers() {
        client.getList(NetworkClient.ListType.Playerlist);
        printServerOutput("Fetching list of Players");
    }

    @FXML
    private void onSubscribe() {
        client.subscribe(NetworkClient.GameType.Reversi);
        printServerOutput("Subscribing to Reversi");
    }

    @FXML void onSendRawMessage() {
        client.sendRawMessage(rawServerMessage.getText());
    }

    private void printServerOutput(String message) {
        String text = this.serverOutput.getText();
        text += "[Console]: " + message + "\n";
        this.serverOutput.setText(text);
    }
}
