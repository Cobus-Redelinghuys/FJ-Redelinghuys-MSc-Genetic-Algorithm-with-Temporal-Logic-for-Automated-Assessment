import java.util.ArrayList;

public class MainCreator {
    static String content = null;

    static String createMain(ArrayList<Long> inputs) {
        if (content == null) {
            content = "";
            ArrayList<String> lines = buildMain(inputs);
            for (String str : lines) {
                content += str + "\n";
            }
        }
        return content;
    }

    static private ArrayList<String> buildMain(ArrayList<Long> inputs) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("#include <iostream>");
        lines.add("#include \"TesterInterface.h\"");
        lines.add("#include \"IsDivisible.h\"");
        lines.add("#include \"IsSmaller.h\"");
        lines.add("#include \"IsGreater.h\"");
        lines.add("#include \"IsEvenOdd.h\"");
        lines.add("#include \"IsPrimeNumber.h\"");
        lines.add("int main(){");
        lines.add("TesterInterface* " + getTesterName() + " = new TesterInterface("
                + inputs.get(0) + ");");
        for(int i=0; i < inputs.get(0); i++){
            lines.add(Functions.AddTester.evaluate(getLatestTesterName(), inputs.get(i).toString()));
        }
        inputs.remove(0);
        lines.add("std::cout << \"Finished setup\" << std::endl;");
        for (int i = 0; i < inputs.size() - 1; i++) {
            Long function = inputs.get(i);
            Long parameter = inputs.get(i++);
            /*
             * lines.add(Functions.CopyConstructor.evaluate(getLatestTesterName(), ""));
             * lines.add("NumberTester* " +
             * TesterClasses.isPrimeNumber.create(value.toString(), getNumberTesterName()));
             * lines.add(Functions.SubScriptOperator.evaluate(getLatestTesterName(),
             * value.toString()));
             * lines.add(deleteTester(getLatestNumberTesterName(), numberTesterNames));
             */
            Functions func= selectFunction(function);
            lines.add(func.evaluate(getLatestTesterName(), parameter.toString()));
            if(func.usedBothParameters()){
                i++;
            }
        }
        for (int i = 0; i < testerNames.size(); i++) {
            lines.add(deleteTester(testerNames.get(0), testerNames));
        }
        for (int i = 0; i < numberTesterNames.size(); i++) {
            lines.add(deleteTester(numberTesterNames.get(0), numberTesterNames));
        }
        lines.add("return 0;");
        lines.add("}");
        return lines;
    }

    static private int currentTester = 1;
    static private int currentNumberTester = 1;
    static ArrayList<String> testerNames = new ArrayList<>();
    static ArrayList<String> numberTesterNames = new ArrayList<>();

    static public String getTesterName() {
        testerNames.add("t" + currentTester++);
        return getLatestTesterName();
    }

    static public String getLatestTesterName() {
        return testerNames.get(testerNames.size() - 1);
    }

    static public String getNumberTesterName() {
        numberTesterNames.add("n" + currentNumberTester++);
        return getLatestNumberTesterName();
    }

    static public String getLatestNumberTesterName() {
        return numberTesterNames.get(numberTesterNames.size() - 1);
    }

    static public String deleteTester(String tester, ArrayList<String> list) {
        String result = "if( " + tester + " != NULL)\n";
        result += "{\n";
        result += "delete " + tester + ";\n";
        result += tester + " = NULL;\n";
        result += "};";
        list.remove(tester);
        return result;
    }

    static String getNumberOfTest(String tester) {
        return "std::cout << Number of current tests: " + tester + "->getCurrNumTesters() << std::endl;\n";
    }

    public static Functions selectFunction(Long value) {
        return Functions.values()[Math.abs(value.intValue() % Functions.values().length)];
    }

}

interface FunctionInterface {
    String evaluate(String tester, String parameter);
    Boolean usedBothParameters();
}

