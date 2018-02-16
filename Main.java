import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Minesweeper Main Start
 * @author Bruce Zou
 *
 */
public class Main extends Application {
  public static final Color backgroundColor = Color.color(57/255., 57/255., 57/255.);

  public static void main(String[] args) {
    Application.launch(args);
  }
  
  public void start(final Stage primaryStage) {
	//Create Menu
    BorderPane root = new BorderPane();
    VBox menu = new VBox();
    MenuBar mainMenu = new MenuBar();
    menu.getChildren().add(mainMenu);
    Menu gridSize = new Menu("Grid Size");
    mainMenu.getMenus().add(gridSize);
    MenuItem easy = new MenuItem("Early Game: 9x9");
    MenuItem med = new MenuItem("Mid Game: 16x16");
    MenuItem hard = new MenuItem("Late Game: 25x25");
    MenuItem custom = new MenuItem("Custom");
    gridSize.getItems().addAll(new MenuItem[] { easy, med, hard, custom });
    
    //Set Styling of start text
    Text startText = new Text("\n\nSelect a grid size to\n  start a new game.");
    startText.setStyle("-fx-font-family: Impact; -fx-font-size:30;");
    startText.setFill(Color.web("#00FF00"));
    startText.setLayoutX(15);
    startText.setLayoutY(5);
    
    //Sets the Title and Icon of the menu window
    primaryStage.setTitle("Menu");
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/Shroom.png")));
    
    //Creates the graphics of the window
    final Pane littleroot = new Pane();
    littleroot.getChildren().add(startText);
    littleroot.setBackground(new Background(new BackgroundFill[] { new BackgroundFill(backgroundColor, null, null) }));
    root.setCenter(littleroot);
    root.setTop(menu);
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
    
    //Handle the selection of the different menu options:
    
    //Easy: Grid size 9 x 9
    easy.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        Grid game = new Grid(9, 9);
        game.setLayoutX(15);
        game.setLayoutY(5);
        primaryStage.setTitle("Jungle-Early Game");
        littleroot.getChildren().clear();
        littleroot.getChildren().add(game);
        primaryStage.close();
        primaryStage.show();
      }
    });
    
    //Medium: Grid Size 16 x 16
    med.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event){
        Grid game = new Grid(16, 16);
        game.setLayoutX(15);
        game.setLayoutY(5);
        primaryStage.setTitle("Jungle-Mid Game");
        littleroot.getChildren().clear();
        littleroot.getChildren().add(game);
        primaryStage.close();
        primaryStage.show();
      }
    });
    
    //Hard: Grid Size 25 x 25
    hard.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        Grid game = new Grid(25, 25);
        game.setLayoutX(15);
        game.setLayoutY(5);
        primaryStage.setTitle("Jungle-Late Game");
        littleroot.getChildren().clear();
        littleroot.getChildren().add(game);
        primaryStage.close();
        primaryStage.show();
      }
    });

    //Custom Grid size
    custom.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event) {
    	//Create a popup window to prompt grid size input.
        final Stage popup = new Stage();
        VBox root2 = new VBox(20);
        Scene scenep = new Scene(root2);
        root2.setBackground(new Background(new BackgroundFill[] { new BackgroundFill(backgroundColor, null, null) }));
        popup.setResizable(false);
        popup.setTitle("Custom Grid");
        popup.getIcons().add(new Image(getClass().getResourceAsStream("images/Shroom.png")));
        
        //Get Grid size Values.
        Text instruc = new Text("  Enter a row size(0<row<46) and column\n      size(0<col<26) so that the grid is at \n     least 6 boxes large and click Create.");
        instruc.setStyle("-fx-font-family: Impact; -fx-font-size:20;");
        instruc.setFill(Color.web("#00FF00"));
        final IntegerTextField rowN = new IntegerTextField();
        final IntegerTextField colN = new IntegerTextField();
        Button create = new Button("Create Grid");
        root2.getChildren().addAll(new Node[] { instruc, rowN, colN, create });
        popup.setScene(scenep);
        popup.show();
        
        //Handle pressing of the button
        create.setOnAction(new EventHandler<ActionEvent>(){
          public void handle(ActionEvent event) {
            int row = 9;int col = 9; //defaults to 9 x 9
            //Makes sure the size is atleast 6
            if (rowN.getInt() * colN.getInt() > 6)
              if ((rowN.getInt() < 46) && (rowN.getInt() > 0)) //makes sure the row size is within the range
                row = rowN.getInt();
              if ((colN.getInt() < 26) && (rowN.getInt() > 0)) //makes sure the col size is within the range
                col = colN.getInt();
            
            //Create the grid of row x col.
            Grid game = new Grid(row, col);
            game.setLayoutX(15);
            game.setLayoutY(5);
            primaryStage.setTitle("Jungle-Custom");
            littleroot.getChildren().clear();
            littleroot.getChildren().add(game);
            primaryStage.close();
            primaryStage.show();
            popup.close();
          }
        });
      }
    });
  }
}
