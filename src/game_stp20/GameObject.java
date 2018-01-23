package game_stp20;
/* 
 * This class is used to manage the lives, score, and number of blocks left in a level
 */
public class GameObject {
	private int score;
	private int lives;
	private int mult;
	private int blocks;
	
	public void reset(){
		score = 0;
		lives = 3;
		mult = 1;
		blocks = 0;
	}
	
	public void changeScore(int num){
		score += num * mult;
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
	
	public void changeMult(int num){
		mult = num;
	}
	
	public int getMult(){
		return mult;
	}
	
	public void changeBlock(int i){
		blocks += i;
	}
	
	public int getBlocks(){
		return blocks;
	}
}
