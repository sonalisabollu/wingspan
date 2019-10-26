import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

	//Tests playing a bird on the Mat
	@Test
	public void testPlayBird() {
		Game wingspan = new Game();
		Player player = new Player();
		wingspan.setup();
		wingspan.printOptions();
		System.out.println("Choose an option: ");
		wingspan.selectOption(1);
		wingspan.printOptions();
	}
	
	//Tests drawing a card
	@Test
	public void testDrawCards() {
		Game wingspan = new Game();
		wingspan.setup();
		wingspan.printOptions();
		System.out.println("Choose an option: ");
		wingspan.selectOption(2);
	}
	
}
