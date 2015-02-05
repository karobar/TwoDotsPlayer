package twodotsplayer;

import java.util.ArrayList;

public class DotMap extends ArrayList<ArrayList<Dot>>{
    public Dot getDot(int x, int y) {
        return get(y).get(x);
    } 
    
    public String printHash() {
        return '@' + Integer.toHexString(hashCode());
    }
}
