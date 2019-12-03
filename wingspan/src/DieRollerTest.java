import static org.junit.Assert.*;
import org.junit.Test;

public class DieRollerTest // will be used as birdfeeder
{
    
    @Test
    public void testDieRollerContructor() {
        DieRoller birdFeeder = new DieRoller(5, 6);
        assertEquals(5, birdFeeder.getDieCount());
        assertEquals(6, birdFeeder.getNumFacesPerDie());
        
    }
    
    @Test
    public void testDieRollerRemove() {
        DieRoller birdFeeder = new DieRoller(5, 6);
        assertEquals(5, birdFeeder.getDieCount());
        birdFeeder.removeDie(1);
        assertEquals(4, birdFeeder.getDieCount());
        birdFeeder.removeDie(1);
        assertEquals(3, birdFeeder.getDieCount());
        birdFeeder.removeDie(1);
        assertEquals(2, birdFeeder.getDieCount());
        birdFeeder.removeDie(1);
        assertEquals(1, birdFeeder.getDieCount());
    }
    
    @Test
    public void testDieRollerReset() {
        DieRoller birdFeeder = new DieRoller(5, 6);
        assertEquals(5, birdFeeder.getDieCount());
        birdFeeder.removeDie(1);
        assertEquals(4, birdFeeder.getDieCount());
        birdFeeder.removeDie(1);
        assertEquals(3, birdFeeder.getDieCount());
        birdFeeder.removeDie(1);
        assertEquals(2, birdFeeder.getDieCount());
        birdFeeder.removeDie(1);
        assertEquals(1, birdFeeder.getDieCount());
    }
    
    @Test
    public void testGetValues() {
        DieRoller birdFeeder = new DieRoller(5, 6);
        assertEquals(5, birdFeeder.getValues().length);
        
        DieRoller birdFeederTwo = new DieRoller(9, 6);
        assertEquals(9, birdFeederTwo.getValues().length);
    
    }
}
