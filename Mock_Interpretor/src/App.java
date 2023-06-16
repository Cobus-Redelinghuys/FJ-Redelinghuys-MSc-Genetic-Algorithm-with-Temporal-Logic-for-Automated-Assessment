import java.io.FileWriter;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class App {
    public static void main(String[] args) throws Exception {
        args = new String[]{"./"};
        ModulesConfig.set(args[0]);
        Input input = null;
        try {
            input = new Input(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JSONArray instructorResult = instructor(input);
        JSONArray studentResult = student(input);
        JSONObject result = new JSONObject();
        JSONArray moduleResults = new JSONArray();
        HashMap<String, JSONObject> studentMap = new HashMap<>();
        HashMap<String, JSONObject> instructionMap = new HashMap<>();
        for (Object object : studentResult) {
            JSONObject jsonObject = (JSONObject)object;
            studentMap.put((String)jsonObject.get("moduleName"), jsonObject);
        }
        for (Object object : instructorResult) {
            JSONObject jsonObject = (JSONObject)object;
            instructionMap.put((String)jsonObject.get("moduleName"), jsonObject);
        }

        for (String moduleName : studentMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentStdOut", studentMap.get(moduleName).get("stdout"));
            jsonObject.put("studentErrOut", studentMap.get(moduleName).get("stderr"));
            jsonObject.put("studentExeTime", studentMap.get(moduleName).get("duration"));
            jsonObject.put("studentExitCode", studentMap.get(moduleName).get("exitvalue"));
            jsonObject.put("instructorStdOut", instructionMap.get(moduleName).get("stdout"));
            jsonObject.put("instructorErrOut", instructionMap.get(moduleName).get("stderr"));
            jsonObject.put("instructorExeTime", instructionMap.get(moduleName).get("duration"));
            jsonObject.put("instructorExitCode", instructionMap.get(moduleName).get("exitvalue"));
            moduleResults.add(jsonObject);
        }
        result.put("results", moduleResults);

        try (FileWriter file = new FileWriter("Output.json");) {
            file.write(result.toJSONString());
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static JSONArray instructor(Input input) {
        JSONArray result = ModulesConfig.executeSystem(input, "./Instructor_Solution");

        try (FileWriter file = new FileWriter("Instructor_Output.json");) {
            file.write(result.toJSONString());
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("InstructorSolution");
        for (int i = 0; i < result.size(); i++) {
            JSONObject res = (JSONObject) result.get(i);
            System.out.println("Module: " + i);
            if (((String) res.get("stdout")).contains("\n"))
                System.out.print("stdout: " + res.get("stdout"));
            else
                System.out.println("stdout: " + res.get("stdout"));
            if (((String) res.get("stderr")).contains("\n"))
                System.out.print("stderr: " + res.get("stderr"));
            else
                System.out.println("stderr: " + res.get("stderr"));
            System.out.println("duration: " + res.get("duration"));
            System.out.println("exit value: " + res.get("exitvalue"));
        }
        return result;
    }

    static JSONArray student(Input input) {
        JSONArray result = ModulesConfig.executeSystem(input, "./Student_Solution");

        try (FileWriter file = new FileWriter("Student_Output.json");) {
            file.write(result.toJSONString());
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("StudentSolution");
        for (int i = 0; i < result.size(); i++) {
            JSONObject res = (JSONObject) result.get(i);
            System.out.println("Module: " + i);
            if (((String) res.get("stdout")).contains("\n"))
                System.out.print("stdout: " + res.get("stdout"));
            else
                System.out.println("stdout: " + res.get("stdout"));
            if (((String) res.get("stderr")).contains("\n"))
                System.out.print("stderr: " + res.get("stderr"));
            else
                System.out.println("stderr: " + res.get("stderr"));
            System.out.println("duration: " + res.get("duration"));
            System.out.println("exit value: " + res.get("exitvalue"));
        }
        return result;
    }
}
