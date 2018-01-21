package game_stp20;

public class GameConstructor {
	public int[][] construct(int level){
		int[][] layout = new int[10][10];
		switch (level){
			case 0: 
				for(int i = 0; i < 10; i++){
					for(int j = 0; j < 10; j++){
						if ((i * 10 + j) % 25 == 0){
							layout[i][j] = 3;
						}
					}
				}
			
		}
		return null;
	}
}
