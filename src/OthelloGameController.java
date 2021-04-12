import AI.AI;
import AI.AIBest;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import utils.commands.Command;
import utils.commands.CommandHandler;
import utils.commands.MatchCommand;
import utils.commands.MoveCommand;

import java.util.ArrayList;
import java.util.Random;

public class OthelloGameController extends GUIController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Text playerOne;
    @FXML
    private Text playerTwo;
    @FXML
    private Text currentPlayer;
    @FXML
    private ListView<String> movesList;
    @FXML
    private Button giveUpButton;

    //int side = 0;
    private final ReversieV2 reversie;
    private final AI ai;

    private final NetworkClient networkClient;

    public OthelloGameController(NetworkClient networkClient) {
        this.networkClient = networkClient;
        int playerColor, computerColor;

        if (networkClient.getPlayerName().equals(networkClient.getFirstPlayer())) {
            playerColor = Reversie.BLACK;
            computerColor = Reversie.WHITE;
        } else {
            playerColor = Reversie.WHITE;
            computerColor = Reversie.BLACK;
        }

        reversie = new ReversieV2(computerColor, playerColor, this);
        ai = new AI(reversie);

        if (isMultiplayer()) {
            CommandHandler commandHandler = networkClient.getCommandHandler();

            commandHandler.addCommand(CommandHandler.CommandType.MyTurn, new Command() {
                @Override
                public void execute(String data) {
                    if (networkClient.getPlayAsAI()) {
                        AIBest bestMove = ai.chooseMove(playerColor, reversie.getBoard());
//                        reversie.playMove(bestMove.row, bestMove.column, playerColor);

//                        ArrayList<int[]> moves = reversie.getPossibleMoves(reversie.getBoard(), playerColor);
//                        for (int[] coords : moves) {
//                            System.out.println(NetworkClient.localToNetworkCoordinates(coords[0], coords[1], NetworkClient.GameType.Reversi));
//                        }
//                        int[] move = moves.get(new Random().nextInt(moves.size()));

                        networkClient.move(bestMove.column, bestMove.row, NetworkClient.GameType.Reversi);

                        reversie.playMove(bestMove.column, bestMove.row, playerColor);

                        try {
                            Thread.sleep(1000);
                        } catch (Exception ignored) {
                        }

//                    networkClient.move(bestMove.row, bestMove.column, NetworkClient.GameType.Reversi);
                    }

                    System.out.println(reversie);
                    System.out.println("Making my move!");
                }
            });

            commandHandler.addCommand(CommandHandler.CommandType.Move, new MoveCommand(data -> {
                String name, move, details;
                name = data.get("PLAYER");
                move = data.get("MOVE");
                details = data.get("DETAILS");

                if (name.equals(networkClient.getPlayerName())) {
                    return;
                }
//                System.out.println("MOVE Playing for: " + reversie.side);

                int[] coords = NetworkClient.networkToLocalCoordinates(Integer.parseInt(move), NetworkClient.GameType.Reversi);

                reversie.playMove(coords[0], coords[1], computerColor);

//                System.out.println("Player: " + name + " made move " + move + " with message: " + details);
            }));

            commandHandler.addCommand(CommandHandler.CommandType.Result, new MatchCommand(data -> {
                String scoreOne, scoreTwo, comment;

                scoreOne = data.get("PLAYERONESCORE");
                scoreTwo = data.get("PLAYERTWOSCORE");
                comment = data.get("COMMENT");

                Platform.runLater(() -> {
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Results");
//                    alert.setHeaderText("Score");
//                    alert.setContentText("Black: " + reversie.getBlackScore() + " White: " + reversie.getWhiteScore());
//                    alert.showAndWait();

                });

//                switchScene(giveUpButton.getScene(), System.getProperty("user.dir") + "/src/resources/Lobby.fxml", new LobbyController(this.networkClient));
            }));
        }
    }

    @FXML
    private void initialize() {
        fillGrid();

        //gridpane.set

        System.out.println(playerOne);
        //setScore(0, 3);
        //setScore(1, 96);

        giveUpButton.setOnAction(value ->
        {
            switchScene(giveUpButton.getScene(), System.getProperty("user.dir") + "/src/resources/SingleplayerMenu.fxml", new SpMenuController());
        });
    }

    public void setScore(int blackScore, int whiteScore) {
        playerOne.setText("Black: " + blackScore);
        playerTwo.setText("White: " + whiteScore);
    }

    public void updateCurrentPlayer(int side) {
        if (side == 0) {
            currentPlayer.setText("Current Player: Black");
        } else {
            currentPlayer.setText("Current Player: White");
        }
    }

    public void addMove(int side, int x, int y) {
        if (side == 0) {
//            movesList.getItems().add("Black to [" + x + "," + y + "]");
        } else {
//            movesList.getItems().add("White to [" + x + "," + y + "]");
        }
    }

    private void fillGrid() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                addNode(x, y);
            }
        }

        changeNodeColor(3, 4, Color.BLACK);
        changeNodeColor(3, 3, Color.WHITE);
        changeNodeColor(4, 3, Color.BLACK);
        changeNodeColor(4, 4, Color.WHITE);
    }

    private void addNode(int x, int y) {
        Circle node = new Circle(0, 0, 10);
        node.setStroke(Color.BLACK);
        node.setOpacity(0);
        node.setOnMouseClicked(value -> {
            if (node.getOpacity() < 1) {
                /*Color tmp = side==0 ? Color.BLACK : Color.WHITE;
                node.setFill(tmp);
                node.setOpacity(1);
                side = side==0 ? 1 : 0;*/

                reversie.playMove(x, y, reversie.side);

                if (isMultiplayer())
                    networkClient.move(x, y, NetworkClient.GameType.Reversi);
            }
        });
        gridPane.add(node, x, y);
        gridPane.add(new Text(String.valueOf(NetworkClient.localToNetworkCoordinates(x, y, NetworkClient.GameType.Reversi))), x, y);
        GridPane.setHalignment(node, HPos.CENTER);
    }

    public void changeNodeColor(int column, int row, Color color) {
        Node node = getNodeByRowColumnIndex(row, column, this.gridPane);
        Circle circle = (Circle) node;

        circle.setFill(color);
        node.setOpacity(1);
    }

    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            try {
                if (GridPane.getRowIndex(node) != null) {
                    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                        result = node;
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return result;
    }

    private boolean isMultiplayer() {
        return this.networkClient != null;
    }

    public NetworkClient getNetworkClient() {
        return this.networkClient;
    }
}