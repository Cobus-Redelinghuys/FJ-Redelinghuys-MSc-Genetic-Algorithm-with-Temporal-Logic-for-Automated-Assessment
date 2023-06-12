import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class ChromosomeDatabase {
    static HashMap<Integer, ArrayList<Chromosome>>[] db;

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

    static float G(Chromosome chromosome, int gen){
        float sum = 0;
        for (int i = 0; i < db.length; i++) {
            if(db[i].containsKey(chromosome.genes[i])){
                sum += (float)db[i].get(chromosome.genes[i]).size() / (float)gen; 
            } else {
                sum += 0;
            }
        }
        return sum * Config.GWeight;
    }

}
