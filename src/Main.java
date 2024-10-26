import javafx.application.Application;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application
{
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("ChessGame");


		StackPane layout = new StackPane();
		// layout.setAlignment(Pos.CENTER);
		// layout.setPadding(new Insets(80));

		//SZACHOWNICA BOARD.java
		Board chessboard = new Board();
		chessboard.setPrefSize(640, 640);
		layout.getChildren().add(chessboard);

		Scene scene = new Scene(layout, 900, 640, Color.BLACK);

		Image icon = new Image("/resources/chess.png");
		primaryStage.getIcons().add(icon);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
		// primaryStage.setResizable(false);	

		// Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		// primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
		// primaryStage.setY((primaryStage.getHeight() - primaryStage.getHeight()) / 2);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}

