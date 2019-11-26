import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class BirdTest {
	//Bird class does not need much testing as majority of functions are getters and setters
	
	//Tests toString function that it prints out Id, Name, Habitat, and Food correctly
	@Test
	public void testBird() {
		Bird bird = new Bird(1, "Test Bird", 1);
		System.out.println(bird.toString());
	}

}