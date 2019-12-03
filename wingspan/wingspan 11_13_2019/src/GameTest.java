import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

	//Tests playing a bird on the Mat
	@Test
	public void testPlayBird() throws IOException {
		Game wingspan = new Game("birds.csv", 1);
		wingspan.setup();
		Player player = wingspan.getPlayer();
		ArrayList<Bird> cards = player.getBirdCards();
		Game.Habitat habitat = cards.get(0).getHabitat().get(0);
		assertEquals(0, player.getMat().getLeftMostEmptyCell(habitat));
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 3);
		wingspan.playCardAction(cards.get(0), habitat);
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 2);
		assertEquals(1, player.getMat().getLeftMostEmptyCell(habitat));
	}

	//Tests drawing a card
	@Test
	public void testDrawCards() {
		Game wingspan = new Game("birds.csv", 1);
		wingspan.setup();
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 3);
		wingspan.selectOption(2, null, null);
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 4);
	}


	@Test
	public void testTwoCardsAreDrawnAtOnce() {
		Game wingspan = new Game("birds.csv", 1);
		wingspan.setup();
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 3);
		Bird bird1 = new Bird(174, "", 1);
		Bird bird2 = new Bird(175, "", 1);
		bird1.addHabitat(Game.Habitat.Wetlands);
		bird2.addHabitat(Game.Habitat.Wetlands);
		wingspan.getPlayer().addBirdCard(bird1);
		wingspan.getPlayer().addBirdCard(bird2);
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 5);
		wingspan.playCardAction(bird1, Game.Habitat.Wetlands);
		wingspan.playCardAction(bird2, Game.Habitat.Wetlands);
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 3);
		wingspan.selectOption(2, null, null);
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 5);
	}

	@Test
	public void testPlayerInitialActionCubes() {
		Game wingspan = new Game("birds.csv", 1);
		assertEquals(wingspan.getPlayer().getActionCubes(), 8);
	}
	
	@Test
	public void testSelectOption() {
		Game wingspan  = new Game("birds.csv", 1);
		wingspan.setup();
		ArrayList<Bird> cards = wingspan.getPlayer().getBirdCards();
		Bird card = cards.get(0);
		Game.Habitat habitat = card.getHabitat().get(0);
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 3);
		assertEquals(0, wingspan.getPlayer().getMat().getLeftMostEmptyCell(habitat));
		wingspan.selectOption(1, card, habitat);
		assertEquals(1, wingspan.getPlayer().getMat().getLeftMostEmptyCell(habitat));
		assertEquals(wingspan.getPlayer().getBirdCards().size(), 2);
	}

}