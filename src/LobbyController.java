import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import utils.commands.*;

public class LobbyController extends GUIController
{
    @FXML
    private Button returnButton;
    @FXML
    private Button goButton;
    @FXML
    private ListView<String> playerList;
    @FXML
    private TableView<Challenge> tableView;
    @FXML
    private Button subscribeButton;
    @FXML
    private CheckBox playAsAI;
    @FXML
    private ComboBox<String> gameTypes;

    private NetworkClient networkClient;

    private ObservableList<Challenge> challenges = FXCollections.observableArrayList();

    public LobbyController(NetworkClient networkClient) {
        this.networkClient = networkClient;

        CommandHandler commandHandler = this.networkClient.getCommandHandler();

        commandHandler.addCommand(CommandHandler.CommandType.Error, new ErrorCommand(data -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Network Error Response");
                alert.setContentText(data);
                alert.showAndWait();

                if (data.contains("Duplicate name exists"))
                    switchScene(goButton.getScene(), System.getProperty("user.dir") + "/src/resources/MultiplayerMenu.fxml", new MpMenuController());
            });
        }));

        commandHandler.addCommand(CommandHandler.CommandType.Challenge, new ChallengeCommand(data -> {
            String name, number, type;

            name = data.get("CHALLENGER");
            number = data.get("CHALLENGENUMBER");
            type = data.get("GAMETYPE");

            this.challenges.add(new Challenge(name, number, type));
        }));

        commandHandler.addCommand(CommandHandler.CommandType.Match, new MatchCommand(data -> {
            String gameType = data.get("GAMETYPE");
            String opponent = data.get("OPPONENT");

            if (gameType.equals("Reversi")) {
                networkClient.setFirstPlayer(data.get("PLAYERTOMOVE"));
                networkClient.setOpponentName(opponent);
                networkClient.setPlayAsAI(playAsAI.isSelected());
                OthelloGameController othelloGameController = new OthelloGameController(networkClient);
                switchScene(goButton.getScene(), System.getProperty("user.dir") + "/src/resources/OthelloGameview.fxml", othelloGameController);
            }
            else if (gameType.equals("Tic-tac-toe"))
                switchScene(goButton.getScene(), System.getProperty("user.dir") + "/src/resources/TicTacToe.fxml",  new TicTacToeGameController(networkClient));
        }));

        commandHandler.addCommand(CommandHandler.CommandType.GameList, new GameListCommand(data -> {
            Platform.runLater(() -> {
                for (String type: data) {
                    gameTypes.getItems().add(type);
                }
            });
        }));

        commandHandler.addCommand(CommandHandler.CommandType.PlayerList, new PlayerListCommand(data -> {
            Platform.runLater(() -> {
                playerList.getItems().removeAll(playerList.getItems());

                for(String name : data) {
                    if (name.equals(networkClient.getPlayerName()))
                        this.playerList.getItems().add(name + " (Me)");
                    else
                        this.playerList.getItems().add(name);
                }
            });
        }));

        if (!this.networkClient.getCommandHandler().isRunning())
            this.networkClient.startCommandHandler();
    }

    @FXML
    private void initialize()
    {
        returnButton.setOnAction(value ->
        {
            switchScene(returnButton.getScene(), System.getProperty("user.dir") + "/src/resources/MultiplayerMenu.fxml", new MainMenuController());
        });

        TableColumn players = new TableColumn("Username");
        players.setCellValueFactory(new PropertyValueFactory<Challenge, String>("username"));

        TableColumn invite = new TableColumn("ID");
        invite.setCellValueFactory(new PropertyValueFactory<Challenge, String>("id"));

        TableColumn type = new TableColumn("Type");
        type.setCellValueFactory(new PropertyValueFactory<Challenge, String>("type"));

        tableView.getColumns().add(players);
        tableView.getColumns().add(invite);
        tableView.getColumns().add(type);

        tableView.setItems(challenges);

        tableView.setOnMouseClicked(mouseEvent -> {
            String id = tableView.getSelectionModel().getSelectedItem().getId();
            networkClient.acceptChallenge(Integer.parseInt(id));
        });

        subscribeButton.setOnMouseClicked(mouseEvent -> {
            String gameType = gameTypes.getSelectionModel().getSelectedItem();
            networkClient.subscribe(gameType);
        });

        goButton.setOnAction(value ->
        {
            switchScene(goButton.getScene(), System.getProperty("user.dir") + "/src/resources/OthelloGameview.fxml", new OthelloGameController(networkClient));
        });

        playerList.setOnMouseClicked(mouseEvent -> {
            String name = playerList.getSelectionModel().getSelectedItem();

            if (name.isBlank() || name.isEmpty())
                return;

            if (name.contains(networkClient.getPlayerName())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cannot Challenge Yourself!");
                alert.show();
                return;
            }

            String selectedGame = gameTypes.getSelectionModel().getSelectedItem();

            if (selectedGame == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please select a GameType");
                alert.showAndWait();
                return;
            }

            this.networkClient.challengePlayer(name, selectedGame);
        });

        this.networkClient.getList(NetworkClient.ListType.Playerlist);
        this.networkClient.getList(NetworkClient.ListType.Gamelist);

        // Hacky timer on UI Thread, runs every 5 seconds.
        Timeline getListTask = new Timeline(new KeyFrame(Duration.seconds(5), actionEvent -> networkClient.getList(NetworkClient.ListType.Playerlist)));
        getListTask.setCycleCount(Timeline.INDEFINITE);
        getListTask.play();
    }

    public static class Challenge {
        private final SimpleStringProperty username;
        private final SimpleStringProperty id;
        private final SimpleStringProperty type;

        private Challenge(String username, String id, String  type) {
            this.username = new SimpleStringProperty(username);
            this.id = new SimpleStringProperty(id);
            this.type = new SimpleStringProperty(type);
        }

        public void setUsername(String username) {
            this.username.set(username);
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public String getUsername() {
            return this.username.get();
        }

        public String getId() {
            return this.id.get();
        }

        public String getType() {
            return this.type.get();
        }
    }
}