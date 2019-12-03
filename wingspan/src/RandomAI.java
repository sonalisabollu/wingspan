import java.util.ArrayList;
import java.util.Random;

public class RandomAI {
    public static int getRandomValue(int range) {
        Random random = new Random();

        return random.nextInt(range);
    }

    public static int getRandomBirdIndex(ArrayList<Integer> options) {
        Random random = new Random();
        if(options.size()== 0)
            return -1;
        return options.get(random.nextInt(options.size()));
    }

    public static Game.Habitat getRandomHabitat(ArrayList<Game.Habitat> options) {
        Random random = new Random();
        if(options.size()== 0)
            return null;
        return options.get(random.nextInt(options.size()));
    }
}