package twodotsplayer;

public class Objective {
    int removedDots;
    int brokenIce;
    int droppedAnchors;
    int squares;
    int brokenBoxes;
    
    public Objective() {
        this.removedDots = 0;
        this.brokenIce = 0;
        this.droppedAnchors = 0;
        this.squares = 0;
    }
    
    public int getScore() {
        return 100*brokenBoxes + 30*brokenIce + 10*squares;
    }
}
