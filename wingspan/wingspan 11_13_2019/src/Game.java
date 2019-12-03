import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game {

    public enum Habitat {Forest, Grasslands, Wetlands}
    public enum Food {Wild, Invertebrate, Seed, Fruit, Fish, Rodent}
    public final int[] WETLAND_DRAW_CARD = {1, 1, 2, 2, 2};
    private int columnsFilled = 0;

    private ArrayList<Bird> birds;
    private ArrayList<Player> players;
    private Random rand;
    private int numPlayers;
    private int currentGameRound = 0; //Counter for current round in game
    private int currentPlayer = -1;
    private boolean noActionCubesLeft = false;

    final int BIRD_CARDS_PER_PLAYER = 3;
    final int FOOD_TOKENS_PER_PLAYER = 3;
    final int MAX_ROUND = 4;

    public Game() {
        birds = new ArrayList<>();
        player = new Player();
        rand = new Random();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("birds.csv"));

            String[] header = csvReader.readLine().split(",");
            String[] row;
            String line;

            while ((line = csvReader.readLine()) != null) {
                row = line.split(",");

                Bird bird = new Bird( Integer.parseInt(row[0]), row[2] );

                for (int i = 0; i < header.length; i++) {
                    if(header[i].toLowerCase().contains("food") && !header[i].equals("FoodNone") && !row[i].equals("null")) {
                        /*
                         * String contains FoodNone,FoodWild,FoodInvertebrate,FoodSeed,FoodFruit,FoodFish or FoodRodent
                         * Taking "Food" out of above header column and adding corresponding values to bird.
                         */
                        bird.addFoodRequirement(Food.valueOf(header[i].substring(4))); // substring(4) return sub-string from index 4 onwards
                    }
                    if(header[i].toLowerCase().contains("habitat") && !row[i].equals("n")) {
                        /*
                         * Column headers contain HabitatForest,HabitatGrasslands or HabitatWetlands
                         * removing "Habitat" from the column header using substring
                         */
                        bird.addHabitat(Habitat.valueOf(header[i].substring(7)));
                    }
                }

                birds.add(bird);
            }
        } catch (IOException e) {

        }
    }



    /**
     * Mulitplayer constructor for Game
     * @param players number of players 
     */
    public Game(String birdsFile, int numPlayers){
        this.numPlayers = numPlayers;
        this.columnsFilled = new int[numPlayers];

        birds = new ArrayList<>();
        players = new ArrayList<>();
        rand = new Random();
        noActionCubesLeft = false;

        for(int index = 0; index<numPlayers; index++){
            Player newPlayer = new Player("Player"+(index));
            this.players.add(newPlayer);
            columnsFilled[index] = 0;
        }

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(birdsFile));

            String[] header = csvReader.readLine().split(",");
            String[] row;
            String line;

            while ((line = csvReader.readLine()) != null) {
                row = line.split(",");
                if (row[7].equals("null")) row[7] = "0";
                Bird bird = new Bird( Integer.parseInt(row[0]), row[2], Integer.parseInt(row[7]) );

                for (int i = 0; i < header.length; i++) {
                    if(header[i].toLowerCase().contains("food") && !header[i].equals("FoodNone") && !row[i].equals("null")) {
                        /*
                         * String contains FoodNone,FoodWild,FoodInvertebrate,FoodSeed,FoodFruit,FoodFish or FoodRodent
                         * Taking "Food" out of above header column and adding corresponding values to bird.
                         */
                        bird.addFoodRequirement(Food.valueOf(header[i].substring(4))); // substring(4) return sub-string from index 4 onwards
                    }
                    if(header[i].toLowerCase().contains("habitat") && !row[i].equals("n")) {
                        /*
                         * Column headers contain HabitatForest,HabitatGrasslands or HabitatWetlands
                         * removing "Habitat" from the column header using substring
                         */
                        bird.addHabitat(Habitat.valueOf(header[i].substring(7)));
                    }
                }

                birds.add(bird);
            }
        } catch (IOException e) {

        }
    }


    public void printOptions() {
        Player player = players.get(currentPlayer);
        System.out.println("******************************************");
        System.out.println(player);
        System.out.println("1. Play a bird");
        System.out.println("2. Draw cards");
        if(player.getActionCubes() == 0)
            System.out.println("No action cubes left. Kindly restart the game.");
    }


    /**
     * Prints options for specified player
     * @param player player selection
     */
    public void printOptions(Player player){
        System.out.println("******************************************");
        System.out.println(player);
        System.out.println("1. Play a bird");
        System.out.println("2. Gain food");
        System.out.println("3. Lay eggs");
        System.out.println("4. Draw cards");
        if(player.getActionCubes() == 0)
            System.out.println("No action cubes left. Kindly restart the game.");
    }

    public int selectOption(int option, Bird card, Habitat habitat) {
        Player player = players.get(currentPlayer);
        switch (option) {
            case 1:
                if(player.getActionCubes() == 0) {
                    return 1;
                }
                playCardAction(card, habitat);
                break;
            case 2:
                if(player.getActionCubes() == 0) {
                    return 1;
                }
                int col = player.getMat().getLeftMostEmptyCell(Habitat.Wetlands);
                for(int i = 0; i < WETLAND_DRAW_CARD[col]; i++) {
                    player.addBirdCard(drawCard());
                }
                System.out.println("You drew "+WETLAND_DRAW_CARD[col]+" new bird card(s)!");
                players.get(currentPlayer).setActionCubes(player.getActionCubes()-1);
                break;
        }
        return 0;
    }
    

    public void playCardAction(Bird card, Habitat habitat) {
        Player player = players.get(currentPlayer);
        int op = card.getId();
        int lec = player.getMat().getLeftMostEmptyCell(habitat)+1;
        int oldColumnsFilled = columnsFilled[currentPlayer];
        columnsFilled[currentPlayer] = Math.max(lec, columnsFilled[currentPlayer]);
        player.setActionCubes(player.getActionCubes()-(columnsFilled[currentPlayer]-oldColumnsFilled));
        player.removeFoodTokens(op);
        player.getMat().addCard(card, habitat);
        player.removeBirdCard(op);
    }
    
    public void passTurn() {
        currentPlayer = ( currentPlayer + 1 ) % numPlayers;
        if (currentPlayer == 0)
            updateRound();
        int nextPlayer = (currentPlayer) % numPlayers;
        while(players.get(nextPlayer).getActionCubes() == 0) {
            nextPlayer = (nextPlayer+1)%numPlayers;
            if (nextPlayer == currentPlayer) {
                noActionCubesLeft = true;
                break;
            }
        }
    }
    
    private void updateRound() {
        currentGameRound += 1;
        giveFoodTokens();
    }


   
    public Bird drawCard() {
        int index = rand.nextInt(birds.size());
        Bird card = birds.get(index);
        birds.remove(index);
        return card;
    }
    
    public boolean isGameOver() {
        return currentGameRound > MAX_ROUND && noActionCubesLeft;
    }

    public Food getFoodToken() {
        ArrayList<Food> tokens = new ArrayList<>(Arrays.asList(Food.Fruit, Food.Fish, Food.Rodent, Food.Invertebrate, Food.Seed, Food.Wild));
        int index = rand.nextInt(tokens.size());
        return tokens.get(index);
    }

    //need to implement random rolling dice. Have set dice for testing purposes
    public void birdFeeder() {
    	
    }
    
    //need to implement random choice of 4 goals,1 per round. Have set goals for testing purposes
    public void chooseGoals() {
    //Total number of birds played
    	
    //Total number of birds in specific row
    	
    //Total number of birds with specific nest type that have at least 1 egg
    	
    //Total eggs in specific row
    	
    //Total sets of eggs (set is equal to 1 egg in each row)
    	
    }
    
    /**
     * Card setup for all players in the game 
     * @param player selected player 
     */
       public void setupAll() {
    	 Scanner input = new Scanner(System.in);
         for(Player player: this.players){//Loop through each player in game
        	 System.out.println("Enter your name: ");
        	 player.setName(input.next());
             for(int i = 0; i < BIRD_CARDS_PER_PLAYER; i++) {
                 player.addBirdCard(drawCard());
             }
         }
     }
       public void setup() {
           for(Player player: this.players){//Loop through each player in game
               for(int i = 0; i < BIRD_CARDS_PER_PLAYER; i++) {
                   player.addBirdCard(drawCard());
               }
               for(int i = 0; i < FOOD_TOKENS_PER_PLAYER; i++) {
                   player.addFoodToken(getFoodToken());
               }
           }
       }

       
       public int calculateScore(Player player) {
       	int totalScore = 0;
       	//1 point for each face up bird on Mat
        for(int i = 0; i < player.mat.MAT_ROWS; i++) {
            for (int j = 0; j < player.mat.MAT_COLUMNS; j++) {
            	if(player.mat.cellFilled[i][j]) {
            		totalScore += 1;
            	}
            }
        }
       
       	//Points for each bonus card, dependent on card
       	
       	//Points for end of round goals
       	
       	//1 point for each egg
       	totalScore += player.getEggs();
       	//1 point for each food token cached on a bird card
       	
       	//1 point for each card tucked under a bird card
       	
       	player.setScore(totalScore);
       	return player.getScore();
       }

    /**
     * All action required for a round in the game. 
     * @param player selected player 
     */
    public void roundAction(){
        Scanner input = new Scanner(System.in);
        System.out.println("*** ROUND "+ this.currentGameRound + " ***");//Print out current round
        //makes sure game only runs for 4 rounds
        while(this.currentGameRound < 5) {
        	for(Player player: this.players){//Loop through each player in game
                if(player.hasName()){//If player has a name
                    System.out.println(player.getName()+ "'s turn\n");
                    System.out.println(player.getName()+ "'s current score: " + calculateScore(player));
                }
                while(player.getActionCubes() != 0) {
                	this.printOptions(player);
                	System.out.println("Choose an option: ");
                	this.selectOption(player, input.nextInt());
                }
                System.out.println("All out of action cubes, your round is over!");
                player.setActionCubes(8 - currentGameRound);
            }
            currentGameRound++;//Increase the round counter
        }
        System.out.println("GAME OVER! Here are the final scores: ");
    	for(Player player: this.players) {
    		System.out.println(player.getName()+ "'s final score: " + player.getScore());
    	}
    }
    

    public void giveFoodTokens() {
        for(Player player: this.players){//Loop through each player in game

            for(int i = 0; i < FOOD_TOKENS_PER_PLAYER; i++) {
                player.addFoodToken(getFoodToken());
            }
        }
    }
      
   public int getWinner() {
          int winner_index =0;
          int max_score = 0;
          for(int i = 0; i < players.size(); i++) {
              int pscore = 0;
              Mat playerMat = players.get(i).getMat();
              for(int j = 0; j < playerMat.getBirds().size(); j++) {
                  pscore += playerMat.getBirds().get(j).getVictoryPoint();
              }

              if(pscore > max_score) {
                  max_score = pscore;
                  winner_index = i;
              }
          }
          return winner_index;
   }

   public int getCurrentPlayerIndex() {
          return currentPlayer;
   }
   

    public Player getPlayer() {
        return this.player;
    }
    
    
    public static void main(String[] args) throws IOException {
        int numPlayers = 0;
        System.out.println("Enter the number of players (1-5): ");
        Scanner input = new Scanner(System.in);
        numPlayers = input.nextInt();
    	Game wingspan = new Game(numPlayers);
        wingspan.setupAll();
        wingspan.roundAction();
    }
}
