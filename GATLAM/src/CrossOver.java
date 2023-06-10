abstract public class CrossOver {
    abstract Chromosome[] crossOver(Chromosome chromosomeA, Chromosome chromosomeB);

    static CrossOver getCrossOver(String type) throws RuntimeException{
        if(type.equals("OnePointCrossOver")){
            return new OnePointCrossover();
        }
        throw new RuntimeException("Invalid CrossOverType");
    }
}

class OnePointCrossover extends CrossOver{
    @Override
    Chromosome[] crossOver(Chromosome chromosomeA, Chromosome chromosomeB) {
        Chromosome[] result = new Chromosome[2];
        String chromosomeABits = chromosomeA.bits;
        String chromosomeBBits = chromosomeB.bits;
        for (int i = 0; i < Config.maxCrossOverAttempts; i++) {
            int crossOverPoint = Config.random.nextInt(chromosomeABits.length());
            String nOffSpringA = chromosomeABits.substring(0, crossOverPoint) + chromosomeBBits.substring(crossOverPoint);
            String nOffSpringB = chromosomeBBits.substring(0, crossOverPoint) + chromosomeABits.substring(crossOverPoint);
            result[0] = new Chromosome(nOffSpringA);
            result[1] = new Chromosome(nOffSpringB);
            if(!result[0].isValid() || !result[1].isValid()){
                result[0] = null;
                result[1] = null;
            } else {
                return result;
            }
        }

        result[0] = new Chromosome(chromosomeA);
        result[1] = new Chromosome(chromosomeB);
        return result;
    }
}