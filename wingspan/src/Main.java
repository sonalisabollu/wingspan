import java.util.Scanner;

public class Main {
    public final String BIRD_FILE = "birds.csv";

    public Game game;

    public Main() {
    	 Scanner input = new Scanner(System.in);
    	 System.out.println("Welcome to Wingspan!");
         System.out.println("Please enter the number of players: ");
         int numberOfPlayers = input.nextInt();
         this.game = new Game(BIRD_FILE, numberOfPlayers);
         game.setup();
         game.roundAction();
    }

    public static void main(String[] args) {
        Main app = new Main();
    }
}
