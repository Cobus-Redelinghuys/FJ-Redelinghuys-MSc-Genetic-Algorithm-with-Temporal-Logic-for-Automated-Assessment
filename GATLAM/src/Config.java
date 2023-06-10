import java.io.FileReader;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Config {
    static final GeneConfig[] genes; 
    static final Random random;
    public static final double reproductionProp;
    public static final double crossoverProp;
    public static final int seed;
    public static final String mutationType;
    public static final int selectionSize;
    public static final int populationSize;
    public static final int numGenerations;
    public static final String crossOverType;
    public static final double mutationProp;
    public static final int nCrossOver;
    public static final int tournamentSize;
    public static final String interpreterPath;
    public static final String interpreterCommand;
    public static final String interpreterOutputPath;
    public static final double LTLWeight;
    public static final double MWeight;
    public static final double GWeight;
    public static final int maxCrossOverAttempts;

    public static final CrossOver crossOver;

    static{
        JSONParser jsonParser = new JSONParser();
        Object obj = null;
        try{
            obj = jsonParser.parse(new FileReader("Config.json"));
        } catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject)obj;
        JSONArray geneArray = (JSONArray)jsonObject.get("Genes");
        GeneConfig[] tempArr = new GeneConfig[geneArray.size()];
        for(int i=0; i < tempArr.length; i++){
            JSONObject jObject = (JSONObject)geneArray.get(i);
            tempArr[i] = GeneConfig.getGeneConfig(jObject);
        }
        genes = tempArr;
        reproductionProp = (double) jsonObject.get("reproductionProp");
        crossoverProp = (double) jsonObject.get("crossoverProp");
        seed = ((Long) jsonObject.get("seed")).intValue();
        mutationType = (String) jsonObject.get("mutationType");
        selectionSize = ((Long) jsonObject.get("selectionSize")).intValue();
        populationSize = ((Long) jsonObject.get("populationSize")).intValue();
        numGenerations = ((Long) jsonObject.get("numGenerations")).intValue();
        crossOverType = (String) jsonObject.get("crossOverType");
        mutationProp = (double) jsonObject.get("mutationProp");
        nCrossOver = ((Long) jsonObject.get("nCrossOver")).intValue();
        tournamentSize = ((Long) jsonObject.get("tournamentSize")).intValue();
        interpreterPath = (String) jsonObject.get("interpreterPath");
        interpreterCommand = (String) jsonObject.get("interpreterCommand");
        interpreterOutputPath = (String) jsonObject.get("interpreterOutputPath");
        LTLWeight = (double)jsonObject.get("LTLWeight");
        MWeight = (double)jsonObject.get("MWeight");
        GWeight = (double)jsonObject.get("GWeight");
        random = new Random(seed);
        maxCrossOverAttempts = ((Long)jsonObject.get("maxCrossOverAttempts")).intValue();
        crossOver = CrossOver.getCrossOver(crossOverType);
    }
}