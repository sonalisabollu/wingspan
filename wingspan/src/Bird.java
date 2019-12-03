import java.util.ArrayList;

public class Bird {
    private int id;
    private String name;
    private ArrayList<Game.Habitat> habitats;
    private ArrayList<Game.Food> foods;
    private int victoryPoint;
    public Bird(int id, String name, int victoryPoint) {
        foods = new ArrayList<>();
        habitats = new ArrayList<>();
        this.name = name;
        this.id = id;
        this.victoryPoint = victoryPoint;
    }
    
    public int getVictoryPoint() {
        return victoryPoint;
    }

    public void setVictoryPoint(int victoryPoint) {
        this.victoryPoint = victoryPoint;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void addHabitat(Game.Habitat habitat) {
        this.habitats.add(habitat);
    }

    public ArrayList<Game.Habitat> getHabitat() {
        return habitats;
    }

    public void addFoodRequirement(Game.Food food){
        this.foods.add(food);
    }

    public ArrayList<Game.Food> getFoodRequirements() {
        return foods;
    }

    public String toString() {
        String out = "Id: "+ id + ", Name: "+name+", Habitat: "+habitats+", Food: "+foods;
        return out;
    }
}