enum Functions implements FunctionInterface {
    CopyConstructor {

        @Override
        public String evaluate(String tester, String parameter) {
            String result = "TesterInterface* " + MainCreator.getTesterName() + " = new TesterInterface(" + tester
                    + ");\n";
            result += MainCreator.deleteTester(tester, MainCreator.testerNames);
            return result;
        }

        @Override
        public Boolean usedBothParameters() {
            return false;
        }

    },
    AddTester {

        @Override
        public String evaluate(String tester, String parameter) {
            String result = NumberTesterObjects.evaluate(tester, parameter);
            if (((Long) Long.parseLong(parameter)).intValue() % TesterClasses.values().length >= 0) {
                result += "NumberTester* " + MainCreator.getNumberTesterName() + " = NULL;\n";
                result += TesterClasses.values()[Math
                        .abs(((Long) Long.parseLong(parameter)).intValue() % TesterClasses.values().length)]
                        .create(parameter, MainCreator.getLatestNumberTesterName()) + "\n";
                result += "std::cout << \"Add tester: \" << " + tester + "->addTester("
                        + MainCreator.getLatestNumberTesterName() + ") << std::endl;\n";
                result += MainCreator.deleteTester(MainCreator.getLatestNumberTesterName(),
                        MainCreator.numberTesterNames);
            } else {
                result += "std::cout << \"Add tester: \" << " + tester + "->addTester(NULL) << std::endl;\n";
            }
            result += NumberTesterObjects.evaluate(tester, parameter);
            return result;
        }

        @Override
        public Boolean usedBothParameters() {
            return true;
        }


    },
    RemoveTester {

        @Override
        public String evaluate(String tester, String parameter) {
            String result = NumberTesterObjects.evaluate(tester, parameter);
            result += "std::cout << \"Remove tester: \" << (" + tester + "->removeTester(" + parameter
                    + ")  ? \"True\" : \"False\") << std::endl;\n";
            result += NumberTesterObjects.evaluate(tester, parameter);
            return result;
        }

        @Override
        public Boolean usedBothParameters() {
            return true;
        }


    },
    EvaluateTester {

        @Override
        public String evaluate(String tester, String parameter) {
            return "std::cout << \"Evaluate tester: \" << (" + tester + "->evaluate(" + parameter
                    + ")  ? \"True\" : \"False\") << std::endl;";
        }

        @Override
        public Boolean usedBothParameters() {
            return true;
        }
    },
    FailedTests {

        @Override
        public String evaluate(String tester, String parameter) {
            String result = "std::cout << \"Failed tests: \"\n;";
            result += "for(int i=0; i < " + tester + "->numberOfFailedTests(" + parameter + "); i++)\n";
            result += "{\n";
            result += "std::cout << " + tester + "->failedTests(" + parameter + ")[i] << \"|\";";
            result += "}\n";
            result += "std::cout << std::endl;";
            return result;
        }

        @Override
        public Boolean usedBothParameters() {
            return true;
        }

    },
    NumberOfFailedTests {

        @Override
        public String evaluate(String tester, String parameter) {
            return "std::cout << \"Number of failed tests: \" << " + tester + "->numberOfFailedTests(" + parameter
                    + ") << std::endl;";
        }

        @Override
        public Boolean usedBothParameters() {
            return true;
        }

    },
    SubScriptOperator {

        @Override
        public String evaluate(String tester, String parameter) {
            String result = "if((*" + tester + ")[" + parameter + "] != NULL){\n";
            result += "std::cout << \"Result of test " + parameter + ": \" << (*" + tester + ")[" + parameter
                    + "] << std::endl;\n";
            result += "} else {\n";
            result += "std::cout << \"Result of test " + parameter + ": \" << \"NULL\" << std::endl;\n";
            result += "}";
            return result;
        }

        @Override
        public Boolean usedBothParameters() {
            return true;
        }

    },
    NumberTesterObjects {

        @Override
        public String evaluate(String tester, String parameter) {
            return "std::cout << \"NumberTester objects: \" << NumberTester::getNumAliveObjects() << std::endl;";
        }

        @Override
        public Boolean usedBothParameters() {
            return false;
        }

    },
    ValueDependantObjects {

        @Override
        public String evaluate(String tester, String parameter) {
            return "std::cout << \"ValueDependantTester objects: \" << ValueDependantTester::getNumAliveObjects() << std::endl;";
        }

        @Override
        public Boolean usedBothParameters() {
            return false;
        }
    },
    ValueIndependentObjects {

        @Override
        public String evaluate(String tester, String parameter) {
            return "std::cout << \"ValueIndependantTester objects: \" << ValueIndependantTester::getNumAliveObjects() << std::endl;";
        }

        @Override
        public Boolean usedBothParameters() {
            return false;
        }
    }
}

interface TesterInterface {
    String create(String parameter, String variable);
}

enum TesterClasses implements TesterInterface {
    isDivisible {

        @Override
        public String create(String parameter, String variable) {
            String result = "std::cout << \"IsDivisible objects: \" << IsDivisible::getNumAliveObjects() << std::endl;\n";
            result += variable + " = new IsDivisible(" + parameter + ");\n";
            result += "std::cout << \"IsDivisible objects: \" << IsDivisible::getNumAliveObjects() << std::endl;";
            return result;
        }

    },
    isGreater {

        @Override
        public String create(String parameter, String variable) {
            String result = "std::cout << \"IsGreater objects: \" << IsGreater::getNumAliveObjects() << std::endl;\n";
            result += variable + " = new IsGreater(" + parameter + ");";
            result += "std::cout << \"IsGreater objects: \" << IsGreater::getNumAliveObjects() << std::endl;";
            return result;
        }

    },
    isSmaller {

        @Override
        public String create(String parameter, String variable) {
            String result = "std::cout << \"IsSmaller objects: \" << IsSmaller::getNumAliveObjects() << std::endl;\n";
            result += variable + " = new IsSmaller(" + parameter + ");";
            result += "std::cout << \"IsSmaller objects: \" << IsSmaller::getNumAliveObjects() << std::endl;";
            return result;
        }

    },
    isEvenOdd {

        @Override
        public String create(String parameter, String variable) {
            String result = "std::cout << \"IsEvenOdd objects: \" << IsEvenOdd::getNumAliveObjects() << std::endl;\n";
            result += variable + " = new IsEvenOdd();";
            result += "std::cout << \"IsEvenOdd objects: \" << IsEvenOdd::getNumAliveObjects() << std::endl;";
            return result;
        }

    },
    isPrimeNumber {

        @Override
        public String create(String parameter, String variable) {
            String result = "std::cout << \"IsPrimeNumber objects: \" << IsPrimeNumber::getNumAliveObjects() << std::endl;\n";
            result += variable + " = new IsPrimeNumber();";
            result += "std::cout << \"IsPrimeNumber objects: \" << IsPrimeNumber::getNumAliveObjects() << std::endl;";
            return result;
        }

    }
}