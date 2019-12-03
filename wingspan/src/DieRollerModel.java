import java.util.ArrayList;

/**
 * Write a description of class DieRollerModel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class DieRollerModel
{
    ArrayList<Die> diceIn;
    ArrayList<Die> diceOut;
    private int facesPerDie;
    private int dieCount;
    
    /**
     * Constructor for objects of class DieRollerModel
     */
    public DieRollerModel(int dieCount, int facesPerDie)
    {
        diceIn = new ArrayList<>();
        diceOut = new ArrayList<>();
        this.facesPerDie = facesPerDie;
        this.dieCount = dieCount;
         for(int i = 0; i<dieCount; i++){
             Die die = new Die(facesPerDie);
             this.diceIn.add(die);
        }
    }

    /**
     * Roll each die
     */
    public int [] rollAll(){
        this.diceIn.forEach((die) -> die.roll());
        return getValues();
    }
    
    public int getNumFacesPerDie(){
        return this.facesPerDie;
    }
    
    /**
     * Return the curren value for each die
     * @return Array of values for each die
     */
    public int [] getValues(){
        int[] values = new int[dieCount];
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
