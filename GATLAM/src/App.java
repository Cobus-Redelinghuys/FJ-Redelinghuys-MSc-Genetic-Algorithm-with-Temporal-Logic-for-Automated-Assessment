public class App {
    public static void main(String[] args) throws Exception {
        Chromosome chromosome = new Chromosome();
        for (int i = 0; i < Config.genes.length; i++) {
            System.out.println(Config.genes[i].toBinaryString(chromosome.genes[i]));
        }
        System.out.println(Config.reproductionProp);
    }
}
