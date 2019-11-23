import java.util.Scanner;

public class Main {
    public final String BIRD_FILE = "birds.csv";

    public Game game;

    public Main() {
        this.game = new Game(BIRD_FILE);
        game.setup();

        Scanner input = new Scanner(System.in);

        while(true) {
            printOptions();
            System.out.println("Choose an option: ");
            int op = input.nextInt();
            int result = 0;
            if(op == 1) {
                Bird card = inputCard();
                Game.Habitat habitat = inputHabitat(card);
                result = game.selectOption(op, card, habitat);
            }
            else if(op == 2) {
                result = game.selectOption(op, null, null);
            }
            if(result == 1) {
                System.out.println("No action cubes left. Game over!");
                break;
            }
        }
    }

    public Bird inputCard() {
        System.out.print("Input Card Id: ");
        Scanner input = new Scanner(System.in);
        int op = input.nextInt();
        Bird card;
        while ((card = game.getPlayer().getBirdCard(op)) == null || !game.getPlayer().getMat().emptyCellExists(card) || !game.getPlayer().hasEnoughFoodTokens(op)) {
            if (card == null)
                System.out.println("[ERROR] Invalid Id.");
            else if(!game.getPlayer().getMat().emptyCellExists(card))
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

    public static void main(String[] args) {
        Main app = new Main();
    }
}