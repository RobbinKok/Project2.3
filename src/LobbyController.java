import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import javafx.util.Pair;
import utils.Callback;
import utils.commands.*;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LobbyController extends GUIController
{
    @FXML
    private Button returnButton;
    @FXML
    private Button goButton;
    @FXML
    private ListView<String> playerList;
    @FXML
    private TableView<String> tableView;

    private NetworkClient networkClient;

    public LobbyController(NetworkClient networkClient) {
        this.networkClient = networkClient;

        CommandHandler commandHandler = this.networkClient.getCommandHandler();

        commandHandler.addCommand(CommandHandler.CommandType.Challenge, new ChallengeCommand(data -> {

        }));

        commandHandler.addCommand(CommandHandler.CommandType.GameList, new GameListCommand(data -> {

        }));

        commandHandler.addCommand(CommandHandler.CommandType.PlayerList, new PlayerListCommand(data -> {
            Platform.runLater(() -> {
                playerList.getItems().removeAll(playerList.getItems());

                for(String name : data) {
                    this.playerList.getItems().add(name);
                }
            });
        }));

        new Thread(this.networkClient.getCommandHandler()).start();
    }

    @FXML
    private void initialize()
    {
        returnButton.setOnAction(value ->
        {
            switchScene(returnButton.getScene(), System.getProperty("user.dir") + "/src/resources/MultiplayerMenu.fxml", new MainMenuController());
        });
        
        playerList.getItems().add("Willem");
        playerList.getItems().add("Klaas");
        playerList.getItems().add("Pieter");
        playerList.getItems().add("Karel");
        playerList.getItems().add("Joop");
        playerList.getItems().add("Gert");

        TableColumn<String, String> players = new TableColumn<>("players");
        /*players.setCellValueFactory
        (
            new PropertyValueFactory<String, String>("Henk")
        );*/
        TableColumn<String, String> invite = new TableColumn<>("invite");
        tableView.getColumns().add(players);
        tableView.getColumns().add(invite);
        
        //tableView.setItems(arg0);
        playerList.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() 
            {
                public void changed(ObservableValue<? extends String> ov, 
                    String old_val, String new_val) 
                    {
                        // geselecteerde waarde -> new_val
                        System.out.println(new_val);
                        networkClient.challengePlayer(new_val, NetworkClient.GameType.Reversi);
                    }
                }
            );

        goButton.setOnAction(value ->
        {
            switchScene(goButton.getScene(), System.getProperty("user.dir") + "/src/resources/OthelloGameview.fxml", new OthelloGameController(this.networkClient));
        });

        this.networkClient.getList(NetworkClient.ListType.Gamelist);
        this.networkClient.getList(NetworkClient.ListType.Playerlist);

        Timeline getListTask = new Timeline(new KeyFrame(Duration.seconds(5), actionEvent -> networkClient.getList(NetworkClient.ListType.Playerlist)));
        getListTask.setCycleCount(Timeline.INDEFINITE);
        getListTask.play();
    }
}