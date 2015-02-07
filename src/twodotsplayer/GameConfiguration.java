package twodotsplayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameConfiguration {
    public List<Transporter> transporters;
    public String dots;
    public DotMap dotMap;
    HashSet<Move> allMoves;
    public int score;
    public ArrayList<Move> path;
    public String iceOverlay;
    public String objectives;
     
    GameConfiguration() {
        this.score = 0;
        this.path = new ArrayList<>();
        this.dotMap = new DotMap();
    }
    
    GameConfiguration(DotMap dotMap, ArrayList<Move> path, Move newMove) {
        this.score = 0;
        this.dotMap = new DotMap();
        
        this.path = new ArrayList<>();
        for(Move daMove : path) {
            this.path.add(daMove);
        }
        for(Ice ice : dotMap.iceLevels) {
            this.dotMap.iceLevels.add(new Ice(ice.x,ice.y,ice.level));
        } 
        
        this.path.add(newMove);
        
        //initialize empty map      
        for(ArrayList<Dot> dotArray : dotMap) {
            ArrayList<Dot> copiedDotArray = new ArrayList<>();
            this.dotMap.add(copiedDotArray);
            for(int x = 0; x<dotMap.get(0).size(); x++) {
                Dot errorDot = new Dot(DotFlavor.UNKNOWN_OR_ERROR, 0, 0);
                copiedDotArray.add(errorDot);
            }
            for(Dot dot : dotArray) {
                Dot dotCopy = new Dot(dot.dotFlavor, dot.getX(), dot.getY());
                setDot(dotCopy);
            }
        }
        
        if(!this.isProper()) {
            System.out.println("IMPROPER (Post-Constructor): \n" + this);
        }
    }
    
    public boolean isProper() {
        for(int i=0;i<this.getNumRows();i++) {
            for(int j=0;j<this.getNumCols();j++) {
                Dot curr = getDot(j,i);
                if(curr.getX() != j || curr.getY() != i) {
                    System.out.println("MISMATCH: loc=(" + j + "," + i +"),dot=(" + curr.getX() + "," + curr.getY() + ")");
                    return false;
                }
            }
        }
        return true;
    }
    
    public List<Transporter> getTransporters() {
        return transporters;
    }
    
    public void setTransporters(List<Transporter> transporters) {
        this.transporters = transporters;
    }
    
    public String getDots() {
        return dots;
    }
    
    @Override
    public String toString() {
        String retStr = "";
        
        for(int curY=0;curY<dotMap.size();curY++) {
            for (int curX=0;curX<dotMap.get(0).size();curX++) {
                retStr = retStr + dotMap.get(curY).get(curX).dotFlavor.toChar();
            }
            retStr = retStr + "\n";
        }        
        return retStr;
    }
    
    public String toString(int tabs) {
        String retStr = "";
        
        for(int curY=0;curY<dotMap.size();curY++) {
            retStr = retStr + TwoDotsPlayer.tabs(tabs);
            for (int curX=0;curX<dotMap.get(0).size();curX++) {
                retStr = retStr + dotMap.get(curY).get(curX).dotFlavor.toChar();
            }
            retStr = retStr + "\n";
        }        
        return retStr;
    }
    
    //returns the number of dots removed
    public void remove(Move daMove, Objective returnObjective) {
        for(Dot dot : daMove) {
            if(dot.dotFlavor != DotFlavor.CYCLE) {  //think this check is superfluous
                for(Dot neighboringBlockDot : getBlockNeighbors(dot)) {
                    Dot emptyDot = new Dot(DotFlavor.EMPTY, neighboringBlockDot.getX(), neighboringBlockDot.getY());
                    setDot(emptyDot);
                    returnObjective.brokenBoxes++;
                }
                Dot emptyDot = new Dot(DotFlavor.EMPTY, dot.getX(), dot.getY());
                setDot(emptyDot);
                if (this.dotMap.breakIce(dot.getX(), dot.getY())) {
                    returnObjective.brokenIce++;
                }
                returnObjective.removedDots++;
            }
        }
        if(daMove.isCycle()) {
            returnObjective.squares++;
            for(int i=0;i<dotMap.size();i++) {
                for(int j=0;j<dotMap.get(i).size();j++) {
                    Dot currDot = dotMap.get(i).get(j);
                    if(currDot.dotFlavor == daMove.color || daMove.allWhiteCycle()) {
                        for(Dot neighboringBlockDot : getBlockNeighbors(currDot)) {
                            Dot emptyDot = new Dot(DotFlavor.EMPTY, neighboringBlockDot.getX(), neighboringBlockDot.getY());
                            setDot(emptyDot);
                            returnObjective.brokenBoxes++;
                        }
                        Dot emptyDot = new Dot(DotFlavor.EMPTY, currDot.getX(), currDot.getY());
                        setDot(emptyDot);
                        if(this.dotMap.breakIce(currDot.getX(), currDot.getY())) {
                            returnObjective.brokenIce++;
                        }
                        returnObjective.removedDots++;
                    }
                }
            }
        }
    }
    
    public void setDots(String dots) {
        this.dots = dots;
    }
    
    public DotMap parse() {
       DotMap parsedDotMap = new DotMap();
       ArrayList<Dot> currRow = new ArrayList<>();
       int x = 0;
       int y = 0;       
       for(int i = 0; i < dots.length(); i++) {
            if(dots.charAt(i)=='\n') {
                parsedDotMap.add(currRow);
                currRow = new ArrayList<>();
                y++;
                x = 0;
            }
            else {
                Dot newDot = new Dot(DotFlavor.charToDotFlavor(dots.charAt(i)), x,y);
                currRow.add(newDot);
                if(iceOverlay != null){
                    //System.out.println("pre-try");
                    try { 
                        String iceStr = "" + iceOverlay.charAt(i);
                        parsedDotMap.addIce(new Ice(x,y,Integer.parseInt(iceStr)));
                        //System.out.println("post-try: " + parsedDotMap.iceLevels.size());
                    } catch(NumberFormatException e) {}
                }
                x++;
            }
       }
       //parsedDotMap.add(currRow);
//       for(int i = 0; i<parsedDotMap.size(); i++) {
//           System.out.println(parsedDotMap.get(i).size());
//       }
       return parsedDotMap;
    }
    

    
    public int getNumCols() {
        return dotMap.get(0).size();
    }
    
    public boolean hasAnchors() {
        for(ArrayList<Dot> dotLine : this.dotMap) {
            for(Dot dot : dotLine) {
                if(dot.dotFlavor == DotFlavor.ANCHOR) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public int getNumRows() {
        return dotMap.size();
    }
    
    public HashSet<Move> findAllMovesForAllDots() {        
        HashSet<Move> allMoves = new HashSet<>();
        for(int currRow = 0; currRow < getNumRows(); currRow++) {
            for(int currCol = 0; currCol < getNumCols(); currCol++) {
                //getDot uses (x,y) notation
                Dot currDot = getDot(currCol, currRow);
                if(currDot.dotFlavor.isColor && dotHasMoves(currDot, currDot.dotFlavor)) {
                    allMoves.addAll( findAllMovesForDot(currDot,null, new HashSet<Dot>(),currDot.dotFlavor) );
                }   
            }
        }
        return allMoves;
    }
    
    public HashSet<Move> findAllMovesForDot(Dot center, Dot exceptionDot, HashSet<Dot> path, DotFlavor beginningFlavor) {
        HashSet<Move> dotMoves = new HashSet<>();
        
        HashSet<Dot> validNeighbors = getMatchingNeighbors(center,beginningFlavor);
        validNeighbors.remove(exceptionDot);
        if(validNeighbors.size() > 0) {
            for(Dot neighbor: validNeighbors){
                Move currMove = new Move(center.dotFlavor);
                currMove.add(center);
                currMove.add(neighbor);    
                if(!path.contains(neighbor)) { 
                    dotMoves.add(currMove);
                    HashSet<Dot> newPath = (HashSet<Dot>)path.clone();
                    newPath.add(center);
                    //recursive step
                    for(Move extendedMoves : findAllMovesForDot(neighbor,center, newPath,beginningFlavor)) {
                        extendedMoves.add(center);
                        dotMoves.add(extendedMoves);
                    }
                }
                else if (path.contains(neighbor)) {
                    currMove.makeCycle(); 
                    dotMoves.add(currMove);
                }
                
            }
        }
        return dotMoves;
    }
    
    public boolean dotHasMoves(Dot daDot, DotFlavor beginningFlavor) {
        return getMatchingNeighbors(daDot,beginningFlavor).size() > 0;
    }
    
    public HashSet<Dot> getBlockNeighbors(Dot centerDot) {
        HashSet<Dot>  blockNeighbors = new HashSet<>();    
        if(centerDot.getX() != 0 && getLeftDot(centerDot).dotFlavor == DotFlavor.BLOCK) {
            blockNeighbors.add(getLeftDot(centerDot));
        }
        if(centerDot.getX() != (getNumCols()-1) && getRightDot(centerDot).dotFlavor == DotFlavor.BLOCK) {
            blockNeighbors.add(getRightDot(centerDot));
        }
        if(centerDot.getY() != 0 && getUpDot(centerDot).dotFlavor == DotFlavor.BLOCK) {
            blockNeighbors.add(getUpDot(centerDot));
        }
        if(centerDot.getY() != (getNumRows()-1) && getDownDot(centerDot).dotFlavor == DotFlavor.BLOCK) {
            blockNeighbors.add(getDownDot(centerDot));
        }
        return blockNeighbors;
    }
    
    
    //dot is already assumed to be colored by this point
    public HashSet<Dot> getMatchingNeighbors(Dot centerDot, DotFlavor beginningFlavor) {
        HashSet<Dot> matchingNeighbors = new HashSet<>();
        if(centerDot.getX() != 0 && centerDot.matching(getLeftDot(centerDot),beginningFlavor)) {
            matchingNeighbors.add(getLeftDot(centerDot));
        }
        if(centerDot.getX() != (getNumCols()-1) && centerDot.matching(getRightDot(centerDot),beginningFlavor)) {
            matchingNeighbors.add(getRightDot(centerDot)); 
        }
        if(centerDot.getY() != 0 && centerDot.matching(getUpDot(centerDot),beginningFlavor)) {
            matchingNeighbors.add(getUpDot(centerDot));
        }
        if(centerDot.getY() != (getNumRows()-1) && centerDot.matching(getDownDot(centerDot),beginningFlavor)) {
            matchingNeighbors.add(getDownDot(centerDot));
        }        
        return matchingNeighbors;
    }
    
    public Dot getLeftDot(Dot centerDot) {
        return getDot(centerDot.getX()-1,centerDot.getY());
    }
    
    public Dot getRightDot(Dot centerDot) {
        return getDot(centerDot.getX()+1,centerDot.getY());
    }
    
    public Dot getUpDot(Dot centerDot) {
        return getDot(centerDot.getX(),centerDot.getY()-1);
    }
    
    public Dot getDownDot(Dot centerDot) {
        return getDot(centerDot.getX(),centerDot.getY()+1);
    }
     
    //all side-effects
    public void drop(Objective daObjective) {     
        for(int x = 0; x < dotMap.get(0).size();x++) {      
            daObjective.droppedAnchors += dropColumn(x);
        }
    }
    
    //uses side effects to change dot positions
    public int dropColumn(int col) {
        boolean allEmpty = true;
        int droppedAnchors = 0;       
        //work from bottom to top...
        //System.out.println("dropColumn:" + dotMap.getNumCols() + "," + dotMap.getNumRows());
        for(int y = dotMap.size()-1;y >= 0;) {
            Dot currDot = this.getDot(col,y);                           
            if(currDot.isEmpty()) {
                allEmpty = runAndSwap(col,y,currDot);
            }
            else {
                allEmpty = false;
            }
            //If there's an anchor on the bottom layer, delete it
            if((currDot.dotFlavor == DotFlavor.ANCHOR) && (y == dotMap.size()-1)) {     
                setDot(new Dot(DotFlavor.EMPTY, col,y));
                droppedAnchors++;
            }
            //update the content of currDot
//            currDot = this.dotMap.get(y).get(col);
            if ( (!currDot.isEmpty() && !(currDot.dotFlavor == DotFlavor.ANCHOR && y==dotMap.size()-1)) || 
                  allEmpty){
                y--;
            } 
        }
        return droppedAnchors;
    }
    
    public boolean runAndSwap(int x, int y, Dot currDot) {
        boolean allEmpty = true;
        //search upwards until a non-empty dot is found 
        for(int runnerY = y;runnerY >= 0;runnerY--) {
            Dot runner = this.dotMap.get(runnerY).get(x);
            if(runner.dotFlavor == DotFlavor.BLOCK) {
                //(break for-loop)
                runnerY=-1;
            }
            else if(!runner.isEmpty() && (runner.dotFlavor != DotFlavor.BLANK)) {
                if(!this.isProper()) {
                    System.out.println("IMPROPER (Pre-swap): \n" + this);
                }
                allEmpty = false;                 
                Dot tempDot = new Dot(currDot.dotFlavor, currDot.getX(),runnerY);
                Dot runnerCopy = new Dot(runner.dotFlavor, runner.getX(), y);
                setDot(runnerCopy);                           
                setDot(tempDot);
                if(!this.isProper()) {
                    System.out.println("IMPROPER (Post-swap): \n" + this);
                }
                //(break for-loop)
                runnerY=-1;
            }
        }
        return allEmpty;
    }
    
    void setDot(Dot daDot) {
        this.dotMap.get(daDot.getY()).set(daDot.getX(),daDot);
    }
    
    public Dot getDot(int x, int y) {
        //System.out.println("getting ("+x+","+y+")");
        return dotMap.get(y).get(x);
    } 
}
