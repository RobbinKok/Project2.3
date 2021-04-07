import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class OrthelloGameController extends GUIController
{
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

    //int side = 0;
    Reversie reversie;

    @FXML
    private void initialize()
    {
        fillGrid();
        reversie = new Reversie(this);
        //gridpane.set

        System.out.println(playerOne);
        //setScore(0, 3);
        //setScore(1, 96);
    }

    public void setScore(int blackScore, int whiteScore)
    {
            playerOne.setText("Black: " + blackScore);
            playerTwo.setText("White: " + whiteScore);
    }

    public void updateCurrentPlayer(int side)
    {
        if(side == 0)
        {
            currentPlayer.setText("Current Player: Black");
        }
        else
        {
            currentPlayer.setText("Current Player: White");
        }
    }

    public void addMove(int side, int x, int y)
    {
        if(side == 0)
        {
            movesList.getItems().add("Black to [" + x + "," + y + "]");
        }
        else
        {
            movesList.getItems().add("White to [" + x + "," + y + "]");
        }
    }

    private void fillGrid()
    {
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                addNode(x, y);
            }
        }
    }

    private void addNode(int x, int y)
    {
        Circle node = new Circle(0, 0, 10);
        node.setStroke(Color.BLACK);
        node.setOpacity(0);
        node.setOnMouseClicked(value -> {
            if(node.getOpacity() < 1)
            {
                /*Color tmp = side==0 ? Color.BLACK : Color.WHITE;
                node.setFill(tmp);
                node.setOpacity(1);
                side = side==0 ? 1 : 0;*/

                String[] coords = new String[]{String.valueOf(x), String.valueOf(y)};
                reversie.move(coords);
            }
        });
        gridPane.add(node, x, y);
        GridPane.setHalignment(node, HPos.CENTER);
    }

    public void changeNodeColor(int x, int y, Color color)
    {
        Node node = getNodeByRowColumnIndex(y, x, this.gridPane);
        Circle circle = (Circle)node;
        
        circle.setFill(color);
        node.setOpacity(1);
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