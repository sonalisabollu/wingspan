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
	
	@Test
	public void testTwoCardsAreDrawnAtOnce() {
		Game wingspan = new Game();
		wingspan.setup();
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 3);
		Bird bird1 = new Bird(174, "");
		Bird bird2 = new Bird(175, "");
		bird1.addHabitat(Game.Habitat.Wetlands);
		bird2.addHabitat(Game.Habitat.Wetlands);
		wingspan.getPlayer().addBirdCard(bird1);
		wingspan.getPlayer().addBirdCard(bird2);
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 5);
		wingspan.playCardAction(bird1, Game.Habitat.Wetlands);
		wingspan.playCardAction(bird2, Game.Habitat.Wetlands);
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 3);
		wingspan.selectOption(2);
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 5);
	}

	@Test
	public void testPlayerInitialActionCubes() {
		Game wingspan = new Game();
		assertEquals(wingspan.getPlayer().getActionCubes(), 8);
	}

	
	
	
}
