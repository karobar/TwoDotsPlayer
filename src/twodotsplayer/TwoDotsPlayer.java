package twodotsplayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class TwoDotsPlayer {
    static final int MAX_LEVEL = 2;
    
    static void DFS(GameConfiguration startGameConfig) {
        HashSet<GameConfiguration> unexploredConfigs = new HashSet<>();
        unexploredConfigs.add(startGameConfig);
        int level = 0;
        HashSet<GameConfiguration> nextLevelConfigs = new HashSet<>();
        ArrayList<Move> maxPath = new ArrayList<>();
        int maxScore = 0;
        while(!unexploredConfigs.isEmpty() && level < MAX_LEVEL) {
            for(GameConfiguration currUnexGameConfig : unexploredConfigs) {
                currUnexGameConfig.findAllMovesForAllDots();
                //System.out.println("Size " + startGameConfig.allMoves.size() + ":" + startGameConfig.allMoves); 
                for(Move move : currUnexGameConfig.allMoves) {   
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
//                        System.out.println("MAX PATH = " + maxPath);
//                        System.out.println(currConfiguration + "");
                    }
                    
//                    System.out.println("Path: " + currConfiguration.path);
//                    System.out.println("Score: " + currConfiguration.score);
//                    System.out.println("" + currConfiguration);
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
        //System.out.println("Initial drop complete: \n" + startGameConfig);
        
        DFS(startGameConfig);
        
        //Dot testDot = testGameConfig.getDot(0,0);
        //HashSet<Move> singleDotMoveSet = testGameConfig.findAllMovesForDot(testDot, null, new HashSet<Dot>());
        //System.out.println("singleDotMoveSet:" + singleDotMoveSet);
          
//        Move lastMove = new Move();
//        for(Move move: allCycleMoves) {
//            System.out.println("Cycle:" + move);
//            lastMove = move;
//        }
        
    }
}
