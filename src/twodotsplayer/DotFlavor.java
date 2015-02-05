package twodotsplayer;

public enum DotFlavor {
    RED('r', true), 
    BLUE('b', true), 
    PURPLE('p',true), 
    YELLOW('y',true), 
    GREEN('g',true), 
    
    WHITE('w',true),
    CYCLE('*',false),
    
    ANCHOR('a',false),
    BLOCK('k',false),
    FIRE('f',false),
    BLANK('-',false),
    EMPTY('_',false),
    UNKNOWN_OR_ERROR('?',false);
    
    char dotChar;
    boolean isColor;
    
    DotFlavor(char dotChar, boolean isColor) {
        this.dotChar = dotChar;
        this.isColor = isColor;
    }
    
    
    public char toChar() {
        return this.dotChar;
    }
    
    public static DotFlavor charToDotFlavor(char dotChar) {
        if(dotChar == 'r') {
            return RED;
        }
        else if(dotChar == 'b') {
            return BLUE;
        }
        else if(dotChar == 'p') {
            return PURPLE;
        }
        else if(dotChar == 'y') {
            return YELLOW;
        }
        else if(dotChar == 'g') {
            return GREEN;
        }
        else if(dotChar == 'w') {
            return WHITE;
        }
        else if(dotChar == 'a') {
            return ANCHOR;
        }
        else if(dotChar == '-') {
            return BLANK;
        }
        else if(dotChar == '_') {
            return EMPTY;
        }
        return UNKNOWN_OR_ERROR;
    }
}
