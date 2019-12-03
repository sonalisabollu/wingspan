import java.util.ArrayList;

public class DieRoller
{
    // instance variables - replace the example below with your own
    ArrayList<Die> diceIn;
    ArrayList<Die> diceOut;
    private int facesPerDie;
    private int dieCount;
    
    
    /**
     * Constructor for objects of class BirdFeeder
     */
    public DieRoller(int dieCount, int facesPerDie)
    {
        diceIn = new ArrayList<>();
        diceOut = new ArrayList<>();
        this.facesPerDie = facesPerDie;
         for(int i = 0; i<dieCount; i++){
             Die die = new Die(facesPerDie);
             this.diceIn.add(die);
        }
    }
    
    /**
     * Roll each die
     */
    public void rollAll(){
        this.diceIn.forEach((die) -> die.roll()); 
    }
    
    
    /**
     * Return the curren value for each die
     * @return Array of values for each die
     */
    public int [] getValues(){
        int[] values = new int[facesPerDie];
        for(int i = 0; i< diceIn.size(); i++){
            values[i] = diceIn.get(i).getCurrentFace();
        }
        return values;
    }
    
    /**
     * Remove a single die from play
     * @param index index of die to be removed
     */
     public void removeDie(int index){
         diceOut.add(diceIn.get(index));
         diceIn.remove(index);
    }
    
    /**
     * Get number of dice on play
     */
    public int getDieCount(){
        return diceIn.size();
    }
    
    /**
     * Restore all dice and randomize them
     */
    public void reset(){
        if(this.diceOut.size() <1){
            return;
        }
        for(int i = 0; i<= diceOut.size(); i++){
            this.diceIn.add(diceOut.get(i));
        }
        
        this.diceIn.forEach((die) -> die.roll()); 
    }
}
