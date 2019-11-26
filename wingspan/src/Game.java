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

    private ArrayList<Bird> birds;
    private ArrayList<Player> players;
    private Random rand;
    private int numPlayers;
    private int currentGameRound = 1; //Counter for current round in game
    private int currentPlayer = 0;

    final int BIRD_CARDS_PER_PLAYER = 3;
    final int FOOD_TOKENS_PER_PLAYER = 3;
    final int MAX_ROUND = 4;


    public Game(String birdsFile) {
    	birds = new ArrayList<>();
        player = new Player();
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
        for(Player player: this.players){//Loop through each player in game
            for(int i = 0; i < BIRD_CARDS_PER_PLAYER; i++) {
                player.addBirdCard(drawCard());
            }
            for(int i = 0; i < FOOD_TOKENS_PER_PLAYER; i++) {
                player.addFoodToken(getFoodToken());
            }
        }
    }


    /**
     * All action required for a round in the game. 
     * @param player selected player 
     */
    public void roundAction(){
        Scanner input = new Scanner(System.in);
        System.out.println("*** ROUND "+ this.currentGameRound + " ***");//Print out current round
        for(Player player: this.players){//Loop through each player in game
            if(player.hasName()){//If player has a name
                System.out.println(player.getName()+ "'s turn\n");
            }
            this.printOptions(player);
            System.out.println("Choose an option: ");
            this.selectOption(player, input.nextInt());
        }
        currentGameRound++;//Increase the round counter
    }
    
   
    public Player getPlayer() {
        return this.players.get(currentPlayer);
    }

}