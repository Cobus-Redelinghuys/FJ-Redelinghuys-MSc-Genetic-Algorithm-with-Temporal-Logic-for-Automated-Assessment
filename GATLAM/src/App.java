public class App {
    public static void main(String[] args) throws Exception {
        Chromosome[] pop = new Chromosome[10];
        for (int i = 0; i < pop.length; i++) {
            pop[i] = new Chromosome();
        }
        Config.interpretor.run(pop);
    }
}
