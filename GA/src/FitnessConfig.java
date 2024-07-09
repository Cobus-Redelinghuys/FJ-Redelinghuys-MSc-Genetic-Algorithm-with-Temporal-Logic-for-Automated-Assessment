import java.io.FileReader;

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

    public static final int weightsOfActiveProperties;

    static {
        JSONParser jsonParser = new JSONParser();
        Object obj = null;
        int tempActive = 0;
        try {
            obj = ((JSONObject) jsonParser.parse(new FileReader("Config.json"))).get("FitnessFunction");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        Object res = null;
        try {
            res = new FitnessConfigField((JSONObject) ((JSONObject) jsonObject).get("Safety"));
        } catch (Exception e) {
            e.printStackTrace();
            res = new FitnessConfigField();
        } finally {
            Safety = (FitnessConfigField) res;
            if(Safety.enabled){
                tempActive += 1;
            }
        }

        try {
            res = new FitnessConfigField((JSONObject) ((JSONObject) jsonObject).get("Livelyness"));
        } catch (Exception e) {
            e.printStackTrace();
            res = new FitnessConfigField();
        } finally {
            Livelyness = (FitnessConfigField) res;
            if(Livelyness.enabled){
                tempActive += 1;
            }
        }

        try {
            res = new FitnessConfigField((JSONObject) ((JSONObject) jsonObject).get("SegFault"));
        } catch (Exception e) {
            e.printStackTrace();
            res = new FitnessConfigField();
        } finally {
            SegFault = (FitnessConfigField) res;
            if(SegFault.enabled){
                tempActive += 1;
            }
        }

        try {
            res = new FitnessConfigField((JSONObject) ((JSONObject) jsonObject).get("Exceptions"));
        } catch (Exception e) {
            e.printStackTrace();
            res = new FitnessConfigField();
        } finally {
            Exceptions = (FitnessConfigField) res;
            if(Exceptions.enabled){
                tempActive += 1;
            }
        }

        try {
            res = new ExecutionTimeField((JSONObject) ((JSONObject) jsonObject).get("ExecutionTime"));
        } catch (Exception e) {
            e.printStackTrace();
            res = new ExecutionTimeField();
        } finally {
            ExecutionTime = (ExecutionTimeField) res;
            if(ExecutionTime.enabled){
                tempActive += 1;
            }
        }

        try {
            res = new IllegalOutputField((JSONObject) jsonObject.get("IllegalOutput"));
        } catch (Exception e) {
            e.printStackTrace();
            res = new IllegalOutputField();
        } finally {
            IllegalOutput = (IllegalOutputField) res;
            if(IllegalOutput.enabled){
                tempActive += 1;
            }
        }

        try {
            res = new ExpectedOutputField((JSONObject) jsonObject.get("ExpectedOutput"));
        } catch (Exception e) {
            e.printStackTrace();
            res = new ExpectedOutputField();
        } finally {
            ExpectedOutput = (ExpectedOutputField) res;
            if(ExpectedOutput.enabled){
                tempActive+= 1;
            }
        }
        weightsOfActiveProperties = tempActive;
    }

}

class FitnessConfigField {
    public final boolean enabled;

    public FitnessConfigField(JSONObject jsonObject) throws RuntimeException {
        try {
            enabled = Boolean.parseBoolean(jsonObject.get("enabled").toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed safety property");
        }
    }

    public FitnessConfigField() {
        enabled = false;
    }
}

class IllegalOutputField {
    public final boolean enabled;
    public final String[] words;

    public IllegalOutputField(JSONObject jsonObject) throws RuntimeException {
        try {
            enabled = Boolean.parseBoolean(jsonObject.get("enabled").toString());
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
        words = new String[0];
    }
}

class ExecutionTimeField {
    public final boolean enabled;
    public final long maxTime;

    public ExecutionTimeField(JSONObject jsonObject) throws RuntimeException {
        try {
            enabled = Boolean.parseBoolean(jsonObject.get("enabled").toString());
            maxTime = Long.parseLong(jsonObject.get("maxTime").toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed fitness function");
        }
    }

    public ExecutionTimeField() {
        enabled = false;
        maxTime = 0;
    }

}

class ExpectedOutputField {
    public final boolean enabled;
    public final boolean exactMatch;
    public final String splitChar;

    public ExpectedOutputField(JSONObject jsonObject) throws RuntimeException {
        try {
            enabled = Boolean.parseBoolean(jsonObject.get("enabled").toString());
            exactMatch = Boolean.parseBoolean(jsonObject.get("exactMatch").toString());
            splitChar = jsonObject.get("splittingChar").toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Malformed safety property");
        }
    }

    public ExpectedOutputField() {
        enabled = false;
        exactMatch = false;
        splitChar = "\n";
    }

    public float constantExpected(InterpreterResults output) {
        /*int matched = 0;
        int possibles = 0;
        int penalty = 0;
        if (exactMatch) {
            if (output.studentStdOut.equals(output.instructorStdOut)) {
                return 0;
            }
            return 0;
        } else {
            ArrayList<String> studentLines = new ArrayList<>();
            Collections.addAll(studentLines, output.studentStdOut.split(splitChar));
            ArrayList<String> instructorLines = new ArrayList<>();
            Collections.addAll(instructorLines, output.instructorStdOut.split(splitChar));
            possibles = instructorLines.size();
            penalty = studentLines.size();
            while (!instructorLines.isEmpty() && !studentLines.isEmpty()) {
                String studentLine = studentLines.get(0);
                if (instructorLines.contains(studentLine)) {
                    matched++;
                }
                studentLines.remove(studentLine);
                instructorLines.remove(studentLine);
            }
        }

        float result = Math.abs(((float) matched - (float) penalty)) / (float) possibles;
        return result;*/
        return 0;
    }

}
