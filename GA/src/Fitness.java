public class Fitness {
    public static float determineFitness(InterpreterResults[] output, int gen, Chromosome chromosome) {
        ChromosomeDBInfo chromosomeDBInfo = new ChromosomeDBInfo(chromosome, gen);
        ChromosomeDatabase.addDBInfo(chromosomeDBInfo);
        float ltl = LTL(output, chromosome, gen);
        chromosomeDBInfo = ChromosomeDatabase.get(chromosome, gen);
        chromosomeDBInfo.ltl = ltl;
        ChromosomeDatabase.addDBInfo(chromosomeDBInfo);
        float m = M(output);
        chromosomeDBInfo = ChromosomeDatabase.get(chromosome, gen);
        chromosomeDBInfo.m = m;
        ChromosomeDatabase.addDBInfo(chromosomeDBInfo);
        if (MContained(output)) {
            float g = ChromosomeDatabase.G(chromosome, gen);
            chromosomeDBInfo = ChromosomeDatabase.get(chromosome, gen);
            chromosomeDBInfo.g = g;
            ChromosomeDatabase.addDBInfo(chromosomeDBInfo);
        }
        chromosomeDBInfo = ChromosomeDatabase.get(chromosome, gen);
        float res = chromosomeDBInfo.ltl + chromosomeDBInfo.m + chromosomeDBInfo.g;
        if(res > 1){
            return 1;
        } else if(res < 0){
            return 0;
        } else if(Float.isNaN(res)){
            return 0;
        }
        return res;
    }

    private static float LTL(InterpreterResults[] output, Chromosome chromosome, int gen) {
        float result = 0;
        ChromosomeDBInfo chromosomeDBInfo = ChromosomeDatabase.get(chromosome, gen);

        float tSafety = 0;
        float tLivelyness = 0;
        float tSegFault = 0;
        float tException = 0;
        float tExecutionTime = 0;
        float tIllegalOutput = 0;
        float tExpectedOutput = 0;

        for (InterpreterResults interpreterResults : output) {
            float res = 0;
            float safety = Safety(interpreterResults);
            res += safety;
            float livelyness = Livelyness(interpreterResults);
            res += livelyness;
            float segFault = SegFault(interpreterResults);
            res += livelyness;
            float exceptions = Exception(interpreterResults);
            res += exceptions;
            float executionTime = ExecutionTime(interpreterResults);
            res += executionTime;
            float illegalOutput = IllegalOutput(interpreterResults);
            res += illegalOutput;
            float expectedOutput = ExpectedOutput(interpreterResults);
            res += expectedOutput;
            result += res / FitnessConfig.weightsOfActiveProperties;
            tSafety += safety;
            tLivelyness += livelyness;
            tSegFault += segFault;
            tException += exceptions;
            tExecutionTime += executionTime;
            tIllegalOutput += illegalOutput;
            tExpectedOutput += expectedOutput;

        }
        chromosomeDBInfo.Safety = tSafety;
        chromosomeDBInfo.Livelyness = tLivelyness;
        chromosomeDBInfo.SegFault = tSegFault;
        chromosomeDBInfo.Exceptions = tException;
        chromosomeDBInfo.ExecutionTime = tExecutionTime;
        chromosomeDBInfo.IllegalOutput = tIllegalOutput;
        chromosomeDBInfo.ExpectedOutput = tExpectedOutput;
        ChromosomeDatabase.addDBInfo(chromosomeDBInfo);
        float finalRes = (((float) Config.LTLWeight * result) / output.length);
        if(finalRes > Config.LTLWeight){
            finalRes = Config.LTLWeight;
        } else if (finalRes < 0) {
            finalRes = 0;
        } else if(Float.isNaN(finalRes)){
            finalRes = 0;
        }
        return finalRes;
    }

    private static float M(InterpreterResults[] output) {
        float result = 0;

        for (InterpreterResults interpreterResults : output) {
            if (Safety(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (Livelyness(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (SegFault(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (Exception(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (ExecutionTime(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (IllegalOutput(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (ExpectedOutput(interpreterResults) > 0) {
                result++;
                continue;
            }
        }
        float finalRes = (result / output.length) * Config.MWeight;
        if(finalRes > Config.MWeight){
            finalRes = Config.MWeight;
        } else if (finalRes < 0) {
            finalRes = 0;
        } else if(Float.isNaN(finalRes)){
            finalRes = 0;
        }
        return finalRes;
    }

    private static boolean MContained(InterpreterResults[] output) {
        float result = 0;

        for (InterpreterResults interpreterResults : output) {
            if (Safety(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (Livelyness(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (SegFault(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (Exception(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (ExecutionTime(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (IllegalOutput(interpreterResults) > 0) {
                result++;
                continue;
            }
            if (ExpectedOutput(interpreterResults) > 0) {
                result++;
                continue;
            }
        }
        return result > 0;
    }

    private static float Safety(InterpreterResults output) {
        if (!FitnessConfig.Safety.enabled) {
            return 0;
        }
        float result = 0;
        if (!(output.errOut.equals("") && output.errOut.isEmpty())) {
            result += 1;
        } else if (output.stdOut.toUpperCase().contains("EXCEPTION")) {
            result += 1;
        }

        return ((float) result);
    }

    private static float Livelyness(InterpreterResults output) {
        if (!FitnessConfig.Livelyness.enabled)
            return 0;

        float result = 0;
        if (output.studentExitCode != 0) {
            result += 1;
        }

        result = (float) (result);
        return result;
    }

    private static float SegFault(InterpreterResults output) {
        /*if (FitnessConfig.Safety.enabled)
            return 0;*/

        if (!FitnessConfig.SegFault.enabled)
            return 0;

        float result = 0;
        if (output.stdOut.toLowerCase().contains("segfault")
                || output.stdOut.toLowerCase().contains("segmentation fault")
                || output.exeTime == 139) {
            result += 1;
        }
        result = (float) (result);
        return result;
    }

    private static float Exception(InterpreterResults output) {
        /*if (FitnessConfig.Safety.enabled)
            return 0;*/

        if (!FitnessConfig.SegFault.enabled)
            return 0;

        float result = 0;

        if (output.stdOut.toLowerCase().contains("exception")
                || output.stdOut.toLowerCase().contains("exceptions")) {
            result += 1;
        } else if (output.stdOut.toLowerCase().contains("exception")
                || output.stdOut.toLowerCase().contains("exceptions")) {
            result += 1;
        }

        result = (float) (result);
        return result;
    }

    private static float ExecutionTime(InterpreterResults output) {
        if (!FitnessConfig.ExecutionTime.enabled)
            return 0;

        float result = 0;

        if (output.exeTime > FitnessConfig.ExecutionTime.maxTime) {
            result += 1;
        }
        result = (float)result;
        return result;
    }

    private static float IllegalOutput(InterpreterResults output) {
        if (!FitnessConfig.IllegalOutput.enabled)
            return 0;

        if (FitnessConfig.IllegalOutput.words.length <= 0)
            return 0;

        float result = 0;

        for (String word : FitnessConfig.IllegalOutput.words) {
            if (output.stdOut.contains(word)) {
                result += 1;
            }
        }
        result = (float) (result
                / (FitnessConfig.IllegalOutput.words.length));
        return result;
    }

    private static float ExpectedOutput(InterpreterResults output) {
        if (!FitnessConfig.ExpectedOutput.enabled)
            return 0;

        float result = FitnessConfig.ExpectedOutput.constantExpected(output);
        // float result = FitnessConfig.ExpectedOutput.

        result = (float) (result);
        return result;
    }
}
