import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import AI.AI;
import AI.AIv2;
import AI.AIBest;
import TicTacToe.TicTacToeGameV2;
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

    Image image1;
    Image image2;

    private TicTacToeGameV2 t;
    private AI ai;

    @FXML
    private Text outputText;

    @FXML
    private Button returnButton;

    @FXML
    private GridPane gridPane;

    private NetworkClient networkClient;

    public TicTacToeGameController(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    @FXML
    private void initialize() throws IOException
    {
        FileInputStream inputstream = new FileInputStream(System.getProperty("user.dir") + "/resources/O.png");
        image1 = new Image(inputstream);
        inputstream = new FileInputStream(System.getProperty("user.dir") + "/resources/X.png");
        image2 = new Image(inputstream);
        inputstream.close();
        fillGrid();
        returnButton.setOnAction(value ->
        {
            switchScene(returnButton.getScene(), System.getProperty("user.dir") + "/Resources/MainMenuv1.fxml", new MainMenuController());
        });
        System.out.println("*** new AI.Game ***\n");
        t = new TicTacToeGameV2();
        ai = new AI(t);
        if (t.computerPlays())
        {
            computerMove();
            System.out.println("I start:\n");
        }        
        else
            System.out.println("You start:\n");
    }

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

    
    private void playerMove(int x, int y) 
    {
        setNode(y, x, t.side);
        t.playMove(y, x, t.side);
        if(!t.gameOver())
        {
            computerMove();
            if(t.gameOver())
            {
                outputText.setText("Game over " + t.winner() + " wins");
            }
        }
        else
        {
            outputText.setText("Game over " + t.winner() + " wins");
        }
        
    }

    private void computerMove()
    {
        int move = aiMove();
        setNode(move / 3, move % 3, t.side);
        t.playMove(move / 3, move % 3, t.side);
    }
    
    private int aiMove() {
        if (t.boardIsEmpty(t.getBoard())) {
            return 0;
        } else {
            AIBest aiBest = ai.chooseMove(t.COMPUTER);
            return aiBest.row * 3 + aiBest.column;
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
                if(!t.computerPlays())
                {
                    if(!t.gameOver())
                    {
                        playerMove(x, y);
                        if(t.gameOver())
                        {
                            outputText.setText("Game over " + t.winner() + " wins");
                        }
                    }
                }
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
            node.setImage(image2);
            node.setOpacity(1);
        }
        else if(side == 1)
        {
            node.setImage(image1);
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
