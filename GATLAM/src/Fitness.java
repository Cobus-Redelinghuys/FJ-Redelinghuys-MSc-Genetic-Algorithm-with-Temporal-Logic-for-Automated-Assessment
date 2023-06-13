public class Fitness {
    public static float determineFitness(InterpretorResults[] output, int gen, Chromosome chromosome) {
        ChromosomeDBInfo chromosomeDBInfo = new ChromosomeDBInfo(chromosome, gen);
        ChromosomeDatabase.addDBInfo(chromosomeDBInfo);
        chromosomeDBInfo.ltl = LTL(output, chromosome, gen);
        chromosomeDBInfo.m = M(output);
        chromosomeDBInfo.g = ChromosomeDatabase.G(chromosome, gen);

        chromosomeDBInfo = ChromosomeDatabase.get(chromosome, gen);
        return chromosomeDBInfo.ltl + chromosomeDBInfo.m + chromosomeDBInfo.g;
    }

    private static float LTL(InterpretorResults[] output, Chromosome chromosome, int gen) {
        float result = 0;
        ChromosomeDBInfo chromosomeDBInfo = ChromosomeDatabase.get(chromosome, gen);

        for (InterpretorResults interpretorResults : output) {
            float res = 0;
            chromosomeDBInfo.Safety += Safety(interpretorResults);
            res += chromosomeDBInfo.Safety;
            chromosomeDBInfo.Livelyness += Livelyness(interpretorResults);
            res += chromosomeDBInfo.Livelyness;
            chromosomeDBInfo.SegFault += SegFault(interpretorResults);
            res += chromosomeDBInfo.SegFault;
            chromosomeDBInfo.Exceptions += Exception(interpretorResults);
            res += chromosomeDBInfo.Exceptions;
            chromosomeDBInfo.ExecutionTime += ExecutionTime(interpretorResults);
            res += chromosomeDBInfo.ExecutionTime;
            chromosomeDBInfo.IllegalOutput += IllegalOutput(interpretorResults);
            res += chromosomeDBInfo.IllegalOutput;
            chromosomeDBInfo.ExpectedOutput +=ExpectedOutput(interpretorResults);
            res += chromosomeDBInfo.ExpectedOutput;
            result += res;
        }

        ChromosomeDatabase.addDBInfo(chromosomeDBInfo);
        return (float) Config.LTLWeight * result;
    }

    private static float M(InterpretorResults[] output){
        float result = 0;

        for (InterpretorResults interpretorResults : output) {
            if(Safety(interpretorResults) > 0){
                result++;
                continue;
            }
            if(Livelyness(interpretorResults) > 0){
                result++;
                continue;
            }
            if(SegFault(interpretorResults) > 0){
                result++;
                continue;
            }
            if(Exception(interpretorResults) > 0){
                result++;
                continue;
            }
            if(ExecutionTime(interpretorResults) > 0){
                result++;
                continue;
            }
            if(IllegalOutput(interpretorResults) > 0){
                result++;
                continue;
            }
            if(ExpectedOutput(interpretorResults) > 0){
                result++;
                continue;
            }
        }
        return result * Config.MWeight;
    }



    private static float Safety(InterpretorResults output) {
        if (!FitnessConfig.Safety.enabled) {
            return 0;
        }
        float result = 0;
        if (!(output.studentErrOut.equals("") && output.studentErrOut.isEmpty())) {
            result += 1;
        } else if (output.studentStdOut.toUpperCase().contains("EXCEPTION")) {
            result += 1;
        }

        return result;
    }

    private static float Livelyness(InterpretorResults output) {
        if (!FitnessConfig.Livelyness.enabled)
            return 0;

        float result = 0;
        if (output.studentExitCode != 0) {
            result += 1;
        }

        result = (float) (FitnessConfig.Livelyness.weight * result);
        return result;
    }

    private static float SegFault(InterpretorResults output) {
        if (FitnessConfig.Safety.enabled)
            return 0;

        if (!FitnessConfig.SegFault.enabled)
            return 0;

        float result = 0;
        if (output.studentStdOut.toLowerCase().contains("segfault")
                || output.studentStdOut.toLowerCase().contains("segmentation fault")
                || output.studentExeTime == 139) {
            result += 1;
        }
        result = (float) (FitnessConfig.SegFault.weight * result);
        return result;
    }

    private static float Exception(InterpretorResults output) {
        if (FitnessConfig.Safety.enabled)
            return 0;

        if (!FitnessConfig.SegFault.enabled)
            return 0;

        float result = 0;

        if (output.studentStdOut.toLowerCase().contains("exception")
                || output.studentStdOut.toLowerCase().contains("exceptions")) {
            result += 1;
        } else if (output.studentStdOut.toLowerCase().contains("exception")
                || output.studentStdOut.toLowerCase().contains("exceptions")) {
            result += 1;
        }

        result = (float) (FitnessConfig.Exceptions.weight * result);
        return result;
    }

    private static float ExecutionTime(InterpretorResults output) {
        if (!FitnessConfig.ExecutionTime.enabled)
            return 0;

        float result = 0;

        if (output.studentExitCode > FitnessConfig.ExecutionTime.maxTime) {
            result += 1;
        }
        result = (float) FitnessConfig.ExecutionTime.weight * result;
        return result;
    }

    private static float IllegalOutput(InterpretorResults output) {
        if (!FitnessConfig.IllegalOutput.enabled)
            return 0;

        if (FitnessConfig.IllegalOutput.words.length <= 0)
            return 0;

        float result = 0;

        for (String word : FitnessConfig.IllegalOutput.words) {
            if (output.studentStdOut.contains(word)) {
                result += 1;
            }
        }
        result = (float) (FitnessConfig.IllegalOutput.weight * result
                / (FitnessConfig.IllegalOutput.words.length));
        return result;
    }

    private static float ExpectedOutput(InterpretorResults output) {
        if (!FitnessConfig.ExpectedOutput.enabled)
            return 0;

        float result = FitnessConfig.ExpectedOutput.constantExpected(output);

        result = (float) (FitnessConfig.ExpectedOutput.weight * result);
        return result;
    }
}
