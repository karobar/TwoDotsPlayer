package twodotsplayer;

import java.util.ArrayList;
import java.util.HashSet;

public class DotMap extends ArrayList<ArrayList<Dot>>{
    HashSet<Ice> iceLevels;
    
    public DotMap() {
        this.iceLevels = new HashSet<>();
    }
    
    public Dot getDot(int x, int y) {
        return get(y).get(x);
    } 
    
    public String printHash() {
        return '@' + Integer.toHexString(hashCode());
    }
    
    public void addIce(Ice daIce) {
        this.iceLevels.add(daIce);
    }
    
    public boolean breakIce(int x, int y) {
        if(iceLevels == null) {
            return false;
        }
        //System.out.println("iceLevels.size(): " + iceLevels.size());
        for(Ice ice : this.iceLevels) {
            if(ice.x == x && ice.y == y) {
                //System.out.println("breaking ice at (" + x + "," + y + ")");
                ice.level--;
                if(ice.level <= 0) {
                    this.iceLevels.remove(ice);
                }   
                return true;  
            }        
        }
        return false;
    }
    
    public int getNumRows() {
        return size();
    }
        
    public int getNumCols() {
        return get(0).size();
    }
}
