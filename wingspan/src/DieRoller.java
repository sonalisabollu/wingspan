import java.util.ArrayList;

public class DieRoller
{
    // instance variables - replace the example below with your own
    ArrayList<Die> dice;
    private int facesPerDie = 6;
    
    /**
     * Constructor for objects of class BirdFeeder
     */
    public DieRoller(int die_count, int facesPerDie)
    {
        dice = new ArrayList<>();
        
         for(int i = 0; i<=die_count; i++){
             Die die = new Die(facesPerDie);
             this.dice.add(die);
        }
    }
    
    public void rollAll(){
        this.dice.forEach((die) -> die.roll()); 
    }
    
    public void removeDie(int value){
        //remove single die from Die Roller
    }
}
