import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public final String BIRD_FILE = "birds.csv";

    public Game game;
    String ai;

    public Main() {

        Scanner input = new Scanner(System.in);
        System.out.println("Number of Players: ");
        int numberOfPlayers = input.nextInt();
        this.game = new Game(BIRD_FILE, numberOfPlayers);
        game.setup();
        input.nextLine();
        System.out.println("Do you want to include AI player(s)? (y/n) ");
        ai = input.nextLine();

        while(!game.isGameOver()) {
            game.passTurn();
            printOptions();
            int result = 0;
            int op;
            int birdid;
            do {
                System.out.println("Choose an option: ");
                if (game.getCurrentPlayerIndex() == 0 || ai.equals("n"))
                    op = input.nextInt();
                else
                    op = RandomAI.getRandomValue(2)+1;
                if (op == 1) {
                    Bird card;
                    if (game.getCurrentPlayerIndex() == 0 || ai.equals("n")) {
                        System.out.print("Input Card Id: ");
                        boolean cardEntered = false;
                        do{
                            if (cardEntered) {
                                System.out.println("Not valid card (either not enough food tokens or invalid card id)");
                                System.out.println("Kindly select valid card Id: ");
                            }
                            birdid = input.nextInt();
                            cardEntered = true;
                        } while ((card = inputCard(birdid)) == null);
                    } else {
                        birdid = RandomAI.getRandomBirdIndex(getAvailableBirdOptions(game.getPlayer())); // AI will act as a player
                        if (birdid == -1) {
                            result = game.selectOption(op, null, null);
                            break;
                        }
                    }

                    card = inputCard(birdid);
                    Game.Habitat habitat;
                    if (game.getCurrentPlayerIndex() == 0 || ai.equals("n"))
                        habitat = inputHabitat(card);
                    else {
                        habitat = RandomAI.getRandomHabitat(getAvailableHabitats(card));
                        if (habitat == null)
                            continue;
                    }
                    result = game.selectOption(op, card, habitat);
                } else if (op == 2) {
                    result = game.selectOption(op, null, null);
                }
            } while(op != 1 && op != 2);

            if(result == 1) {
                System.out.println("No action cubes left. Game over!");
                break;
            }
        }

        System.out.println("Winner is Player #"+game.getWinner());
    }

    public Bird inputCard(int op) {

        Bird card = null;
        if ((card = game.getPlayer().getBirdCard(op)) == null || !game.getPlayer().getMat().emptyCellExists(card) || !game.getPlayer().hasEnoughFoodTokens(op)) {

            System.out.println(op+" "+card);
            //System.out.println((card = game.getPlayer().getBirdCard(op)) +" "+ !game.getPlayer().getMat().emptyCellExists(card) +" "+ !game.getPlayer().hasEnoughFoodTokens(op));
            return null;
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
            else if(!game.getPlayer().getMat().emptyCellExists(habitat))
                System.out.println("Not space left in that habitat.");
        }
        while (!habitatExists || !card.getHabitat().contains(habitat));
        return habitat;
    }

    public void printOptions() {
        System.out.println("******************************************");
        System.out.println(game.getPlayer());
        System.out.println("1. Play a bird");
        System.out.println("2. Draw cards");
        if(game.getPlayer().getActionCubes() == 0)
            System.out.println("No action cubes left. Kindly restart the game.");
    }

    public ArrayList<Integer> getAvailableBirdOptions(Player player){
        ArrayList<Bird> bird = player.getBirdCards();
        ArrayList<Integer> options = new ArrayList<>();

        for(int i = 0; i < bird.size(); i++) {
            if (inputCard(bird.get(i).getId()) != null)
                options.add(bird.get(i).getId());
        }
        return options;
    }

    public ArrayList<Game.Habitat> getAvailableHabitats(Bird bird) {
        ArrayList<Game.Habitat> habitat = bird.getHabitat();
        System.out.println(bird.getHabitat().size());
        ArrayList<Game.Habitat> options = new ArrayList<>();

        for(int i = 0; i < habitat.size(); i++) {
            options.add(habitat.get(i));
        }
        return options;
    }


    public static void main(String[] args) {
        Main app = new Main();
    }
}