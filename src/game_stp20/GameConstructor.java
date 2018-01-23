package game_stp20;

public class GameConstructor {
	public int[][] construct(int level){
		int[][] layout = new int[10][10];
		switch (level){
			case 0: 
				for(int i = 0; i < 10; i++){
					for(int j = 0; j < 10; j++){
						if ((j == 3 | j == 7) & (i == 2 | i == 7)){
							layout[i][j] = 2;
						}
						else if((j == 4 | j == 6) & (i == 3 | i == 6)){
							layout[i][j] = 3;
						}
						else if((j * 10 + i) == 54 | (j * 10 + i) == 55){
							layout[i][j] = 4;
						}
						else if((j == 5 & (i == 2 | i == 7) )){
							layout[i][j] = 5;
						}
						else{
							layout[i][j] = 1;
						}
					}
				}
				break;
			case 1:
				for(int i = 0; i < 10; i++){
					for(int j = 0; j < 10; j++){
						if ((j == 8) & (i == 1 | i == 8)){
							layout[i][j] = 2;
						}
						else if((j == 1) & (i == 2 | i == 7)){
							layout[i][j] = 3;
						}
						else if(((j == 4) & (i == 3 | i == 6)) | ((j == 3) & (i == 4 | i == 5))){
							layout[i][j] = 4;
						}
						else if((j == 4 & (i == 4 | i == 5))){
							layout[i][j] = 5;
						}
						else if((j == 5 & (i == 4 | i == 5))){
							layout[i][j] = 0;
						}
						else{
							layout[i][j] = 1;
						}
					}
				}
				break;
			case 2:
				for(int i = 0; i < 10; i++){
					for(int j = 0; j < 10; j++){
						if ((j == 2 | j == 7) & (i == 5 | i == 4)){
							layout[i][j] = 2;
						}
						else if((j == 5) & (i == 2 | i == 7)){
							layout[i][j] = 3;
						}
						else if(j == 9 & (i == 4 | i == 5)){
							layout[i][j] = 4;
						}
						else if((j == 9 & (i == 1 | i == 8))){
							layout[i][j] = 5;
						}
						else if(i == j | i + j == 9){
							layout[i][j] = 0;
						}
						else{
							layout[i][j] = 1;
						}
					}
				}
				break;
			
		}
		return layout;
	}
}
