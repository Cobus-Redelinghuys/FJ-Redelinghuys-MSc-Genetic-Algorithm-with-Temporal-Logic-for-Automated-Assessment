public class App {
    public static void main(String[] args) throws Exception {
        while(true){
            int count = 0;
            for (int i = 0; i < Config.interpreter.finished.length; i++) {
                if(Config.interpreter.finished[i] != null && Config.interpreter.finished[i].get() == true){
                    count++;
                }
            }
            if(count == Config.interpreter.interpreterInstancePaths.length){
                break;
            }
        }
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.run();
        ga.printSummaryToFile("results.json");
        ChromosomeDatabase.printToFile("database.json");
        InterpreterInstance.runProgram("python3 visualizer.py", "");
    }
}
