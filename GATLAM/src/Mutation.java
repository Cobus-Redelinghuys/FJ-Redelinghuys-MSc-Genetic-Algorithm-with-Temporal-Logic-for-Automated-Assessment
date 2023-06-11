abstract public class Mutation {
    abstract Chromosome mutate(Chromosome chromosome);

    static Mutation getMutation(String type) throws RuntimeException{
        if(type.equals("SingleBitInversion")){
            return new SingleBitInversion();
        }
        throw new RuntimeException("Invalid CrossOverType");
    }
}

class SingleBitInversion extends Mutation{
    @Override
    Chromosome mutate(Chromosome chromosome) {
        String bits = chromosome.bits;
        int point = Config.random.nextInt(bits.length()-2)+1;
        String a = bits.substring(0, point);
        String b = bits.substring(point+1);
        char bit = bits.charAt(point);
        if(bit == '1'){
            bit = '0';
        } else {
            bit = '1';
        }
        String res = a + bit + b;
        return new Chromosome(res);
    }
}