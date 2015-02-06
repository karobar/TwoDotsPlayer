package twodotsplayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class TwoDotsPlayer {
    static final int MAX_LEVEL = 4;
    
    static final boolean PRINT_DFS = false;
    
    static String tabs(int numTabs) {
        char[] tabArray = new char[numTabs];
        for(int i = 0; i < tabArray.length;i++) {
            tabArray[i] = '\t';
        }
        return new String(tabArray);
    }
    
    //Depth-First Search
    static ScorePackage DFS(GameConfiguration startGameConfig, int currLevel) {     
        if(PRINT_DFS) { System.out.println(tabs(currLevel) + "Beginning DFS at level:" + currLevel); }
        if(PRINT_DFS) { System.out.println(tabs(currLevel) + "START CONFIG:\n" + startGameConfig.toString(currLevel)); } 
        
        ScorePackage currPackage = new ScorePackage();  
        ScorePackage retPackage = new ScorePackage();
        if (currLevel < MAX_LEVEL) {
            HashSet<Move> allMoves = startGameConfig.findAllMovesForAllDots();
            if(PRINT_DFS) { System.out.println("allmoves="+allMoves); }
            for(Move move : allMoves) {
                GameConfiguration currConfiguration = new GameConfiguration(startGameConfig.dotMap, startGameConfig.path, move);
                int removedDots = currConfiguration.remove(move);
                int droppedAnchors = currConfiguration.drop();           
                if(PRINT_DFS) { System.out.println("new configuration (after "+move+"):\n" + currConfiguration.toString(currLevel)); }                
                if(!currConfiguration.hasAnchors()) {
                    currConfiguration.score = startGameConfig.score + removedDots; 
                }
                else {
                    currConfiguration.score = startGameConfig.score + droppedAnchors;
                }
                if (currConfiguration.score > retPackage.score) {
                    retPackage.path = currConfiguration.path;
                    retPackage.score = currConfiguration.score;
                }
                ScorePackage newPackage = DFS(currConfiguration, (currLevel+1));
                if (newPackage.score > retPackage.score) {
                    retPackage.path = newPackage.path;
                    retPackage.score = newPackage.score;
                }
            }
        }
        if(PRINT_DFS) { System.out.println(tabs(currLevel) + "DFS complete: score:" + retPackage.score); }
        return retPackage;
    }
    
    static void BFS(GameConfiguration startGameConfig) {  
        HashSet<GameConfiguration> unexploredConfigs = new HashSet<>();
        unexploredConfigs.add(startGameConfig);
        int level = 0;
        HashSet<GameConfiguration> nextLevelConfigs = new HashSet<>();
        
        ArrayList<Move> maxPath = new ArrayList<>();
        int maxScore = 0;
        while(!unexploredConfigs.isEmpty() && level < MAX_LEVEL) {
            for(GameConfiguration currUnexGameConfig : unexploredConfigs) {
                HashSet<Move> allMoves = currUnexGameConfig.findAllMovesForAllDots();
                //System.out.println("Size " + startGameConfig.allMoves.size() + ":" + startGameConfig.allMoves); 
                for(Move move : allMoves) {   
                    GameConfiguration currConfiguration = new GameConfiguration(currUnexGameConfig.dotMap, currUnexGameConfig.path, move);
                    //remove all dots through side-effects
                    int removedDots = currConfiguration.remove(move);
                    int droppedAnchors = currConfiguration.drop();           
                    if(!currConfiguration.hasAnchors()) {
                        currConfiguration.score = currUnexGameConfig.score + removedDots; 
                    }
                    else {
                        currConfiguration.score = currUnexGameConfig.score + droppedAnchors;
                    }
                    if (currConfiguration.score > maxScore) {                 
                        maxPath = currConfiguration.path;
                        maxScore = currConfiguration.score;
                    }
                    nextLevelConfigs.add(currConfiguration);
                } 
            }
            unexploredConfigs.clear();
            unexploredConfigs.addAll(nextLevelConfigs);
            nextLevelConfigs.clear();
            System.out.println("================================NEW LEVEL=======================================");
            level++;
        }  
        System.out.println("Best path (Score=" + maxScore + "):" + maxPath);
    } 
    
    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException {
        Constructor constructor = new Constructor(GameConfiguration.class);
        TypeDescription gameConfigDesc = new TypeDescription(GameConfiguration.class);
        
        gameConfigDesc.putListPropertyType("transporters", Transporter.class);
        
        constructor.addTypeDescription(gameConfigDesc);
        Yaml yaml = new Yaml(constructor);
        
        GameConfiguration startGameConfig = (GameConfiguration) yaml.load(new FileInputStream("src/twodotsplayer/gameConfig05.yml"));
        
        System.out.println("" + startGameConfig.dots);
        startGameConfig.parse();
        //in case of a weird initial config, just drop it
        startGameConfig.drop();
        //BFS(startGameConfig);   
        ScorePackage maxPackage = DFS(startGameConfig,0);
        System.out.println("Best path (Score=" + maxPackage.score + "):" + maxPackage.path);
    }
}
