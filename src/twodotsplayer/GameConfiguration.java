package twodotsplayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameConfiguration {
    public List<Transporter> transporters;
    public String dots;
    public DotMap dotMap;
    HashSet<Move> allMoves;
     
    public List<Transporter> getTransporters() {
        return transporters;
    }
    
    public void setTransporters(List<Transporter> transporters) {
        this.transporters = transporters;
    }
    
    public String getDots() {
        return dots;
    }
    
    public void setDots(String dots) {
        this.dots = dots;
    }
    
    public void parse() {
       dotMap = new DotMap();
       ArrayList<Dot> currRow = new ArrayList<>();
       int x = 0;
       int y = 0;       
       for(int i = 0; i < dots.length(); i++) {
            if(dots.charAt(i)=='\n') {
                dotMap.add(currRow);
                currRow = new ArrayList<>();
                y++;
                x = 0;
            }
            else {
                currRow.add(new Dot(DotFlavor.charToDotFlavor(dots.charAt(i)), x,y));
                x++;
            }
       }
       dotMap.add(currRow);
    }
    
    public Dot getDot(int x, int y) {
        return dotMap.get(y).get(x);
    } 
    
    public int getNumCols() {
        return dotMap.get(0).size();
    }
    
    public int getNumRows() {
        return dotMap.size();
    }
    
    public void findAllMovesForAllDots() {        
        allMoves = new HashSet<>();
        for(int currRow = 0; currRow < getNumRows(); currRow++) {
            for(int currCol = 0; currCol < getNumCols(); currCol++) {
                Dot currDot = getDot(currRow, currCol);
                if(currDot.dotFlavor.isColor && dotHasMoves(currDot)) {
                    allMoves.addAll( findAllMovesForDot(currDot) );
                }   
            }
        } 
    }
    
    public HashSet<Move> findAllMovesForDot(Dot daDot) {
        HashSet<Move> dotMoves = new HashSet<>();
        
        //get immediate moves
        for(Dot neighbor:getMatchingNeighbors(daDot)){
            Move currMove = new Move();
            currMove.add(daDot);
            currMove.add(neighbor);
            dotMoves.add(currMove);
        }
        
        return dotMoves;
    }
    
    public boolean dotHasMoves(Dot daDot) {
        return getMatchingNeighbors(daDot).size() > 0;
    }
    
    //dot is already assumed to be colored by this point
    public ArrayList<Dot> getMatchingNeighbors(Dot centerDot) {
        ArrayList<Dot> matchingNeighbors = new ArrayList<>();
        
        if(centerDot.x != 0 && centerDot.matching(getLeftDot(centerDot))) {
            matchingNeighbors.add(getLeftDot(centerDot));
        }
        if(centerDot.x != (getNumCols()-1) && centerDot.matching(getRightDot(centerDot))) {
            matchingNeighbors.add(getRightDot(centerDot)); 
        }
        if(centerDot.y != 0 && centerDot.matching(getUpDot(centerDot))) {
            matchingNeighbors.add(getUpDot(centerDot));
        }
        if(centerDot.y != (getNumRows()-1) && centerDot.matching(getDownDot(centerDot))) {
            matchingNeighbors.add(getDownDot(centerDot));
        }
        
        return matchingNeighbors;
    }
    
    public Dot getLeftDot(Dot centerDot) {
        return getDot(centerDot.x-1,centerDot.y);
    }
    
    public Dot getRightDot(Dot centerDot) {
        return getDot(centerDot.x+1,centerDot.y);
    }
    
    public Dot getUpDot(Dot centerDot) {
        return getDot(centerDot.x,centerDot.y-1);
    }
    
    public Dot getDownDot(Dot centerDot) {
        return getDot(centerDot.x,centerDot.y+1);
    }
}
