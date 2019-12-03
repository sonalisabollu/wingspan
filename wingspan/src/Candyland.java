import java.util.Scanner;
/**
 * A very simple version of a board game where the players roll a pair of dice and move along a gameboard. The player
 * that reached the end of the board first wins. 
 * 
 */
public class Candyland
{
    // instance variables - replace the example below with your own
    Scanner input;
    private int numPlayers;
    private int boardLength = 20;
    private int [] playerPositions;
    private DieRoller dice = new DieRoller(2, 6); //standard dice(2 dice, 6 faces per die)
    private int [] currentRoll;
    private boolean gameRunning;
    /**
     * Constructor for objects of class Candyland
     */
    public Candyland()
    {
         this.input = new Scanner(System.in);
         System.out.println("Welcome to Candyland!");
         System.out.println("Please enter the number of players: ");
         System.out.println("\n\n");
         this.numPlayers = input.nextInt();
         runGame();
    }
    
    
    public void runGame(){
        this.gameRunning = true;
        this.playerPositions = new int[numPlayers];
        
        while(this.gameRunning){
            for(int i = 0; i<numPlayers; i++){
                if(this.gameRunning){
                    System.out.println("Player "+ (i+1) + "'s Turn\n Enter '1' to roll the dice");
                    this.input.nextInt();
                    currentRoll = this.dice.rollAll();
                    System.out.println("Your rolled: " + currentRoll[0] +", " + currentRoll[1]);
                    this.playerPositions[i] += currentRoll[0] + currentRoll[1];
                
                System.out.println("You are now at position: " + this.playerPositions[i]+ "\n");
                }
                
                if( this.playerPositions[i] >= this.boardLength){
                    System.out.println("You have reached the end of the board. You win!");
                    this.gameRunning = false;
                }
            }
        }
    }
}
