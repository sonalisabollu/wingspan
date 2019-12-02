import java.util.ArrayList;
import java.util.Random; 

public class Die
{

    private int faces;
    private int currentFace = 0;
    /**
     * Constructor for objects of class Die
     */
    public Die(int faces)
    {
        this.faces = faces;
    }

    /**
     * Simmulates the rolling of this die
     */
    public void roll() 
    { 
       Random rand = new Random(); 
       this.currentFace = rand.nextInt(this.faces);
    }
    
     /**
     * Returns current face on die
     * @return current face
     */
    public int getCurrentFace(){
        return this.currentFace;
    }
}
