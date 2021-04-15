import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
public class SpMenuController extends GUIController
{
    @FXML
    private Button returnButton;
    @FXML
    private Button goButton;
    @FXML
    private ChoiceBox<String> gameTypeChoiceBox;
    @FXML
    private ChoiceBox<String> difficultyChoiceBox;

    String gameType = "Othello";
    String difficulty = "Easy";
    @FXML
    private void initialize()
    {
        gameTypeChoiceBox.getItems().add("Tic Tac Toe");
        gameTypeChoiceBox.getItems().add("Othello");

        gameTypeChoiceBox.setOnAction(value -> 
        {
            String selectedGame = gameTypeChoiceBox.getSelectionModel().getSelectedItem();
            System.out.println("Selected Game: " + selectedGame);
            gameType = selectedGame;

        });

        difficultyChoiceBox.getItems().add("Easy");
        difficultyChoiceBox.getItems().add("Medium");
        difficultyChoiceBox.getItems().add("Hard");

        difficultyChoiceBox.setOnAction(value -> 
        {
            String selectedDifficulty = difficultyChoiceBox.getSelectionModel().getSelectedItem();
            difficulty = selectedDifficulty;
        });

        returnButton.setOnAction(value ->
        {
            switchScene(returnButton.getScene(), System.getProperty("user.dir") + "/src/resources/MainMenuv1.fxml", new MainMenuController());
        });

        goButton.setOnAction(value ->
        {
            System.out.println(gameType);
            if(gameType.equals("Othello"))
            {
                switchScene(goButton.getScene(), System.getProperty("user.dir") + "/src/resources/OthelloGameview.fxml", new OthelloGameController(null));
            }
            else if(gameType.equals("Tic Tac Toe"))
            {
                switchScene(goButton.getScene(), System.getProperty("user.dir") + "/src/resources/TicTacToe.fxml", new TicTacToeGameController(null));
            }
        });
    }
}
