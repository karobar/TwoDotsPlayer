package twodotsplayer;

public class Dot {
    DotFlavor dotFlavor;
    private int x,y;
    
    Dot(DotFlavor dotFlavor, int x, int y) {
        this.dotFlavor = dotFlavor;
        this.x = x;
        this.y = y;
    }
    
    public boolean matching(Dot otherDot,DotFlavor origFlavor) {
        if(dotFlavor == otherDot.dotFlavor || 
                (dotFlavor == DotFlavor.WHITE && (otherDot.dotFlavor == origFlavor || otherDot.dotFlavor == DotFlavor.WHITE)) || 
                otherDot.dotFlavor == DotFlavor.WHITE) {
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        String xString = x + "";
        String yString = y + "";
        return dotFlavor.toChar() + ":(" + xString + "," + yString + ")";
    }
    
    public boolean isEmpty() {
        return DotFlavor.EMPTY==this.dotFlavor;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getY() {
        return this.y;
    }
}
