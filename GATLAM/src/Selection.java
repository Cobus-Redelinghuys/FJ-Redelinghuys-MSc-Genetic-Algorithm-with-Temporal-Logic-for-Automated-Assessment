import java.util.HashSet;

abstract public class Selection {
    abstract Chromosome[] selectChromosomes(HashSet<Chromosome> population);
    abstract Chromosome[] invSelectionChromosomes(HashSet<Chromosome> population);
}
