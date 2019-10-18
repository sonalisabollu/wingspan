import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {

    public enum Habitat {Forest, Grasslands, Wetlands}
    public enum Food {Wild, Invertebrate, Seed, Fruit, Fish, Rodent}
    public final int[] WETLAND_DRAW_CARD = {1, 1, 2, 2, 2};
    private int columnsFilled = 0;

    private ArrayList<Bird> birds;
    private Player player;
    private Random rand;

    final int BIRD_CARDS_PER_PLAYER = 3;
    final int FOOD_TOKENS_PER_PLAYER = 5-BIRD_CARDS_PER_PLAYER;


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

    public void printOptions() {
        System.out.println("******************************************");
        System.out.println(player);
        System.out.println("1. Play a bird");
        System.out.println("2. Draw cards");
        if(player.getActionCubes() == 0)
            System.out.println("No action cubes left. Kindly restart the game.");
    }

    public void selectOption(int option) {
        switch (option) {
            case 1:
                if(player.getActionCubes() == 0) {
                    System.out.println("No action cubes left. Kindly restart the game.");
                    return;
                }
                System.out.print("Input Card Id: ");
                Scanner input = new Scanner(System.in);
                int op = input.nextInt();
                Bird card;
                while ((card = player.getBirdCard(op)) == null || !player.getMat().emptyCellExists(card) || !player.hasEnoughFoodTokens(op)) {
                    if (card == null)
                        System.out.println("[ERROR] Invalid Id.");
                    else if(!player.getMat().emptyCellExists(card))
                        System.out.println("[ERROR] Invalid habitat. No empty cells.");
                    else
                        System.out.println("[ERROR] Not enough food tokens.");
                    System.out.println("Kindly select valid card Id: ");
                    op = input.nextInt();
                }
                input.nextLine();
                Habitat habitat = null;
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
                int oldColumnsFilled = columnsFilled;
                columnsFilled = Math.max(lec, columnsFilled);
                player.setActionCubes(player.getActionCubes()-(columnsFilled-oldColumnsFilled));
                player.removeFoodTokens(op);
                player.getMat().addCard(card, habitat);
                player.removeBirdCard(op);
                break;
            case 2:
                if(player.getActionCubes() == 0) {
                    System.out.println("No action cubes left. Kindly restart the game.");
                    return;
                }
                int col = player.getMat().getLeftMostEmptyCell(Habitat.Wetlands);
                for(int i = 0; i < WETLAND_DRAW_CARD[col]; i++) {
                    player.addBirdCard(drawCard());
                }
                System.out.println("You drew "+WETLAND_DRAW_CARD[col]+" new bird card(s)!");
                player.setActionCubes(player.getActionCubes()-1);
                break;
        }
    }

    public Bird drawCard() {
        int index = rand.nextInt(birds.size());
        Bird card = birds.get(index);
        birds.remove(index);
        return card;
    }

    public void setup() {
        for(int i = 0; i < BIRD_CARDS_PER_PLAYER; i++) {
            player.addBirdCard(drawCard());
        }
    }

    public static void main(String[] args) throws IOException {
        Game wingspan = new Game();
        wingspan.setup();

        Scanner input = new Scanner(System.in);

        while(true) {
            wingspan.printOptions();
            System.out.println("Choose an option: ");
            wingspan.selectOption(input.nextInt());
        }
    }
}
