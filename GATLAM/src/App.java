public class App {
    public static void main(String[] args) throws Exception {
        Chromosome chromosomeA = new Chromosome();
        Chromosome chromosomeB = new Chromosome();
        Chromosome[] result = Config.crossOver.crossOver(chromosomeA, chromosomeB);
        System.out.println(chromosomeA.bits);
        System.out.println(chromosomeB.bits);
        for (Chromosome chromosome : result) {
            System.out.println(chromosome.bits);
        }
        System.out.println(Config.reproductionProp);
    }
}
