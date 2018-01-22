package game_stp20;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RunGame extends Application {
	// values that won't change
	public static final String TITLE = "Wall Ball";
	public static final int SIZE = 700;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final Color[] BACKGROUNDS = {Color.rgb(170, 203, 255), Color.rgb(168, 219, 176), Color.rgb(196, 109, 109)};
	public static final String BOUNCER_IMAGE = "ball.gif";
	public static final String paddle_IMAGE = "paddle.gif";
	public static final String BRICK1_IMAGE = "brick1.gif";
	public static final String BRICK2_IMAGE = "brick3.gif";
	public static final String BRICK3_IMAGE = "brick5.gif";
	public static final String BRICK4_IMAGE = "brick10.gif";
	public static final String FINISH_LINE = "finish_line.gif";

	// values needed globally
	private Scene[] myScenes = new Scene[3];
	private ImageView[] paddle = new ImageView[2];
	private ImageView[] Ball = new ImageView[2];
	private int paddleSpeed = 20;
	private int[] Xvelocity = new int[2];
	private int[] Yvelocity = new int[2];
	private Random random = new Random();
	private Stage myStage;
	private int level = 0;
	private ImageView[][] brick = new ImageView[10][10];
	private Group myGroup;
	private boolean hitHorizontal = false;
	private boolean hitVertical = false;
	private Image brick1i = new Image(getClass().getClassLoader().getResourceAsStream(BRICK1_IMAGE));
	private Image brick2i = new Image(getClass().getClassLoader().getResourceAsStream(BRICK2_IMAGE));
	private Image brick3i = new Image(getClass().getClassLoader().getResourceAsStream(BRICK3_IMAGE));
	private Image brick4i = new Image(getClass().getClassLoader().getResourceAsStream(BRICK4_IMAGE));
	private GameConstructor constructor = new GameConstructor();
	private GameObject game = new GameObject();
	private Text score;
	private Text lives;
	
	
	@Override
	public void start (Stage stage){
		myStage = stage;
		game.reset();
		myScenes[0] = setupGame(SIZE, SIZE, BACKGROUNDS[0]);
		
		myStage.setScene(myScenes[0]);
        myStage.setTitle(TITLE);
        myStage.show();
        // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
	}
	
	private Scene setupGame(int width, int height, Color background){
		// to hold everything 
		myGroup = new Group();
		// to view the objects in the Scene
		Scene scene = new Scene(myGroup, width, height, background);
		// make shapes
		Image finish_lineI = new Image(getClass().getClassLoader().getResourceAsStream(FINISH_LINE));
		ImageView finish_line = new ImageView(finish_lineI);
		Ball[0] = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE)));
		Ball[1] = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE)));
		paddle[0] = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(paddle_IMAGE)));
		
		int[][] layout = constructor.construct(0);
		
		for (int i = 0; i < 10 ; i++){
			for(int j = 0; j < 10; j++){
				switch (layout[i][j]){
				case 1: 
					brick[i][j] = new ImageView(brick1i);
					break;
				case 2:
					brick[i][j] = new ImageView(brick2i);
					break;
				case 3:
					brick[i][j] = new ImageView(brick3i);
					break;
				case 4:
					brick[i][j] = new ImageView(brick4i);
					break;
				}
				brick[i][j].setX(70 * i);
				brick[i][j].setY(20 * j + 75);
				myGroup.getChildren().add(brick[i][j]);
			}
		}
		// setup score
		Text scoreText = new Text(500, 30, "Score: ");
		Text livesText = new Text(5, 30, "Lives Left: ");
		score = new Text(565, 30, Integer.toString(game.getScore()));
		lives = new Text(107, 30, Integer.toString(game.getLives()));
		scoreText.setFont(new Font(20));
		livesText.setFont(new Font(20));
		score.setFont(new Font(20));
		lives.setFont(new Font(20));
		
		// set locations of objects
		paddle[0].setX(width / 2 - paddle[0].getBoundsInLocal().getWidth() / 2);
		paddle[0].setY(SIZE - 75);
		Ball[0].setX(width / 2 - Ball[0].getBoundsInLocal().getWidth() / 2);
		Ball[0].setY(height / 2 - Ball[0].getBoundsInLocal().getHeight() / 2);
		finish_line.setX(0);
		finish_line.setY(50);
		// set ball starting velocity
		Xvelocity[0] = random.nextInt(120) - 60;
		Yvelocity[0] = 150;
		// add objects to the group
		myGroup.getChildren().add(Ball[0]);
		myGroup.getChildren().add(paddle[0]);
		myGroup.getChildren().add(finish_line);
		myGroup.getChildren().add(scoreText);
		myGroup.getChildren().add(livesText);
		myGroup.getChildren().add(score);
		myGroup.getChildren().add(lives);
		// when input is detected
		scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        
        return scene;
	}
	
	private void step(double elapsedTime){
		// check location of bouncer, reverse velocity if bounce needed
    	if (Ball[0].getX() > SIZE - Ball[0].getBoundsInLocal().getWidth() | Ball[0].getX() < 0){
    		Xvelocity[0] = Xvelocity[0] * -1;
    	}
    	if (Ball[0].getY() < 0){
    		Yvelocity[0] = Yvelocity[0] * -1;
    	}
    	// check for collision with paddle[0]
    	if (paddle[0].getBoundsInParent().intersects(Ball[0].getBoundsInParent())){
    		Yvelocity[0] = Yvelocity[0] * -1;
    		Xvelocity[0] = random.nextInt(280) - 140; 		
    	}
    	// check for brick collision
    	brickCollision();
    	// update score and lives
    	update();
        // update attributes
        Ball[0].setX(Ball[0].getX() + Xvelocity[0] * elapsedTime);
        Ball[0].setY(Ball[0].getY() + Yvelocity[0] * elapsedTime); 
        
	}
	
	private void update(){
		score.setText(Integer.toString(game.getScore()));
		lives.setText(Integer.toString(game.getLives()));
	}
	
	private void brickCollision(){
		if(Ball[0].getY() < 300){
    		for (int i = 0; i < 10; i++){
    			for(int j = 0; j < 10; j++){
    				if (brick[i][j] != null){ // thought you could do these if statements inline but got null pointer exception even though null check was first
    					if (brick[i][j].getBoundsInParent().intersects(Ball[0].getBoundsInParent())){
    						hitHorizontal = (Ball[0].getBoundsInParent().getMinX() <= brick[i][j].getBoundsInParent().getMinX() - 2.5 
    								& Xvelocity[0] > 0) | (Ball[0].getBoundsInParent().getMaxX() >= brick[i][j].getBoundsInParent().getMaxX()
    								+ 2.5 & Xvelocity[0] < 0);
    				    	hitVertical = (Ball[0].getBoundsInParent().getMinY() <= brick[i][j].getBoundsInParent().getMinY() - 2.5 
    				    			& Yvelocity[0] > 0) | (Ball[0].getBoundsInParent().getMaxY() >= brick[i][j].getBoundsInParent().getMaxY()
    				    			+ 2.5 & Yvelocity[0] < 0);
    				    	if(brick[i][j].getImage() == brick2i){
    				    		bomb(i, j);
    				    	}
    				    	else if(brick[i][j].getImage() == brick3i){
    				    		lightning(j);
    				    	}
    				    	else if(brick[i][j].getImage() == brick4i){
    				    		game.changeScore(-10);
    				    		myGroup.getChildren().remove(brick[i][j]);
    				    		brick[i][j] = null;
    				    	}
    				    	else{
    				    		game.changeScore(10);
    				    		myGroup.getChildren().remove(brick[i][j]);
    				    		brick[i][j] = null;
    				    	}
    						
    					}
					}
				}
			}
		}
    	// set direction change
    	if (hitHorizontal){
    		Xvelocity[0] = Xvelocity[0] * -1;
    		hitHorizontal = false;
    	}
    	if (hitVertical){
    		Yvelocity[0] = Yvelocity[0] * -1;
    		hitVertical = false;
    	}
	}
	
	private void bomb(int i, int j){
		for(int k = i - 1; k < i + 2; k++){
			for(int h = j - 1; h < j + 2; h++){
				if(k > -1 & k < 10 & h > -1 & h < 10){
					if(brick[k][h] != null){
						game.changeScore(10);
						myGroup.getChildren().remove(brick[k][h]);
						brick[k][h] = null;
					}
				}
			}
		}
	}
	
	private void lightning(int j){
		for(int i = 0; i < 10; i++){
			if(brick[i][j] != null){
				game.changeScore(10);
				myGroup.getChildren().remove(brick[i][j]);
				brick[i][j] = null;
			}
		}
	}
	
	private void nextLevel(){
		level++;
		myScenes[level] = setupGame(SIZE, SIZE, BACKGROUNDS[level]);
		myStage.setScene(myScenes[level]);
	}
	
	private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT & paddle[0].getX() < SIZE - paddle[0].getBoundsInLocal().getWidth()) {
            paddle[0].setX(paddle[0].getX() + paddleSpeed);
        }
        else if (code == KeyCode.LEFT & paddle[0].getX() > 0) {
            paddle[0].setX(paddle[0].getX() - paddleSpeed);
        }
        else if (code == KeyCode.DIGIT1){
        	level = -1;
        	nextLevel();
        }
        else if (code == KeyCode.DIGIT2){
        	level = 0;
        	nextLevel();
        }
        else if (code == KeyCode.DIGIT3){
        	level = 1;
        	nextLevel();
        }
        else if (code == KeyCode.R){
        	level--;
        	nextLevel();
        }
        else if(code == KeyCode.E){
        	game.changeLives(5);
        }
    }
	
	public static void main (String[] args) {
        launch(args);
    }

	
}
