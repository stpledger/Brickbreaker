package game_stp20;

public class GameObject {
	private int score;
	private int lives;
	
	public void reset(){
		score = 0;
		lives = 3;
	}
	
	public void changeScore(int num){
		score += num;
	}
	
	public void changeLives(int num){
		lives += num;
	}
	
	public int getScore(){
		return score;
	}
	
	public int getLives(){
		return lives;
	}
}
