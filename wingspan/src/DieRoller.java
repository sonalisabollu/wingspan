import java.util.ArrayList;

public class DieRoller
{
    // instance variables - replace the example below with your own
    
    DieRollerModel model;
    
    /**
     * Constructor for objects of class BirdFeeder
     */
    public DieRoller(int dieCount, int facesPerDie)
    {
        this.model = new DieRollerModel(dieCount, facesPerDie);
    }
    
    /**
     * Roll each die
     */
    public int [] rollAll(){
        return model.rollAll();
    }
    
    /**
     * Return the number of faces per die
     * @return Int numbe rof faces per die
    */
    public int getNumFacesPerDie(){
        return model.getNumFacesPerDie();
    }
    
    
    /**
     * Return the curren value for each die
     * @return Array of values for each die
     */
    public int [] getValues(){
        return model.getValues();
    }
    
    /**
     * Remove a single die from play
     * @param index index of die to be removed
     */
     public void removeDie(int index){
         model.removeDie(index);
    }
    
    /**
     * Get number of dice on play
     */
    public int getDieCount(){
        return model.getDieCount();
    }
    
    /**
     * Restore all dice and randomize them
     */
    public void reset(){
        model.reset();
    }
}
