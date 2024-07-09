import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Interpreter {
    public final String interpreterPath;
    public final String interpreterCommand;
    public final String interpreterExecutorName;
    public final int numberInterpreterInstances;
    public final File[] interpreterInstancePaths;
    public final AtomicBoolean[] finished;

    Interpreter(String path, String command, int nInstances, String executor)
            throws RuntimeException {
        interpreterPath = path;
        interpreterCommand = command;
        numberInterpreterInstances = nInstances;
        interpreterExecutorName = executor;
        interpreterInstancePaths = new File[numberInterpreterInstances];
        finished = new AtomicBoolean[numberInterpreterInstances];
        File interpreterSourceFile = new File(interpreterPath);
        File parentInstanceDir = new File("./InterpreterInstances");
        if (parentInstanceDir.exists()) {
            try {
                Files
                        .walk(parentInstanceDir.toPath()) // Traverse the file tree in depth-first order
                        .sorted(Comparator.reverseOrder())
                        .forEach(parentPath -> {
                            try {
                                // System.out.println("Deleting: " + parentPath);
                                Files.delete(parentPath); // delete each file or directory
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parentInstanceDir.mkdir();
        for (int i = 0; i < interpreterInstancePaths.length; i++) {
            interpreterInstancePaths[i] = new File(parentInstanceDir.getPath(), "Instance_" + i);
            interpreterInstancePaths[i].mkdir();
            finished[i] = new AtomicBoolean(false);
            try {
                for (File file : interpreterSourceFile.listFiles()) {
                    Path source = file.toPath();
                    Path dest = Paths.get(interpreterInstancePaths[i].toPath().toString() + "/" + file.getName());
                    Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                    if (file.isDirectory()) {
                        recDirectories(source, dest);
                    }
                }
                finished[i].set(true);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        
    }

    private void recDirectories(Path parentSource, Path parentDir) throws Exception {
        for (File file : parentSource.toFile().listFiles()) {
            Path source = file.toPath();
            Path dest = Paths.get(parentDir.toString() + "/" + file.getName());
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            if (file.isDirectory()) {
                recDirectories(source, dest);
            }
        }
    }

    public HashMap<Chromosome, InterpreterResults[]> run(Chromosome[] chromosomes) {

        ArrayList<InterpreterInstance> interpreterInstancesQueue = new ArrayList<>();
        ArrayList<InterpreterInstance> finishedList = new ArrayList<>();
        //AtomicInteger count = new AtomicInteger(0);
        for (Chromosome chromosome : chromosomes) {
            interpreterInstancesQueue
                    .add(new InterpreterInstance(chromosome, interpreterCommand,
                            interpreterExecutorName));
        }
        InterpreterInstance[] instances = new InterpreterInstance[numberInterpreterInstances];
        while (!interpreterInstancesQueue.isEmpty()) {
            for (int i = 0; i < numberInterpreterInstances; i++) {
                if (instances[i] == null) {
                    if (interpreterInstancesQueue.size() > 0) {
                        instances[i] = interpreterInstancesQueue.remove(0);
                        instances[i].instanceNumber = i;
                        instances[i].start();
                    }
                } else if (instances[i].done.get()) {
                    try {
                        instances[i].join();
                        //System.out.println("Chromosome done: " + (count.getAndIncrement()+1) + "/" + chromosomes.length);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finishedList.add(instances[i]);
                    instances[i] = null;
                }
            }
        }

        HashMap<Chromosome, InterpreterResults[]> results = new HashMap<>();
        for (InterpreterInstance interpreterInstance : instances) {
            while (interpreterInstance != null && !interpreterInstance.done.get()) {

            }
            if (interpreterInstance != null) {
                finishedList.add(interpreterInstance);
                //System.out.println("Chromosome done: " + (count.getAndIncrement()+1) + "/" + chromosomes.length);
            }
        }

        for (InterpreterInstance interpreterInstance : finishedList) {
            JSONArray resultsArray = (JSONArray) interpreterInstance.result.get("results");
            InterpreterResults[] interpreterResultsArray = new InterpreterResults[resultsArray.size()];
            int i = 0;
            for (Object obj : resultsArray) {
                JSONObject jsonObject = (JSONObject) obj;
                interpreterResultsArray[i] = new InterpreterResults(jsonObject);
                i++;
            }
            results.put(interpreterInstance.chromosome, interpreterResultsArray);
        }

        return results;
    }

}

class InterpreterInstance extends Thread {
    final Chromosome chromosome;
    int instanceNumber;
    final String command;
    final String executor;
    JSONObject result;
    AtomicBoolean done = new AtomicBoolean(false);

    InterpreterInstance(Chromosome chromosome, String command, String executor) {
        this.chromosome = chromosome;
        this.command = command;
        this.executor = executor;
    }

    //@Override
    public void run() {
        File file = new File("InterpreterInstances/Instance_" + instanceNumber + "/Input.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(chromosome.toJSONString());
            fileWriter.close();
            String cmd = command + "InterpreterInstances/Instance_" + instanceNumber + "/" + executor; 
            String params = "InterpreterInstances/Instance_" + instanceNumber + "/";
            runProgram(cmd, params);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
            done.set(true);
            return;
        } finally {
            JSONParser jsonParser = new JSONParser();
            Object obj = null;
            try {
                obj = jsonParser
                        .parse(new FileReader("InterpreterInstances/Instance_" + instanceNumber + "/Output.json"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = (JSONObject) obj;
            /*for(Object objt : (JSONArray)result.get("results")){
                JSONObject jobjt = (JSONObject)objt;
                if(!((String)jobjt.get("instructorErrOut")).isEmpty()){
                    System.out.println(chromosome.geneString());
                    System.out.println((String)jobjt.get("instructorErrOut"));
                }
            }*/
            done.set(true);
        }
    }

    public static void runProgram(String str, String path) throws Exception {

        //System.out.println(str +" " +path);
        Process pro = Runtime.getRuntime().exec(str +" " +path);
        //System.out.println(str +" " +path);
        String error = "";
        // System.out.println(str + " stdout:" + pro.getInputStream());
        // System.out.println(str + " stderr:" + pro.getErrorStream());
        /*LocalDateTime start = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now();
        while(ChronoUnit.MILLIS.between(start, endTime) <= 120000){
            endTime = LocalDateTime.now();
            //System.out.println(ChronoUnit.MILLIS.between(start, endTime));
        }
        System.out.println(LocalDateTime.now());
        ChronoUnit.MILLIS.between(start, endTime);
        pro.destroy();*/

        int input = pro.getErrorStream().read();
        while (input != -1) {
            error += (char) input;
            input = pro.getErrorStream().read();
        }
        //System.out.println(error);
        input = pro.getInputStream().read();
        while (input != -1) {
            error += (char) input;
            input = pro.getInputStream().read();
        }
        //System.out.println(error);

        pro.waitFor();

        // System.out.println(str + " exitValue() " + pro.exitValue());
    }

}

class InterpreterResults {
    final String stdOut;
    final String errOut;
    final long exeTime;
    final int studentExitCode;

    InterpreterResults(JSONObject output) {
        stdOut = (String) output.get("stdOut");
        errOut = (String) output.get("errOut");
        exeTime = (Long) output.get("exeTime");
        studentExitCode = ((Long) output.get("exitCode")).intValue();
    }

    @SuppressWarnings("unchecked")
    JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("stdOut", stdOut);
        jsonObject.put("errOut", errOut);
        jsonObject.put("exeTime", exeTime);
        jsonObject.put("exitCode", studentExitCode);
        return jsonObject;
    }
}