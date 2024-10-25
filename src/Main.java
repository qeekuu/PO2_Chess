import javafx.application.Application;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application
{
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("ChessGame");


		StackPane layout = new StackPane();
		
		//SZACHOWNICA BOARD.java
		Board chessboard = new Board();
		layout.getChildren().add(chessboard);

		Scene scene = new Scene(layout, 960, 960);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);	

		// primaryStage.centerOnScreen();

		// Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		// primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
		// primaryStage.setY((primaryStage.getHeight() - primaryStage.getHeight()) / 2);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}

