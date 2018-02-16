import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Box class to populate the minesweeper grid
 * @author Bruce Zou
 *
 */
public class Box extends Parent {
	
  Rectangle rectBox;
  Text rectText;
  boolean isBomb = false;
  int bombCount = 0;
  int posRow;
  int posCol;
  boolean isCovered = true;
  boolean isFlagged = false;
  ImageView imgFlag = null;
  ImageView imgBomb = null;
  Group grp = new Group();
  
  /**
   * Constructor for the Box
   * @param w width of the rectangle to draw
   * @param h height of the rectangle to draw
   */
  public Box(int w, int h) {
    rectBox = new Rectangle(w, h);
    rectBox.setFill(Color.web("#009925"));
    rectText = new Text(w / 2, h / 2, new Integer(this.bombCount).toString());
    rectText.setFill(Color.RED);
    rectText.setFont(new Font("Arial", 15));
    rectText.setVisible(false);
    
    grp.getChildren().add(rectBox);
    getChildren().add(grp);
  }
  
  /**
   * Resets all the values of the box to the original values
   */
  public void resetBox() {
    isBomb = false;
    bombCount = 0;
    isCovered = true;
    isFlagged = false;
    
    rectBox.setFill(Color.web("#009925"));
    rectText.setFill(Color.RED);
    rectText.setFont(new Font("Arial", 15.0D));
    rectText.setVisible(false);
    
    grp.getChildren().remove(this.rectText);
    grp.getChildren().remove(this.imgBomb);
    grp.getChildren().remove(this.imgFlag);
  }
  
  /**
   * Makes a box a mine
   */
  public void setBombBox() {
    if ((!isBomb)) {
      isBomb = true;
      imgBomb = new ImageView(new Image(Main.class.getResourceAsStream("images/Shroom.png")));
      imgBomb.setFitWidth(rectBox.getWidth());
      imgBomb.setFitHeight(rectBox.getHeight());
      imgBomb.setVisible(false);
      grp.getChildren().add(imgBomb);
    }
  }
  
  /**
   * Uncovers the box to show the bomb count
   */
  public void showBombCount()
  {
    if ((!rectText.isVisible()) && (!isBomb))
    {
      setCovered(false);
      rectText.setText(new Integer(bombCount).toString());
      grp.getChildren().add(rectText);
      rectText.setVisible(true);
      isCovered = false;
    }
  }
  
  /**
   * Explodes the bomb revealing the mine
   */
  public void explodeBomb()
  {
    if (isBomb)
    {
      imgBomb.setVisible(true);
      if (isFlagged) {
        imgFlag.setVisible(false);
      }
      imgBomb.setImage(new Image(Grid.class.getResourceAsStream("images/Shroom.png")));
    }
  }
  
  //Getters and Setters below
  public void setBoxPosition(int row, int col)
  {
    posRow = row;
    posCol = col;
  }
  
  public int getBoxRow()
  {
    return posRow;
  }

  public int getBoxCol()
  {
    return posCol;
  }
  
  public void setBombCount(int count)
  {
    bombCount = count;
  }
  
  public void setCovered(boolean covered)
  {
    isCovered = covered;
    if (isCovered) {
      rectBox.setFill(Color.web("#009925"));
    } else {
      rectBox.setFill(Color.web("#414A4C"));
    }
  }

  public boolean getIsCovered()
  {
    return isCovered;
  }
  
  public boolean getIsBomb()
  {
    return isBomb;
  }
  
  public int getBombCount()
  {
    return bombCount;
  }
  
  public boolean getIsFlagged()
  {
    return isFlagged;
  }
  
  
  public void setFlagged(boolean flagged)
  {
    if (isCovered)
    {
      isFlagged = flagged;
      //Adds the flag
      if (isFlagged)
      {
        imgFlag = new ImageView(new Image(Grid.class.getResourceAsStream("images/PinkWard.png")));
        imgFlag.setFitHeight(rectBox.getHeight());
        imgFlag.setFitWidth(rectBox.getWidth());
        imgFlag.setVisible(isFlagged);
        grp.getChildren().add(imgFlag);
      }
      else //removes the flag
      {
        grp.getChildren().remove(imgFlag);
        imgFlag = null;
      }
    }
  }
  
  public void setBoxColor(Paint paintBox)
  {
    rectBox.setFill(paintBox);
  }
}
