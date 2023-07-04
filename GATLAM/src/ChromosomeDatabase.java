import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class ChromosomeDatabase {
    static HashMap<Integer, ArrayList<Chromosome>>[] db;
    static HashMap<Integer, HashMap<Chromosome, ChromosomeDBInfo>> chromosomeDBInfo = new HashMap<>();

    static {
        db = new HashMap[Config.genes.length];
        for (int i = 0; i < db.length; i++) {
            db[i] = new HashMap<>();
        }

    }

    static void addChromosome(Chromosome chromosome) {
        for (int i = 0; i < Config.genes.length; i++) {
            if (!db[i].containsKey(chromosome.genes[i])) {
                db[i].put(chromosome.genes[i], new ArrayList<>());
            }
            db[i].get(chromosome.genes[i]).add(chromosome);
        }
    }

    static void addDBInfo(ChromosomeDBInfo cDBInfo) {
        if (!chromosomeDBInfo.containsKey(cDBInfo.gen)) {
            chromosomeDBInfo.put(cDBInfo.gen, new HashMap<>());
        }

        if (!chromosomeDBInfo.get(cDBInfo.gen).containsKey(cDBInfo.chromosome)) {
            chromosomeDBInfo.get(cDBInfo.gen).put(cDBInfo.chromosome, cDBInfo);
        } else {
            chromosomeDBInfo.get(cDBInfo.gen).put(cDBInfo.chromosome, cDBInfo);
        }
    }

    static ChromosomeDBInfo get(Chromosome chromosome, int gen) {
        return chromosomeDBInfo.get(gen).get(chromosome);
    }

    static float G(Chromosome chromosome, int gen) {
        ChromosomeDBInfo chromosomeDBInfo = get(chromosome, gen);
        chromosomeDBInfo.gSubValues = new float[db.length];
        float sum = 0;
        for (int i = 0; i < db.length; i++) {
            if (db[i].containsKey(chromosome.genes[i])) {
                int size = db[i].get(chromosome.genes[i]).size();
                int tGen = gen+1;
                sum += (float) size / (float) tGen;
                chromosomeDBInfo.gSubValues[i] = (float) size / (float) tGen;
            } else {
                sum += 0;
                chromosomeDBInfo.gSubValues[i] = 0;
            }
        }
        addDBInfo(chromosomeDBInfo);
        return sum * Config.GWeight;
    }

    static JSONObject dbDump() {
        JSONObject result = new JSONObject();
        for (Integer gen : chromosomeDBInfo.keySet()) {
            JSONArray genInfo = new JSONArray();
            for (Chromosome chromosome : chromosomeDBInfo.get(gen).keySet()) {
                JSONObject jsonObject = new JSONObject();
                JSONObject dbInfo = chromosomeDBInfo.get(gen).get(chromosome).toJSON();
                jsonObject.put("FitnessInfo", dbInfo);
                jsonObject.put("ChromosomInfo", chromosome.toJSON());
                genInfo.add(jsonObject);
            }
            result.put(gen, genInfo);
        }
        return result;
    }

    static void printToFile(String fileName) {
        JSONObject jsonObject = dbDump();
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(jsonObject.toJSONString());
            System.out.println("JSON object has been written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class ChromosomeDBInfo {
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

    ChromosomeDBInfo(Chromosome chromosome, int gen) {
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