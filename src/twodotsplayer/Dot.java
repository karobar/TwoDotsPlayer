package twodotsplayer;

public class Dot {
    DotFlavor dotFlavor;
    int x,y;
    
    Dot(DotFlavor dotFlavor, int x, int y) {
        this.dotFlavor = dotFlavor;
        this.x = x;
        this.y = y;
    }
    
    public boolean matching(Dot otherDot) {
        if(dotFlavor == otherDot.dotFlavor) {
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        String xString = x + "";
        String yString = y + "";
        return dotFlavor.toChar() + xString + yString;
    }
}
