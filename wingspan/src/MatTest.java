import static org.junit.Assert.*;

import org.junit.Test;

public class MatTest {

	//Tests initialization of Mat and that it prints correctly
	@Test
	public void testMat() {
		Game test = new Game();
		test.setup();
		test.printOptions();
	}

	//Tests adding a card to the board
	@Test
	public void testAddCard() {
		Game wingspan = new Game();
		wingspan.setup();
		wingspan.printOptions();
		System.out.println("Choose an option: ");
		wingspan.selectOption(1);
		wingspan.printOptions();
	}

}
