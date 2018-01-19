package game_stp20;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RunGame extends Application {
	// values that won't change
	public static final String TITLE = "Wall Ball";
	public static final int SIZE = 600;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1 / FRAMES_PER_SECOND;
	public static final Color BACKGROUND1 = Color.rgb(170, 203, 255);
	public static final Color BACKGROUND2 = Color.rgb(168, 219, 176);
	public static final Color BACKGROUND3 = Color.rgb(196, 109, 109);
	public static final String BOUNCER_IMAGE = "ball.gif";
	public static final String PADDLE_IMAGE = "paddle.gif";
	
	// values that could change
	private Scene myScene;
	private ImageView paddle;
	private ImageView paddle2;
	private ImageView Ball;
	private ImageView Ball2;
	private int paddleSpeed;
	
	@Override
	public void start (Stage stage){
		myScene = setupGame(SIZE, SIZE, BACKGROUND1);
		
		stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();
        // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                      e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
	}
	
	private Scene setupGame(int width, int height, Color background){
		// to hold everything 
		Group root = new Group();
		// to view the objects in the Scene
		Scene scene = new Scene(root, width, height, background);
		// make shapes
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
		Image paddlei = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
		Ball = new ImageView(image);
		Ball2 = new ImageView(image);
		paddle = new ImageView(paddlei);
		// set locations of objects
		paddle.setX(width / 2 - paddle.getBoundsInLocal().getWidth() / 2);
		paddle.setY(SIZE - 75);
		// add objects to the group
		root.getChildren().add(Ball);
		root.getChildren().add(paddle);
		// set paddle speed
		paddleSpeed = 10;
		// when input is detected
		scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        
        return scene;
	}
	
	private void step(double elapsedTime){
		
	}
	
	private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT & paddle.getX() < SIZE - paddle.getBoundsInLocal().getWidth()) {
            paddle.setX(paddle.getX() + paddleSpeed);
        }
        else if (code == KeyCode.LEFT & paddle.getX() > 0) {
            paddle.setX(paddle.getX() - paddleSpeed);
        }
    }
	
	public static void main (String[] args) {
        launch(args);
    }

	
}
