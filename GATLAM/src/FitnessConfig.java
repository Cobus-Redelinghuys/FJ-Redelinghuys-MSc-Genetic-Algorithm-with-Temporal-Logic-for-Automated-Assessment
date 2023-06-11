import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FitnessConfig {
    public static final FitnessConfigField Safety;
    public static final FitnessConfigField Livelyness;
    public static final FitnessConfigField SegFault;
    public static final FitnessConfigField Exceptions;
    public static final ExecutionTimeField ExecutionTime;
    public static final IllegalOutputField IllegalOutput;
    public static final ExpectedOutputField ExpectedOutput;

    static {
        JSONParser jsonParser = new JSONParser();
        Object obj = null;
        try {
            obj = ((JSONObject) jsonParser.parse(new FileReader("Config.json"))).get("FitnessFunction");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        Object res = null;
        try {
            res = new FitnessConfigField((JSONObject) jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            res = new FitnessConfigField();
        } finally {
            Safety = (FitnessConfigField) res;
        }

        try {
            res = new FitnessConfigField((JSONObject) jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            res = new FitnessConfigField();
        } finally {
            Livelyness = (FitnessConfigField) res;
        }

        try {
            res = new FitnessConfigField((JSONObject) jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            res = new FitnessConfigField();
        } finally {
            SegFault = (FitnessConfigField) res;
        }

        try {
            res = new FitnessConfigField((JSONObject) jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            res = new FitnessConfigField();
        } finally {
            Exceptions = (FitnessConfigField) res;
        }

        try {
            res = new ExecutionTimeField((JSONObject) jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            res = new ExecutionTimeField();
        } finally {
            ExecutionTime = (ExecutionTimeField) res;
        }

        try {
            res = new IllegalOutputField((JSONObject) jsonObject.get("IllegalOutput"));
        } catch (Exception e) {
            e.printStackTrace();
            res = new IllegalOutputField();
        } finally {
            IllegalOutput = (IllegalOutputField) res;
        }

        try {
            res = new ExpectedOutputField((JSONObject) jsonObject.get("ExpectedOutput"));
        } catch (Exception e) {
            e.printStackTrace();
            res = new ExpectedOutputField();
        } finally {
            ExpectedOutput = (ExpectedOutputField) res;
        }
    }

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
        if (!Safety.enabled) {
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
        result = Safety.weight * result / (float) output.length;
        return result;
    }

    private static float Livelyness(InterpretorResults[] output) {
        if (!Livelyness.enabled)
            return 0;

        float result = 0;
        for (InterpretorResults interpretorResults : output) {
            if (interpretorResults.studentExitCode != 0) {
                result += 1;
            }
        }
        result = Livelyness.weight * result / output.length;
        return result;
    }

    private static float SegFault(InterpretorResults[] output) {
        if (Safety.enabled)
            return 0;

        if (!SegFault.enabled)
            return 0;

        float result = 0;
        for (InterpretorResults interpretorResults : output) {
            if (interpretorResults.studentStdOut.toLowerCase().contains("segfault")
                    || interpretorResults.studentStdOut.toLowerCase().contains("segmentation fault")
                    || interpretorResults.studentExeTime == 139) {
                result += 1;
            }
        }
        result = SegFault.weight * result / output.length;
        return result;
    }

    private static float Exception(InterpretorResults[] output) {
        if (Safety.enabled)
            return 0;

        if (!SegFault.enabled)
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
        result = Exceptions.weight * result / output.length;
        return result;
    }

    private static float ExecutionTime(InterpretorResults[] output) {
        if (!ExecutionTime.enabled)
            return 0;

        float result = 0;

        for (InterpretorResults interpretorResults : output) {
            if (interpretorResults.studentExitCode > ExecutionTime.maxTime) {
                result += 1;
            }
        }
        result = (float) ExecutionTime.weight * result / output.length;
        return result;
    }

    private static float IllegalOutput(InterpretorResults[] output) {
        if (!IllegalOutput.enabled)
            return 0;

        if (IllegalOutput.words.length <= 0)
            return 0;

        float result = 0;

        for (InterpretorResults interpretorResults : output) {
            for (String word : IllegalOutput.words) {
                if (interpretorResults.studentStdOut.contains(word)) {
                    result += 1;
                }
            }
        }
        result = IllegalOutput.weight * result / (IllegalOutput.words.length * output.length);
        return result;
    }

    private static float ExpectedOutput(InterpretorResults[] output) {
        if (!ExpectedOutput.enabled)
            return 0;

        float result = ExpectedOutput.constantExpected(output);

        result = ExpectedOutput.weight * result / output.length;
        return result;
    }

}

class FitnessConfigField {
    public final boolean enabled;
    public final float weight;

    public FitnessConfigField(JSONObject jsonObject) throws RuntimeException {
        try {
            enabled = (Boolean) jsonObject.get("enabled");
            weight = ((Long) jsonObject.get("weight")).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed safety property");
        }
    }

    public FitnessConfigField() {
        enabled = false;
        weight = Float.NaN;
    }
}

class IllegalOutputField {
    public final boolean enabled;
    public final float weight;
    public final String[] words;

    public IllegalOutputField(JSONObject jsonObject) throws RuntimeException {
        try {
            enabled = (Boolean) jsonObject.get("enabled");
            weight = ((Long) jsonObject.get("weight")).floatValue();
            JSONArray arr = (JSONArray) jsonObject.get("words");
            String[] res = new String[arr.size()];
            for (int i = 0; i < res.length; i++) {
                res[i] = (String) arr.get(i);
            }
            words = res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed safety property");
        }
    }

    public IllegalOutputField() {
        enabled = false;
        weight = Float.NaN;
        words = new String[0];
    }
}

class ExecutionTimeField {
    public final boolean enabled;
    public final float weight;
    public final long maxTime;

    public ExecutionTimeField(JSONObject jsonObject) throws RuntimeException {
        try {
            enabled = (Boolean) jsonObject.get("enabled");
            weight = ((Long) jsonObject.get("weight")).floatValue();
            maxTime = (Long) jsonObject.get("maxTime");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed fitness function");
        }
    }

    public ExecutionTimeField() {
        enabled = false;
        weight = Float.NaN;
        maxTime = 0;
    }

}

class ExpectedOutputField {
    public final boolean enabled;
    public final float weight;
    public final boolean exactMatch;
    public final String splitChar;

    public ExpectedOutputField(JSONObject jsonObject) throws RuntimeException {
        try {
            enabled = (Boolean) jsonObject.get("enabled");
            weight = ((Long) jsonObject.get("weight")).floatValue();
            exactMatch = (Boolean) jsonObject.get("exactMatch");
            splitChar = (String)jsonObject.get("splittingChar");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed safety property");
        }
    }

    public ExpectedOutputField() {
        enabled = false;
        weight = 0;
        exactMatch = false;
        splitChar = "\n";
    }

    public float constantExpected(InterpretorResults[] output) {
        int matched = 0;
        int possibles = 0;
        int penalty=0;
        for (InterpretorResults interpretorResults : output) {
            if (exactMatch) {
                if (interpretorResults.studentStdOut.equals(interpretorResults.instructorStdOut)) {
                    matched++;
                }
                possibles++;
            } else {
                ArrayList<String> studentLines = new ArrayList<>();
                Collections.addAll(studentLines, interpretorResults.studentStdOut.split(splitChar));
                ArrayList<String> instructorLines = new ArrayList<>();
                Collections.addAll(instructorLines, interpretorResults.instructorStdOut.split(splitChar));
                while(!instructorLines.isEmpty() && !studentLines.isEmpty()){
                    String studentLine = studentLines.get(0);
                    if(instructorLines.contains(studentLine)){
                        matched++;
                    }
                    studentLines.remove(studentLine);
                    instructorLines.remove(studentLine);
                }
                possibles = instructorLines.size();
                penalty = studentLines.size();
            }
        }
        float result = ((float) matched - (float) penalty) / (float) possibles;
        return result;
    }

}
