import static org.junit.Assert.*;


import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import org.junit.Test;

public class MatTest {
	//Tests initialization of Mat and that it prints correctly
	@Test
	public void testMat() throws IOException {
		Mat playerMat = new Mat();
		ArrayList<String> mat = new ArrayList<>();
		BufferedReader matReadeer = new BufferedReader(new FileReader("mat.txt"));
		String line;
		while ((line = matReadeer.readLine()) != null) {
			mat.add(line);
		}
		String matString = "";
		for(int i = 0; i < mat.size(); i++) {
			matString += mat.get(i) + "\n";
		}
		assertEquals(playerMat.toString(), matString);
	}

	//Tests adding a card to the board
	@Test
	public void testAddCard() {
		Game wingspan = new Game("birds.csv", 1);
		wingspan.setup();
		Player player = wingspan.getPlayer();
		Mat mat = player.getMat();
		ArrayList<Bird> cards = player.getBirdCards();
		Game.Habitat habitat = cards.get(0).getHabitat().get(0);
		assertEquals(0, mat.getLeftMostEmptyCell(habitat));
		wingspan.playCardAction(cards.get(0), habitat);
		assertEquals(1, mat.getLeftMostEmptyCell(habitat));
	}
	
	@Test
	public void testCardIsRejectedWhenHabitatFull() {
		Mat mat = new Mat();
		assertTrue(mat.addCard(new Bird(0, "", 1), Game.Habitat.Forest));
		assertTrue(mat.addCard(new Bird(0, "", 1), Game.Habitat.Forest));
		assertTrue(mat.addCard(new Bird(0, "", 1), Game.Habitat.Forest));
		assertTrue(mat.addCard(new Bird(0, "", 1), Game.Habitat.Forest));
		assertTrue(mat.addCard(new Bird(0, "", 1), Game.Habitat.Forest));
		assertFalse(mat.addCard(new Bird(0, "", 1), Game.Habitat.Forest));
	}


}