import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class OthelloGameController extends GUIController
{
    @FXML
    private GridPane gridPane;

    int side = 0;

    private NetworkClient networkClient;

    public OthelloGameController(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    @FXML
    private void initialize()
    {
        fillGrid();
        //gridpane.set
        
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
                Color tmp = side==0 ? Color.BLACK : Color.WHITE;
                node.setFill(tmp);
                node.setOpacity(1);
                side = side==0 ? 1 : 0;
            }
        });
        gridPane.add(node, x, y);
        GridPane.setHalignment(node, HPos.CENTER);
    }

    private void changeNodeColor(int x, int y, Color color)
    {
        Node node = getNodeByRowColumnIndex(y, x, this.gridPane);
        Circle circle = (Circle)node;
        circle.setFill(color);
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