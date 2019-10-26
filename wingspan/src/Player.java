import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    ArrayList<Bird> birds;
    ArrayList<Game.Food> foodTokens;
    private int actionCubes = 8;
    private String name;
    Mat mat;

    public Player() {
        birds = new ArrayList<>();
        foodTokens = new ArrayList<>();
        foodTokens.addAll(Arrays.asList(Game.Food.Fruit, Game.Food.Fish, Game.Food.Rodent, Game.Food.Invertebrate, Game.Food.Seed, Game.Food.Wild));
        mat = new Mat();
    }
    
    /**
     * Player constructor with name
     * @param name player's name 
    */
    public Player(String name){
        birds = new ArrayList<>();
        foodTokens = new ArrayList<>();
        foodTokens.addAll(Arrays.asList(Game.Food.Fruit, Game.Food.Fish, Game.Food.Rodent, Game.Food.Invertebrate, Game.Food.Seed, Game.Food.Wild));
        mat = new Mat();
        this.name = name;
    }

    public int getActionCubes() {
        return actionCubes;
    }

    public void setActionCubes(int x) {
        actionCubes = x;
    }

    public String toString() {
        String out = "";
        out += "Action Cubes: "+actionCubes+"\n\n";
        out += "My Bird Cards: \n";
        for(int i = 0; i < birds.size(); i++) {
            out += birds.get(i)+"\n";
        }
        out += "\n";
        out += "My Food Tokens: ";
        for(int i = 0; i < foodTokens.size(); i++) {
            out += foodTokens.get(i);
            if (i < foodTokens.size()-1)
                out += ", ";
        }
        out += "\n\n";
        if(this.name != null){
            out += this.name +"'s Mat: \n";
        }else{
            out += "Player Mat: \n";
        }
        
        out += mat;
        return out;
    }

    public boolean hasFoodToken(ArrayList<Game.Food> foods) {
        for(int i = 0; i < foods.size(); i++) {
            if (!foodTokens.contains(foods.get(i)))
                return false;
        }
        return true;
    }

    public boolean hasEnoughFoodTokens(int cardId) {
        Bird card = getBirdCard(cardId);
        ArrayList<Game.Food> foodRequired = card.getFoodRequirements();
        for(int i = 0; i < foodRequired.size(); i++) {
            if(!foodTokens.contains(foodRequired.get(i)))
                return false;
        }
        return true;
    }

    public void removeFoodTokens(int cardId) {
        Bird card = getBirdCard(cardId);
        ArrayList<Game.Food> foodRequired = card.getFoodRequirements();
        for(int i = 0; i < foodTokens.size(); i++) {
            if(foodRequired.contains(foodTokens.get(i)))
                foodTokens.remove(i);
        }
    }

    public Bird getBirdCard(int id) {
        for(int i = 0; i < birds.size(); i++)
            if(birds.get(i).getId() == id)
                return birds.get(i);
        return null;
    }

    public Mat getMat() {
        return mat;
    }

    public ArrayList<Bird> getBirdCards() {
        return birds;
    }

    public void addBirdCard(Bird bird) {
        birds.add(bird);
    }

    public boolean removeBirdCard(int id) {
        for(int i = 0; i < birds.size(); i++) {
            if(birds.get(i).getId() == id) {
                birds.remove(i);
                return true;
            }
        }
        return false;
    }

}
