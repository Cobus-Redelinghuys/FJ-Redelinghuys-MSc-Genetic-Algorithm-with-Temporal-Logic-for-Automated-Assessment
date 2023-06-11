import java.util.HashSet;

abstract public class Selection {
    abstract SelectionResult selectChromosomes(HashSet<Chromosome> population);
}

class SelectionResult{
    final Chromosome[] winners;
    final Chromosome[] loosers;

    SelectionResult(Chromosome[] w, Chromosome[] l){
        winners = w;
        loosers = l;
    }
}
