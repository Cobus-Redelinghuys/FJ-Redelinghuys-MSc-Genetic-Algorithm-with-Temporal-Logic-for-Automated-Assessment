public class App {
    public static void main(String[] args) throws Exception {
        Chromosome chromosomeA = new Chromosome();
        System.out.println(chromosomeA.bits);
        Chromosome mutation = Config.mutation.mutate(chromosomeA);
        System.out.println(mutation.bits);
    }
}
