package twodotsplayer;

import java.util.HashSet;

public class Move extends HashSet<Dot> {
    static final Dot cycleDot = new Dot(DotFlavor.CYCLE,0,0); 
    DotFlavor color;
    
    public Move(DotFlavor color) {
        this.color = color;
    }
    
    public void makeCycle() {
        add(cycleDot);
    }
    
    public boolean isCycle() {
        if(contains(cycleDot)) {
            return true;
        }
        return false;
    }
    
    public boolean allWhiteCycle() {
        for (Dot dot : this) {
            if(dot.dotFlavor != DotFlavor.WHITE) {
                return false;
            }
        }
        return true;
    }
    
//    @Override
//    public String toString() {
//        String daString = "[";
//        for(Dot dot: this) {
//            daString = daString + dot.myToString() + ", ";
//        }
//        daString = daString + "]";
//        return daString;
//    }
}
