import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class ChromosomeDatabase {
    static HashMap<Integer, ArrayList<Chromosome>>[] db;
    static HashMap<Integer, HashMap<Chromosome, ChromosomeDBInfo>> chromosomeDBInfo = new HashMap<>();

    static{
        db = new HashMap[Config.genes.length];
        for (int i = 0; i < db.length; i++) {
            db[i] = new HashMap<>();
        }

    }

    static void addChromosome(Chromosome chromosome){
        for (int i = 0; i < Config.genes.length; i++) {
            if(!db[i].containsKey(chromosome.genes[i])){
                db[i].put(chromosome.genes[i], new ArrayList<>());
            }
            db[i].get(chromosome.genes[i]).add(chromosome);
        }
    }

    static void addDBInfo(ChromosomeDBInfo cDBInfo){
        if(!chromosomeDBInfo.containsKey(cDBInfo.gen)){
            chromosomeDBInfo.put(cDBInfo.gen, new HashMap<>());
        }

        if(!chromosomeDBInfo.get(cDBInfo.gen).containsKey(cDBInfo.chromosome)){
            chromosomeDBInfo.get(cDBInfo.gen).put(cDBInfo.chromosome, cDBInfo);
        } else{
            chromosomeDBInfo.get(cDBInfo.gen).put(cDBInfo.chromosome, cDBInfo);
        }
    }

    static ChromosomeDBInfo get(Chromosome chromosome, int gen){
        return chromosomeDBInfo.get(gen).get(chromosome);
    }

    static float G(Chromosome chromosome, int gen){
        ChromosomeDBInfo chromosomeDBInfo = get(chromosome, gen);
        chromosomeDBInfo.gSubValues = new float[db.length];
        float sum = 0;
        for (int i = 0; i < db.length; i++) {
            if(db[i].containsKey(chromosome.genes[i])){
                sum += (float)db[i].get(chromosome.genes[i]).size() / (float)gen;
                chromosomeDBInfo.gSubValues[i] =  (float)db[i].get(chromosome.genes[i]).size() / (float)gen;
            } else {
                sum += 0;
                chromosomeDBInfo.gSubValues[i] = 0;
            }
        }
        addDBInfo(chromosomeDBInfo);
        return sum * Config.GWeight;
    }

}

class ChromosomeDBInfo{
    public Chromosome chromosome;
    public float Safety;
    public float Livelyness;
    public float SegFault;
    public float Exceptions;
    public float ExecutionTime;
    public float IllegalOutput;
    public float ExpectedOutput;
    public float m;
    public float g;
    public float ltl;
    public float[] gSubValues;
    public int gen;

    ChromosomeDBInfo(Chromosome chromosome, int gen){
        this.chromosome = chromosome;
        this.gen = gen;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SegFault", SegFault);
        jsonObject.put("Safety", Safety);
        jsonObject.put("Livelyness", Livelyness);
        jsonObject.put("Exceptions", Exceptions);
        jsonObject.put("ExecutionTime", ExecutionTime);
        jsonObject.put("IllegalOutput", IllegalOutput);
        jsonObject.put("ExpectedOutput", ExpectedOutput);
        jsonObject.put("m", m);
        jsonObject.put("g", g);
        jsonObject.put("ltl", ltl);
        JSONArray gArray = new JSONArray();
        for (Float gVal : gSubValues) {
            gArray.add(gVal);
        }
        jsonObject.put("gSubValues", gArray);

        return jsonObject;
    }
}