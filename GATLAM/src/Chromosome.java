public class Chromosome{
    final String[] genes = new String[Config.genes.length];
    final String bits;

    Chromosome(){
        String tempBits = "";
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Config.genes[i].generateGene();
            tempBits += Config.genes[i].toBinaryString(genes[i]);
        }
        bits = tempBits;
    }

    Chromosome(Chromosome other){
        for (int i = 0; i < genes.length; i++) {
            genes[i] = other.genes[i];
        }
        bits = other.bits;
    }

    Chromosome(String[] genes){
        String tempBits = "";
        for (int i = 0; i < genes.length; i++) {
            this.genes[i] = genes[i];
            tempBits += Config.genes[i].convertFromBin(genes[i]);
        }
        bits = tempBits;
    }

    @SuppressWarnings("rawtypes")
    Chromosome(String bits) throws RuntimeException{
        String resBits = bits;
        for (int i=0; i < Config.genes.length; i++) {
            GeneConfig geneConfig = Config.genes[i];
            if(resBits.length() < geneConfig.numBits()){
                throw new RuntimeException("Invalid number of bits to form chromosome");
            } else {
                String geneStr = resBits.substring(0, geneConfig.numBits());
                this.genes[i] = geneConfig.convertFromBin(geneStr).toString();
            }
        }
        this.bits = bits;
    }
}