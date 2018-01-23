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
	private final Image brick1i = new Image(getClass().getClassLoader().getResourceAsStream("brick1.gif"));
	private final Image brick2i = new Image(getClass().getClassLoader().getResourceAsStream("brick3.gif"));
	private final Image brick3i = new Image(getClass().getClassLoader().getResourceAsStream("brick5.gif"));
	private final Image brick4i = new Image(getClass().getClassLoader().getResourceAsStream("brick10.gif"));
	private final Image brick5i = new Image(getClass().getClassLoader().getResourceAsStream("brick8.gif"));
	private final Image power1 = new Image(getClass().getClassLoader().getResourceAsStream("sizepower.gif"));
	private final Image power2 = new Image(getClass().getClassLoader().getResourceAsStream("pointspower.gif"));
	private final Image power3 = new Image(getClass().getClassLoader().getResourceAsStream("extraballpower.gif"));
	private final Image paddlei = new Image(getClass().getClassLoader().getResourceAsStream("paddle.gif"));
	private final Image paddle2i = new Image(getClass().getClassLoader().getResourceAsStream("paddle2.gif"));
	private final Image balli = new Image(getClass().getClassLoader().getResourceAsStream("ball.gif"));

	// values needed globally
	private Scene[] myScenes = new Scene[3];
	private ImageView paddle;
	private ImageView Ball;
	private ImageView paddle2;
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
	private GameConstructor constructor = new GameConstructor();
	private GameObject game = new GameObject();
	private long powerTime = 0;
	private int powerType = 0;
	private ImageView item;
	private Text levelnum;
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
		Ball = new ImageView(balli);
		paddle = new ImageView(paddlei);
		paddle2 = new ImageView(paddle2i);
		
		
		int[][] layout = constructor.construct(0);
		
		for (int i = 0; i < 10 ; i++){
			for(int j = 0; j < 10; j++){
				switch (layout[i][j]){
				case 1: 
					brick[i][j] = new ImageView(brick1i);
					game.changeBlock(1);;
					break;
				case 2:
					brick[i][j] = new ImageView(brick2i);
					game.changeBlock(1);;
					break;
				case 3:
					brick[i][j] = new ImageView(brick3i);
					game.changeBlock(1);;
					break;
				case 4:
					brick[i][j] = new ImageView(brick4i);
					break;
				case 5:
					brick[i][j] = new ImageView(brick5i);
					game.changeBlock(1);;
					break;
				default: 
					brick[i][j] = null;
					break;
					
				}
				if(brick[i][j] != null){
					brick[i][j].setX(70 * i);
					brick[i][j].setY(20 * j + 75);
					myGroup.getChildren().add(brick[i][j]);
				}
			}
		}
		// setup score
		Text scoreText = new Text(550, 30, "Score: ");
		Text livesText = new Text(5, 30, "Lives Left: ");
		Text levelText = new Text(300, 30, "Level: ");
		score = new Text(615, 30, Integer.toString(game.getScore()));
		lives = new Text(107, 30, Integer.toString(game.getLives()));
		levelnum = new Text(360, 30, Integer.toString(level + 1));
		levelText.setFont(new Font(20));
		levelnum.setFont(new Font(20));
		scoreText.setFont(new Font(20));
		livesText.setFont(new Font(20));
		score.setFont(new Font(20));
		lives.setFont(new Font(20));
		
		// set locations of objects
		paddle.setX(width / 2 - paddle.getBoundsInLocal().getWidth() / 2);
		paddle.setY(SIZE - 75);
		Ball.setX(width / 2 - Ball.getBoundsInLocal().getWidth() / 2);
		Ball.setY(height / 2 - Ball.getBoundsInLocal().getHeight() / 2);
		
		// add objects to the group
		myGroup.getChildren().add(Ball);
		myGroup.getChildren().add(paddle);
		myGroup.getChildren().add(scoreText);
		myGroup.getChildren().add(livesText);
		myGroup.getChildren().add(score);
		myGroup.getChildren().add(lives);
		myGroup.getChildren().add(levelnum);
		myGroup.getChildren().add(levelText);
		// when input is detected
		scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        
        return scene;
	}
	
	private void step(double elapsedTime){
		// check location of bouncer, reverse velocity if bounce needed
    	if (Ball.getX() > SIZE - Ball.getBoundsInLocal().getWidth() | Ball.getX() < 0){
    		Xvelocity[0] = Xvelocity[0] * -1;
    	}
    	if (Ball.getY() < 0){
    		Yvelocity[0] = Yvelocity[0] * -1;
    	}
    	// check for collision with paddle
    	if (paddle.getBoundsInParent().intersects(Ball.getBoundsInParent()) | paddle2.getBoundsInParent().intersects(Ball.getBoundsInParent())){
    		Yvelocity[0] = Yvelocity[0] * -1;
    		Xvelocity[0] = random.nextInt(280) - 140;
    		if(powerType == 2){
    			game.changeScore(5);
    		}
    	}
    	if (Ball.getY() > 750){
    		game.changeLives(-1);
    		resetBall();
    	}
    	if (game.getBlocks() == 0){
    		nextLevel();
    	}
    	if (item != null){
    		if (item.getBoundsInParent().intersects(paddle.getBoundsInParent()) | item.getBoundsInParent().intersects(paddle2.getBoundsInParent())){
    			powerup();
    		}
    	}
    	if (powerType != 0 & (System.currentTimeMillis() - powerTime) / 1000 > 15){
    		powerOff();
    	}
    	// check for brick collision
    	brickCollision();
    	// update score and lives
    	update();
    	// item velocity
    	if(item != null){
    		item.setY(item.getY() + 100 * elapsedTime);
    	}
        // update attributes
        Ball.setX(Ball.getX() + Xvelocity[0] * elapsedTime);
        Ball.setY(Ball.getY() + Yvelocity[0] * elapsedTime); 
        
	}
	
	private void update(){
		score.setText(Integer.toString(game.getScore()));
		lives.setText(Integer.toString(game.getLives()));
	}
	
	private void brickCollision(){
		if(Ball.getY() < 300){
    		for (int i = 0; i < 10; i++){
    			for(int j = 0; j < 10; j++){
    				if (brick[i][j] != null){ // thought you could do these if statements inline but got null pointer exception even though null check was first
    					if (brick[i][j].getBoundsInParent().intersects(Ball.getBoundsInParent())){
    						hitHorizontal = (Ball.getBoundsInParent().getMinX() <= brick[i][j].getBoundsInParent().getMinX() - 2.5 
    								& Xvelocity[0] > 0) | (Ball.getBoundsInParent().getMaxX() >= brick[i][j].getBoundsInParent().getMaxX()
    								+ 2.5 & Xvelocity[0] < 0);
    				    	hitVertical = (Ball.getBoundsInParent().getMinY() <= brick[i][j].getBoundsInParent().getMinY() - 2.5 
    				    			& Yvelocity[0] > 0) | (Ball.getBoundsInParent().getMaxY() >= brick[i][j].getBoundsInParent().getMaxY()
    				    			+ 2.5 & Yvelocity[0] < 0);
    				    	if(brick[i][j].getImage() == brick2i){
    				    		bomb(i, j);
    				    	}
    				    	else if(brick[i][j].getImage() == brick3i){
    				    		lightning(j);
    				    	}
    				    	else if(brick[i][j].getImage() == brick4i){
    				    		game.changeScore(-10);
    				    		destroy(i, j);
    				    	}
    				    	else if(brick[i][j].getImage() == brick5i){
    				    		releaseItem(i, j);
    				    	}
    				    	else{
    				    		game.changeScore(10);
    				    		destroy(i, j);
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
	private void releaseItem(int i, int j){
		game.changeScore(20);
		destroy(i, j);
		int type = 3; //random.nextInt(2) + 1;
		switch(type){
		case 1:
			item = new ImageView(power1);
			break;
		case 2:
			item = new ImageView(power2);
			break;
		case 3: 
			item = new ImageView(power3);
			break;
		}
		item.setX(70 * i + 29);
		item.setY(20 * j + 79);
		myGroup.getChildren().add(item);
	}
	
	private void bomb(int i, int j){
		for(int k = i - 1; k < i + 2; k++){
			for(int h = j - 1; h < j + 2; h++){
				if(k > -1 & k < 10 & h > -1 & h < 10){
					if(brick[k][h] != null){
						game.changeScore(10);
						destroy(k,h);
					}
				}
			}
		}
	}
	
	private void destroy(int i, int j){
		myGroup.getChildren().remove(brick[i][j]);
		brick[i][j] = null;
		game.changeBlock(-1);
	}
	
	private void lightning(int j){
		for(int i = 0; i < 10; i++){
			if(brick[i][j] != null){
				game.changeScore(10);
				destroy(i,j);
			}
		}
	}
	
	private void powerup(){
		powerTime = System.currentTimeMillis();
		if(item.getImage() == power1){
			Xvelocity[0] = Xvelocity[0] / 2;
			Yvelocity[0] = Yvelocity[0] / 2;
			powerType = 1;
		}
		else if(item.getImage() == power2){
			game.changeMult(2);
			powerType = 2;
		}
		else{
			powerType = 3;
			swapPaddle(0);
		}
		myGroup.getChildren().remove(item);
		item = null;
	}
	
	private void swapPaddle(int i){
		if(i == 0){
			paddle2.setX(paddle.getX() - 30);
			paddle2.setY(paddle.getY());
			paddle.setX(800);
			paddle.setY(800);
			myGroup.getChildren().remove(paddle);
			myGroup.getChildren().add(paddle2);
		}
		else{
			paddle.setX(paddle2.getX() + 30);
			paddle.setY(paddle2.getY());
			paddle2.setX(800);
			paddle2.setY(800);
			myGroup.getChildren().remove(paddle2);
			myGroup.getChildren().add(paddle);
		}
	}
	
	private void powerOff(){
		switch (powerType){
		case 1:
			Xvelocity[0] = Xvelocity[0] * 2;
			Yvelocity[0] = Yvelocity[0] * 2;
			break;
		case 2: 
			game.changeMult(1);
			break;
		case 3:
			swapPaddle(1);
			break;
		}
		powerType = 0;
	}
	
	private void nextLevel(){
		
		level++;
		myScenes[level] = setupGame(SIZE, SIZE, BACKGROUNDS[level]);
		myStage.setScene(myScenes[level]);
		resetBall();
	}
	
	private void resetBall(){
		Ball.setX(SIZE / 2 - Ball.getBoundsInLocal().getWidth() / 2);
		Ball.setY(SIZE / 2 - Ball.getBoundsInLocal().getHeight() / 2);
		Xvelocity[0] = 0;
		Yvelocity[0] = 0;
		item = null;
	}
	
	private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) { //& paddle.getX() < SIZE - paddle.getBoundsInLocal().getWidth()
            paddle.setX(paddle.getX() + paddleSpeed);
            paddle2.setX(paddle2.getX() + paddleSpeed);
        }
        else if (code == KeyCode.LEFT) { //& paddle.getX() > 0
            paddle.setX(paddle.getX() - paddleSpeed);
            paddle2.setX(paddle2.getX() - paddleSpeed);
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
        else if(code == KeyCode.SHIFT){
        	if(Ball.getY() == SIZE / 2 - Ball.getBoundsInLocal().getHeight() / 2){
        		Xvelocity[0] = random.nextInt(120) - 60;
        		Yvelocity[0] = 150;
        	}
        }
    }
	
	public static void main (String[] args) {
        launch(args);
    }

	
}
