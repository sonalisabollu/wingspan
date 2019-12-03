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
    private int[] columnsFilled;

    private int columns = 0;
    private ArrayList<Bird> birds;
    private ArrayList<Player> players;
    private Random rand;
    private int numPlayers;
    private int currentGameRound = 1; //Counter for current round in game
    private int currentPlayer = 0;

    final int BIRD_CARDS_PER_PLAYER = 3;
    final int FOOD_TOKENS_PER_PLAYER = 5;
    final int MAX_ROUND = 4;


    public Game(String birdsFile) {
    	birds = new ArrayList<>();
        players = new ArrayList<>();
        rand = new Random();

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

    /**
     * Prints options for specified player
     * @param player player selection
     */
    public void printOptions(Player player){
        System.out.println("******************************************");
        System.out.println(player);
        System.out.println("1. Play a bird");
        System.out.println("2. Draw cards");
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
    	  System.out.print("Input Card Id: ");
          Scanner input = new Scanner(System.in);
          int op = input.nextInt();
          while ((card = player.getBirdCard(op)) == null || !player.getMat().emptyCellExists(card) || !player.hasEnoughFoodTokens(op)) {
              if (card == null)
                  System.out.println("[ERROR] Invalid Id.");
              else if(!player.getMat().emptyCellExists(card))
                  System.out.println("[ERROR] Invalid habitat. No empty cells.");
              else
                  System.out.println("[ERROR] Not enough food tokens.");
              System.out.println("Kindly select valid card Id: ");
              if(input.hasNextInt()) {
            	  op = input.nextInt();  
              }
              else {
            	  System.out.println("Please enter a valid card Id: ");
              }
          }
          input.nextLine();
          boolean habitatExists = false;
          do {
              System.out.print("Input Habitat: ");
              String hbt = input.nextLine();
              try {
                  habitat = Habitat.valueOf(hbt);
                  habitatExists = true;
              }
              catch (Exception e) {
              }
              if(!habitatExists)
                  System.out.println("Habitat doesn't exist.");
              else if (!card.getHabitat().contains(habitat))
                  System.out.println("Bird doesn't live in that habitat.");
              else if(!player.getMat().emptyCellExists(habitat))
                  System.out.println("Not space left in that habitat.");
          }
          while (!habitatExists || !card.getHabitat().contains(habitat));
          int lec = player.getMat().getLeftMostEmptyCell(habitat)+1;
          int oldColumnsFilled = columns;
          columns = Math.max(lec, columns);
          player.setActionCubes(player.getActionCubes()-(columns-oldColumnsFilled));
          player.removeFoodTokens(op);
          player.getMat().addCard(card, habitat);
          player.removeBirdCard(op);
    }

    public void passTurn() {
        currentPlayer = ( currentPlayer + 1 ) % numPlayers;
        if (currentPlayer == 0)
            updateRound();
    }

    private void updateRound() {
        currentGameRound += 1;
        setup();
    }


    public Bird drawCard() {
        int index = rand.nextInt(birds.size());
        Bird card = birds.get(index);
        birds.remove(index);
        return card;
    }

    public Food getFoodToken() {
    	 ArrayList<Food> tokens = new ArrayList<>(Arrays.asList(Food.Fruit, Food.Fish, Food.Rodent, Food.Invertebrate, Food.Seed, Food.Wild));
         int index = rand.nextInt(tokens.size());
         return tokens.get(index);

    }

    /**
     * Card setup for all players in the game 
     * @param player selected player 
     */
    public void setup() {
    	Scanner input = new Scanner(System.in);
        for(Player player: this.players){//Loop through each player in game
       	 System.out.println("Enter your name: ");
       	 player.setName(input.next());
            for(int i = 0; i < BIRD_CARDS_PER_PLAYER; i++) {
                player.addBirdCard(drawCard());
            }
        }
    }

    public Bird inputCard() {
        System.out.print("Input Card Id: ");
        Scanner input = new Scanner(System.in);
        int op = input.nextInt();
        Bird card = null;
        while ((card = this.getPlayer().getBirdCard(op)) == null || !this.getPlayer().getMat().emptyCellExists(card) || !this.getPlayer().hasEnoughFoodTokens(op)) {
            if (card == null)
                System.out.println("[ERROR] Invalid Id.");
            else if(!this.getPlayer().getMat().emptyCellExists(card))
                System.out.println("[ERROR] Invalid habitat. No empty cells.");
            else
                System.out.println("[ERROR] Not enough food tokens.");
            System.out.println("Kindly select valid card Id: ");
            op = input.nextInt();
        }
        return card;
    }


    public Game.Habitat inputHabitat(Bird card) {
        Scanner input = new Scanner(System.in);
        Game.Habitat habitat = null;
        boolean habitatExists = false;
        do {
            System.out.print("Input Habitat: ");
            String hbt = input.nextLine();
            try {
                habitat = Game.Habitat.valueOf(hbt);
                habitatExists = true;
            }
            catch (Exception e) {
            }
            if(!habitatExists)
                System.out.println("Habitat doesn't exist.");
            else if (!card.getHabitat().contains(habitat))
                System.out.println("Bird doesn't live in that habitat.");
            else if(!this.getPlayer().getMat().emptyCellExists(habitat))
                System.out.println("Not space left in that habitat.");
        }
        while (!habitatExists || !card.getHabitat().contains(habitat));
        return habitat;
    }

    public void printOptions() {
        System.out.println("******************************************");
        System.out.println(this.getPlayer());
        System.out.println("1. Play a bird");
        System.out.println("2. Draw cards");
        if(this.getPlayer().getActionCubes() == 0)
            System.out.println("No action cubes left. Your turn is over!");
    }
    /**
     * All action required for a round in the game. 
     * @param player selected player 
     */
    public void roundAction(){
    	 Scanner input = new Scanner(System.in);
         while(this.currentGameRound < 5) {
        	System.out.println("*** ROUND "+ this.currentGameRound + " ***");//Print out current round
         	for(Player player: this.players){//Loop through each player in game
                 if(player.hasName()){//If player has a name
                	 
                     System.out.println(player.getName()+ "'s turn\n");
                     System.out.println(player.getName()+ "'s current score: " + player.calculateScore(player));
                 }
                 while(player.getActionCubes() != 0) {
                 	this.printOptions(player);
                 	System.out.println("Choose an option: ");
                 	this.selectOption(input.nextInt(), null, null);
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
    
   
    public Player getPlayer() {
        return this.players.get(currentPlayer);
    }

}
