/**
 * @author ricemery
 * https://gist.github.com/ricemery/4534910
 */
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class IntegerTextField
  extends TextField
{
  public IntegerTextField()
  {
    addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>()
    {
      public void handle(KeyEvent event)
      {
        if (!IntegerTextField.this.isValid(IntegerTextField.this.getText())) {
          event.consume();
        }
      }
    });
    textProperty().addListener(new ChangeListener<String>()
    {
      public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue)
      {
        if (!IntegerTextField.this.isValid(newValue)) {
          IntegerTextField.this.setText(oldValue);
        }
      }
    });
  }
  
  private boolean isValid(String value)
  {
    if ((value.length() == 0) || (value.equals("-"))) {
      return true;
    }
    try
    {
      Integer.parseInt(value);
      return true;
    }
    catch (NumberFormatException ex) {}
    return false;
  }
  
  public int getInt()
  {
    try
    {
      return Integer.parseInt(getText());
    }
    catch (NumberFormatException e) {}
    return 0;
  }
}
