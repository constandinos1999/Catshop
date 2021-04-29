package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;


/**
 * View of the model
 * @author  M A Smith (c) June 2014  
 */
public class CashierView implements Observer
{
  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels
  
  private static final String CHECK  = "Check";
  private static final String BUY    = "Buy";
  private static final String BOUGHT = "Bought";

  private final Label      theAction  = new Label();
  private final TextField  theInput   = new TextField();
  private final TextArea   theOutput  = new TextArea();
  private final Button     theBtCheck = new Button( CHECK );
  private final Button     theBtBuy   = new Button( BUY );
  private final Button     theBtBought= new Button( BOUGHT );

  private StockReadWriter theStock     = null;
  private OrderProcessing theOrder     = null;
  private CashierController cont       = null;
  
  /**
   * Construct the view
   * @param stage   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-coordinate of position of window on screen 
   * @param y     y-coordinate of position of window on screen  
   */
          
  public CashierView(  Stage stage,  MiddleFactory mf, int x, int y  )
  {
    try                                           // 
    {      
      theStock = mf.makeStockReadWriter();        // Database access
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    stage.setWidth( W ); // Set Window Size
    stage.setHeight( H );
    stage.setX( x );  // Set Window Position
    stage.setY( y );

//    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

    theBtCheck.setPrefSize(  80, 40 );    // Check Button
    theBtCheck.setOnAction( event->cont.doCheck( theInput.getText() ) ); // Call back code

    theBtBuy.setPrefSize(  80, 40 );      // Buy button
    theBtBuy.setOnAction( event -> cont.doBuy() );

    theBtBought.setPrefSize(  80, 40 );   // Clear Button
    theBtBought.setOnAction(                  // Call back code
            event -> cont.doBought() );

    theAction.setPrefSize( 270, 20 );       // Message area
    theAction.setText( "Welcome" );                        // Blank

    theInput.setPrefSize( 270, 40 );         // Input Area
    theInput.setText("");                           // Blank

    theOutput.setPrefSize( 270, 160 );          // Scrolling pane
    theOutput.setText( "" );                        //  Blank
//    theOutput.setFont( f );                         //  Uses font

    GridPane buttonPane = new GridPane(); // button Pane
    buttonPane.addColumn(0, theBtCheck, theBtBuy, theBtBought);
    buttonPane.setVgap(10); // Vertical Spacing

    GridPane infoPane = new GridPane();
    infoPane.addColumn(0, theAction, theInput, theOutput);
    infoPane.setVgap(10);

    HBox root = new HBox();
    root.setSpacing(10);   //Setting the space between the nodes of a root pane

    ObservableList rootList = root.getChildren(); // retrieving the observable list of the root pane
    rootList.addAll(buttonPane, infoPane); // Adding all the nodes to the observable list


    // Set the Size of the GridPane
    root.setMinSize(700, 500);
    // Set style
    String rootStyle = "-fx-padding: 10;-fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5;" +
            "-fx-border-radius: 5; -fx-border-color: blue; -fx-background-color: #b4fcb4;";
    String buttonStyle = "-fx-background-color: #71fc48; -fx-text-fill: black;";

    root.setStyle(rootStyle);
    theBtCheck.setStyle(buttonStyle);
    theBtBuy.setStyle(buttonStyle);
    theBtBought.setStyle(buttonStyle);

    Scene scene = new Scene(root);  // Create the Scene
    stage.setScene(scene); // Add the scene to the Stage

    theInput.requestFocus();                        // Focus is here
  }

  /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CashierController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg )
  {
    CashierModel model  = (CashierModel) modelC;
    String      message = (String) arg;
    theAction.setText( message );
    Basket basket = model.getBasket();
    if ( basket == null )
      theOutput.setText( "Customers order" );
    else
      theOutput.setText( basket.getDetails() );
    
    theInput.requestFocus();               // Focus is here
  }

}
