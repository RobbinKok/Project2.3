import java.io.IOException;

import javax.lang.model.util.ElementScanner6;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class TicTacToeGameController  extends GUIController
{
    Image image1 = new Image("O.png");
    Image image2 = new Image("X.png");

    @FXML
    private Text outputText;

    @FXML
    private Button returnButton;

    int side = 0;

    @FXML
    private GridPane gridPane;

    //private TicTacToe game;
    private NetworkClient networkClient;

    public TicTacToeGameController(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    @FXML
    private void initialize() throws IOException
    {
        fillGrid();
        // game = new TicTacToe(this);
        returnButton.setOnAction(value ->
        {
            switchScene(returnButton.getScene(), System.getProperty("user.dir") + "/Resources/MainMenuv1.fxml", new MainMenuController());
        });
    }

    /*public void setOutputText()
    {
        if(game.computerPlays())
        {
            outputText.setText("Opponents' turn");
        }
        else
        {
            outputText.setText("Your turn");
        }
        
    }*/

    private void fillGrid()
    {
        for(int x = 0; x < 3; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                addNode(x, y);
            }
        }
    }

    private void addNode(int x, int y)
    {
        ImageView node = new ImageView();
        node.setPickOnBounds(true);
        node.setOpacity(0);
        node.setFitHeight(30);
        node.setFitWidth(30);
        node.setOnMouseClicked(value -> {
            if(node.getOpacity() < 1)
            {
                // todo
                Image tmp = side==0 ? image1 : image2;
                node.setImage(tmp);
                node.setOpacity(1);
                side= side==0 ? 1 : 0;
            }
        });
        gridPane.add(node, x, y);
        GridPane.setHalignment(node, HPos.CENTER);
    }

    public void setNode(final int row, final int column, int side)
    {
        ImageView node = (ImageView)getNodeByRowColumnIndex(row, column, gridPane);
        if(side == 0)
        {
            node.setImage(image1);
            node.setOpacity(1);
        }
        else if(side == 1)
        {
            node.setImage(image2);
            node.setOpacity(1);
        }
    }

    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
    
        for (Node node : childrens) {
            try 
            {
                if(GridPane.getRowIndex(node) != null)
                {
                    if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) 
                    {
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
}
