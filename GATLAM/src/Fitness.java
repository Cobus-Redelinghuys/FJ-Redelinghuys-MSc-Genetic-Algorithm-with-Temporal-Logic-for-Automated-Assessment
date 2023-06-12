public class Fitness {
    public static float determineFitness(InterpretorResults[] output, int gen, Chromosome chromosome) {
        return LTL(output) + M(output) + ChromosomeDatabase.G(chromosome, gen);
    }

    private static float LTL(InterpretorResults[] output) {
        float result = 0;

        for (InterpretorResults interpretorResults : output) {
            float res = 0;
            res += Safety(interpretorResults);
            res += Livelyness(interpretorResults);
            res += SegFault(interpretorResults);
            res += Exception(interpretorResults);
            res += ExecutionTime(interpretorResults);
            res += IllegalOutput(interpretorResults);
            res += ExpectedOutput(interpretorResults);
            result += res;
        }
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

        result = FitnessConfig.Livelyness.weight * result;
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
        result = FitnessConfig.SegFault.weight * result;
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

        result = FitnessConfig.Exceptions.weight * result;
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
        result = FitnessConfig.IllegalOutput.weight * result
                / (FitnessConfig.IllegalOutput.words.length);
        return result;
    }

    private static float ExpectedOutput(InterpretorResults output) {
        if (!FitnessConfig.ExpectedOutput.enabled)
            return 0;

        float result = FitnessConfig.ExpectedOutput.constantExpected(output);

        result = FitnessConfig.ExpectedOutput.weight * result;
        return result;
    }
}
