public class Fitness {
    public static float determineFitness(InterpretorResults[] output) {
        float res = 0;
        res += Safety(output);
        res += Livelyness(output);
        res += SegFault(output);
        res += Exception(output);
        res += ExecutionTime(output);
        res += IllegalOutput(output);
        res += ExpectedOutput(output);
        res = (float) Config.LTLWeight * res;
        return res;
    }

    private static float Safety(InterpretorResults[] output) {
        if (!FitnessConfig.Safety.enabled) {
            return 0;
        }
        float result = 0;
        for (InterpretorResults interpretorResults : output) {
            if (!(interpretorResults.studentErrOut.equals("") && interpretorResults.studentErrOut.isEmpty())) {
                result += 1;
            } else if (interpretorResults.studentStdOut.toUpperCase().contains("EXCEPTION")) {
                result += 1;
            }

        }
        result = FitnessConfig.Safety.weight * result / (float) output.length;
        return result;
    }

    private static float Livelyness(InterpretorResults[] output) {
        if (!FitnessConfig.Livelyness.enabled)
            return 0;

        float result = 0;
        for (InterpretorResults interpretorResults : output) {
            if (interpretorResults.studentExitCode != 0) {
                result += 1;
            }
        }
        result = FitnessConfig.Livelyness.weight * result / output.length;
        return result;
    }

    private static float SegFault(InterpretorResults[] output) {
        if (FitnessConfig.Safety.enabled)
            return 0;

        if (!FitnessConfig.SegFault.enabled)
            return 0;

        float result = 0;
        for (InterpretorResults interpretorResults : output) {
            if (interpretorResults.studentStdOut.toLowerCase().contains("segfault")
                    || interpretorResults.studentStdOut.toLowerCase().contains("segmentation fault")
                    || interpretorResults.studentExeTime == 139) {
                result += 1;
            }
        }
        result = FitnessConfig.SegFault.weight * result / output.length;
        return result;
    }

    private static float Exception(InterpretorResults[] output) {
        if (FitnessConfig.Safety.enabled)
            return 0;

        if (!FitnessConfig.SegFault.enabled)
            return 0;

        float result = 0;

        for (InterpretorResults interpretorResults : output) {
            if (interpretorResults.studentStdOut.toLowerCase().contains("exception")
                    || interpretorResults.studentStdOut.toLowerCase().contains("exceptions")) {
                result += 1;
            } else if (interpretorResults.studentStdOut.toLowerCase().contains("exception")
                    || interpretorResults.studentStdOut.toLowerCase().contains("exceptions")) {
                result += 1;
            }
        }
        result = FitnessConfig.Exceptions.weight * result / output.length;
        return result;
    }

    private static float ExecutionTime(InterpretorResults[] output) {
        if (!FitnessConfig.ExecutionTime.enabled)
            return 0;

        float result = 0;

        for (InterpretorResults interpretorResults : output) {
            if (interpretorResults.studentExitCode > FitnessConfig.ExecutionTime.maxTime) {
                result += 1;
            }
        }
        result = (float) FitnessConfig.ExecutionTime.weight * result / output.length;
        return result;
    }

    private static float IllegalOutput(InterpretorResults[] output) {
        if (!FitnessConfig.IllegalOutput.enabled)
            return 0;

        if (FitnessConfig.IllegalOutput.words.length <= 0)
            return 0;

        float result = 0;

        for (InterpretorResults interpretorResults : output) {
            for (String word : FitnessConfig.IllegalOutput.words) {
                if (interpretorResults.studentStdOut.contains(word)) {
                    result += 1;
                }
            }
        }
        result = FitnessConfig.IllegalOutput.weight * result / (FitnessConfig.IllegalOutput.words.length * output.length);
        return result;
    }

    private static float ExpectedOutput(InterpretorResults[] output) {
        if (!FitnessConfig.ExpectedOutput.enabled)
            return 0;

        float result = FitnessConfig.ExpectedOutput.constantExpected(output);

        result = FitnessConfig.ExpectedOutput.weight * result / output.length;
        return result;
    }
}
