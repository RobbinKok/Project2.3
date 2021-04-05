import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    
    @FXML
    private void initialize()
    {
        returnButton.setOnAction(value ->
        {
            switchScene(returnButton.getScene(), System.getProperty("user.dir") + "/src/resources/Multiplayer.fxml", new MainMenuController());
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
                    }
                }
            );

        goButton.setOnAction(value ->
        {
            switchScene(goButton.getScene(), System.getProperty("user.dir") + "/src/resources/OrthelloGameview.fxml", new OrthelloGameController());
        });
    }
}