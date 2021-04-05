import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
public class SpMenuController extends GUIController
{
    @FXML
    private Button returnButton;
    @FXML
    private ChoiceBox<String> gameTypeChoiceBox;
    @FXML
    private ChoiceBox<String> difficultyChoiceBox;

    @FXML
    private void initialize()
    {
        System.out.println("?????????");

        gameTypeChoiceBox.getItems().add("Tic Tac Toe");
        gameTypeChoiceBox.getItems().add("Orthello");

        gameTypeChoiceBox.setOnAction(value -> 
        {
            String selectedGame = gameTypeChoiceBox.getSelectionModel().getSelectedItem();
            System.out.println("Selected Game: " + selectedGame);
        });

        difficultyChoiceBox.getItems().add("Easy");
        difficultyChoiceBox.getItems().add("Medium");
        difficultyChoiceBox.getItems().add("Hard");

        difficultyChoiceBox.setOnAction(value -> 
        {
            String selectedDifficulty = difficultyChoiceBox.getSelectionModel().getSelectedItem();
            System.out.println("Selected Difficulty: " + selectedDifficulty);
        });

        returnButton.setOnAction(value ->
        {
            switchScene(returnButton.getScene(), System.getProperty("user.dir") + "/Resources/MainMenuv1.fxml", new MainMenuController());
        });
    }
}
