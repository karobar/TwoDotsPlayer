package twodotsplayer;

import java.util.ArrayList;

public class ScorePackage {
    ArrayList<Move> path;
    int score;
    
    ScorePackage() {
        this.path = new ArrayList<>();
        this.score = 0;
    }
}
