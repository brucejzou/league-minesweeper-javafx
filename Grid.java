import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.*;

/**
 * 
 * Grid Class for displaying the Minesweeper gamefield
 * @author Bruce Zou
 */
public class Grid extends Parent {
  int col;
  int row;
  int countBomb;
  int countFlagged;
  Box[][] boxGrid;
  VBox vboxLayout;
  ScoreBoard scoreBoard;
  boolean isGameFinished;

  
  /**
   * Scoreboard class to display text above the grid field
   * @author Bruce Zou
   *
   */
  private class ScoreBoard extends Parent {
    Text txtMinesRemaining = new Text(); //Text telling how many mines are left
    Text txtWinMessage = new Text(); //Top Text s
    Button btnRestart = new Button("RESTART GAME");
    
    /**
     * Constructor for ScoreBoard.
     * Sets all the styling and text for the Texts and Buttons
     */
    public ScoreBoard() {
      //Styling for the Textbox
      HBox shroomsBox = new HBox(3);
      Text txtCaptionMinesRemaining = new Text("SHROOMS REMAINING: ");
      txtCaptionMinesRemaining.setFill(Color.rgb(254, 65, 100));
      txtCaptionMinesRemaining.setFont(new Font("Impact", 18));
      txtMinesRemaining.setFill(Color.rgb(254, 65, 100));
      txtMinesRemaining.setFont(new Font("Impact", 18));
      
      //Add the number of mines remaining to the textbox
      shroomsBox.getChildren().addAll(new Node[] { txtCaptionMinesRemaining, txtMinesRemaining });
      
      //Set Styling for the restart button
      HBox buttonBox = new HBox(3);
      btnRestart.setFont(new Font("Impact", 15));
      
      //Event Handler for button press
      btnRestart.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event){
          //Resets values to initial state.
          resetGame();
          isGameFinished = false;
          setTextWinMessage("Try to gank Teemo. Clear all the Shrooms.", Color.web("#FFFF33"));
        }
      });
      buttonBox.getChildren().add(btnRestart);
      
      //Styling and setting text of instructions
      HBox instructBox = new HBox(3);
      txtWinMessage.setFont(new Font("Impact", 17));
      setTextWinMessage("Try to gank Teemo. Clear all the Shrooms.", Color.web("#FFFF33"));
      instructBox.getChildren().add(txtWinMessage);
      
      VBox headerBox = new VBox(3);
      headerBox.getChildren().addAll(new Node[] { buttonBox, instructBox, shroomsBox });
      
      getChildren().add(headerBox);
      headerBox.setAlignment(Pos.CENTER);
    }
    
    /**
     * Sets the text value of txtMinesRemaining to the number of mines left
     * @param mineRemaining integer number of mines left
     */
    public void setTextMinesRemaining(int mineRemaining) {
      txtMinesRemaining.setText(Integer.toString(mineRemaining));
    }
    
    /**
     * Sets the text value of txtWinMessage
     * @param message the String to set the text to
     * @param textFill the color to set the text to
     */
    public void setTextWinMessage(String message, Color textFill)
    {
      txtWinMessage.setText(message);
      txtWinMessage.setFill(textFill);
      btnRestart.setVisible(true);
    }
  }
  
  
  /**
   * Explodes all the bombs on the grid
   */
  private void explodeBombs() {
    for (int i = 0; i < row; i++) {
      for (int k = 0; k < col; k++) {
        if (boxGrid[i][k].getIsBomb()) {
          boxGrid[i][k].explodeBomb();
        }
      }
    }
  }
  
  /**
   * Uncovers the bomb counts around the clicked box
   * @param clickedBox the box object that was clicked
   */
  private void showBombCounts(Box clickedBox) {
	  //Show the bomb count if its not 0
	  if(clickedBox.getBombCount()>0)
		  clickedBox.showBombCount();
	  //Branch out from current box if the bomb count is 0
	  else if(clickedBox.getBombCount() == 0) {
		  Queue<Box> boxQ = new LinkedList<Box>(); //Queue of boxes to check
		  boxQ.add(clickedBox);
		  while(!boxQ.isEmpty()) {
			  Box curr = boxQ.remove();
			  curr.setCovered(false);
			  //Show bomb count if its not 0
			  if(curr.getBombCount()>0)
				  curr.showBombCount();
			  //Add all neighbors to queue if bomb count of current box is 0
			  else if(curr.getBombCount()==0) {
				  for(int i=Math.max(0,curr.getBoxRow()-1);i<=Math.min(row-1, curr.getBoxRow()+1);i++) {
					  for(int j=Math.max(0, curr.getBoxCol()-1);j<=Math.min(col-1, curr.getBoxCol()+1);j++) {
						  if(boxGrid[i][j].getIsCovered()) //only add if its covered (not visited before)
							  boxQ.add(boxGrid[i][j]);
					  }
				  } 
			  }
		  }
	  }
  }
  
  /**
   * Checks if all the bombs on the grid are flagged.
   * If so, end game and display win message.
   */
  public void checkBombsFlagged() {
	boolean keepChecking = true;
    for (int i = 0; i < row && keepChecking; i++) {
      for (int k = 0; k < col && keepChecking; k++) {
        if ((boxGrid[i][k].getIsBomb()) && (!boxGrid[i][k].getIsFlagged())) {
        	keepChecking = false;
        }
      }
    }
    if(keepChecking) {
	    scoreBoard.setTextWinMessage("You  Won!!! Congrats!! All mines are flagged.", Color.HOTPINK);
	    isGameFinished = true;
	}
  }
  
  public Grid(int cols, int rows) {
	//Setting variable values
    col = cols;
    row = rows;
    isGameFinished = false;
    boxGrid = new Box[row][col];
    scoreBoard = new Grid.ScoreBoard();
    vboxLayout = new VBox(2);
    vboxLayout.getChildren().add(scoreBoard);
    
    setEventsAndGrid(vboxLayout);
    
    getChildren().add(vboxLayout);
    
    setGamePuzzle();
  }
  
  /**
   * Creates all the boxes on the grid and sets the MouseEvents
   * @param vboxlayout the vertical box to add the Boxes to.
   */
  private void setEventsAndGrid(VBox vboxlayout) {
	  //Loop through the size of the grid
	  for (int i = 0; i < row; i++) {
	      HBox hboxLayout = new HBox(2);
	      for (int k = 0; k < col; k++){
	        boxGrid[i][k] = new Box(35, 35);
	        boxGrid[i][k].setBoxPosition(i, k);
	        
	        boxGrid[i][k].setOnMouseClicked(new EventHandler<MouseEvent>() {
	        	
	          public void handle(MouseEvent event) {
	            Box clickedBox = (Box)event.getSource();
	            if (!isGameFinished) { //Dont do anything if game is over
		            if (event.getButton() == MouseButton.PRIMARY) { //Left Click to reveal box
		              if ((clickedBox.getIsBomb()) && (!clickedBox.getIsFlagged())) { //Explode bombs if the box is a bomb and not flagged
		                explodeBombs();
		                isGameFinished = true;
		                scoreBoard.setTextWinMessage("Gank Failed. You Lose!", Color.RED);
		              }
		              else if (!clickedBox.getIsFlagged()) //Uncover boxes if its not a bomb and not flagged
		                showBombCounts(clickedBox);
		            }
		            else if (event.getButton() == MouseButton.SECONDARY) { //Right Click to set flag
		            	if(clickedBox.getIsFlagged()) {
		            		clickedBox.setFlagged(!clickedBox.getIsFlagged()); //Unflag box
		            		countFlagged -= 1;
		            	}
		            	else if(!clickedBox.getIsFlagged() && countFlagged<countBomb) { //Makes sure you can only flag as many boxes as there are mines
		            		clickedBox.setFlagged(!clickedBox.getIsFlagged());
		            		countFlagged += 1;
		            		checkBombsFlagged();
		            	}
	            		scoreBoard.setTextMinesRemaining(countBomb - countFlagged);

		            }
	            }
	          }
	          
	        });
	        
	        //Event when mouse is hovering the box
	        boxGrid[i][k].setOnMouseEntered(new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) {
	            Box clickedBox = (Box)event.getSource();
	            //Darken the box if the game isn't over, the box isn't flagged and it is covered.
	            if (!(isGameFinished || clickedBox.isFlagged) && clickedBox.getIsCovered()) {
	              clickedBox.setBoxColor(Color.web("#009925").darker());
	            }
	          }
	        });
	        
	        //Event when the mouse stops hovering the box
	        boxGrid[i][k].setOnMouseExited(new EventHandler<MouseEvent>() {
	          public void handle(MouseEvent event) {
	            Box clickedBox = (Box)event.getSource();
	            //Undarken the box if game isn't over and the box is covered
	            if (!isGameFinished && clickedBox.getIsCovered()) {
	              clickedBox.setBoxColor(Color.web("#009925"));
	            }
	          }
	        });
	        
	        //Add the box to the row
	        hboxLayout.getChildren().add(boxGrid[i][k]);
	      }
	      //Add the row to the vertical box
	      vboxLayout.getChildren().add(hboxLayout);
	    }
  }
  
  /**
   * Resets the game by resetting every box and 
   * then calling setGamePuzzle to set a different configuration of mines
   */
  private void resetGame() {
    for (int i = 0; i < this.row; i++) 
      for (int k = 0; k < this.col; k++) 
        this.boxGrid[i][k].resetBox();
    
    setGamePuzzle();
  }
  
  
  /**
   * Configures the game puzzle
   * Randomly sets which boxes should be mines
   * Sets the bomb counts of boxes based on the set mine locations
   */
  private void setGamePuzzle() {
    countBomb = (int)(col * row * 15.0 / 100); //About 15% of Boxes will be a mine, get that number
    
    //Randomly choose boxes till that number of boxes are mines
    int count = 0;
    while(count < countBomb) {
        int randRow = (int)(Math.random() * row);
        int randCol = (int)(Math.random() * col);
        if(!boxGrid[randRow][randCol].getIsBomb()) {
        	boxGrid[randRow][randCol].setBombBox();
        	count++;
        }
    }

    countFlagged = 0; //Reset count of flagged boxes
    scoreBoard.setTextMinesRemaining(countBomb - countFlagged); //Set text of number of bombs left
    
    //Loops through the boxes to set their bomb counts
    for (int i = 0; i < this.row; i++) {
      for (int k = 0; k < this.col; k++) {
    	Box curr = boxGrid[i][k];
    	int bombCount = 0;
        if (!curr.getIsBomb()) {
        	//Check all adjacent boxes
        	for(int a=Math.max(0,curr.getBoxRow()-1);a<=Math.min(row-1, curr.getBoxRow()+1);a++) {
				  for(int b=Math.max(0, curr.getBoxCol()-1);b<=Math.min(col-1, curr.getBoxCol()+1);b++) {
					  if(boxGrid[a][b].getIsBomb())
						  bombCount++; //increment count of bombs next to the box
				  }
        	}
        	curr.setBombCount(bombCount);
        }
      }
    }
  }
}
