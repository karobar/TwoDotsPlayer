package twodotsplayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class TwoDotsPlayer {

    public static void main(String[] args) throws FileNotFoundException {
        Constructor constructor = new Constructor(GameConfiguration.class);
        TypeDescription gameConfigDesc = new TypeDescription(GameConfiguration.class);
        
        gameConfigDesc.putListPropertyType("transporters", Transporter.class);
        
        constructor.addTypeDescription(gameConfigDesc);
        Yaml yaml = new Yaml(constructor);
        
        GameConfiguration testGameConfig = (GameConfiguration) yaml.load(new FileInputStream("src/twodotsplayer/gameConfig01.yml"));
        
        System.out.println("" + testGameConfig.transporters.get(0).dropper);
        System.out.println("" + testGameConfig.dots);
        testGameConfig.parse();
        
        System.out.println("" + testGameConfig.getNumRows() + "," + testGameConfig.getNumCols());
        
        Dot testDot = testGameConfig.getDot(7,0);
        testGameConfig.findAllMovesForAllDots();
                
        System.out.println(testDot + ":" + testGameConfig.allMoves);
    }
}
