import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class Interpretor {
    public final String interpreterPath;
    public final String interpreterCommand;
    public final String interpreterOutputPath;
    public final String interpretorExecutorName;
    public final int numberInterpreterInstances;
    public final File[] interpreterInstacePaths;

    Interpretor(String path, String command, String outputPath, int nInstances, String executor)
            throws RuntimeException {
        interpreterPath = path;
        interpreterCommand = command;
        interpreterOutputPath = outputPath;
        numberInterpreterInstances = nInstances;
        interpretorExecutorName = executor;
        interpreterInstacePaths = new File[numberInterpreterInstances];
        File interpretorSourceFile = new File(interpreterPath);
        File parentInstanceDir = new File("./InterpretorInstances");
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
        for (int i = 0; i < interpreterInstacePaths.length; i++) {
            interpreterInstacePaths[i] = new File(parentInstanceDir.getPath(), "Instance_" + i);
            interpreterInstacePaths[i].mkdir();
            try {
                for (File file : interpretorSourceFile.listFiles()) {
                    Path source = file.toPath();
                    Path dest = Paths.get(interpreterInstacePaths[i].toPath().toString() + "/" + file.getName());
                    Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                    if (file.isDirectory()) {
                        recDirectories(source, dest);
                    }
                }
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

    public HashMap<Chromosome, JSONObject> run(Chromosome[] chromosomes) {

        ArrayList<InterpretorInstance> interpreterInstancesQueue = new ArrayList<>();
        ArrayList<InterpretorInstance> finishedList = new ArrayList<>();
        for (Chromosome chromosome : chromosomes) {
            interpreterInstancesQueue
                    .add(new InterpretorInstance(chromosome, interpreterCommand, interpreterOutputPath,
                            interpretorExecutorName));
        }
        InterpretorInstance[] instances = new InterpretorInstance[numberInterpreterInstances];
        while (!interpreterInstancesQueue.isEmpty()) {
            for (int i = 0; i < numberInterpreterInstances; i++) {
                if (instances[i] == null) {
                    if (interpreterInstancesQueue.size() > 0) {
                        instances[i] = interpreterInstancesQueue.remove(0);
                        instances[i].instanceNumber = i;
                        instances[i].start();
                    }
                } else if (instances[i].done) {
                    try {
                        instances[i].join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finishedList.add(instances[i]);
                    instances[i] = null;
                }
            }
        }

        HashMap<Chromosome, JSONObject> results = new HashMap<>();

        for (InterpretorInstance interpretorInstance : finishedList) {
            results.put(interpretorInstance.chromosome, interpretorInstance.result);
        }

        return results;
    }

}

class InterpretorInstance extends Thread {
    final Chromosome chromosome;
    int instanceNumber;
    final String command;
    final String outputPath;
    final String executor;
    JSONObject result;
    boolean done;

    InterpretorInstance(Chromosome chromosome, String command, String outputPath, String executor) {
        this.chromosome = chromosome;
        this.command = command;
        this.outputPath = outputPath;
        this.executor = executor;
    }

    @Override
    public void run() {
        File file = new File("Interpretorinstances/Instance_" + instanceNumber + "/input.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(chromosome.toJSONString());
            fileWriter.close();
            runProgram(command + "Interpretorinstances/Instance_" + instanceNumber + "/" + executor);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
            done = true;
            return;
        } finally {

        }
        done = true;
    }

    public static void runProgram(String str) throws Exception {

        Process pro = Runtime.getRuntime().exec(str);
        String error = "";
        // System.out.println(str + " stdout:" + pro.getInputStream());
        // System.out.println(str + " stderr:" + pro.getErrorStream());
        int input = pro.getErrorStream().read();
        while (input != -1) {
            error += (char) input;
            input = pro.getErrorStream().read();
        }
        System.out.println(error);
        input = pro.getInputStream().read();
        while (input != -1) {
            error += (char) input;
            input = pro.getInputStream().read();
        }
        System.out.println(error);

        pro.waitFor();

        // System.out.println(str + " exitValue() " + pro.exitValue());
    }

}