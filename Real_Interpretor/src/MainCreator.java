import java.util.ArrayList;

public class MainCreator {
    static String content = null;
    static String createMain(ArrayList<Long> inputs){
        if(content == null){
            content = "";
            ArrayList<String> lines = buildMain(inputs);
            for (String str : lines) {
                content += str + "\n";
            }
        }
        return content;
    }

    static private ArrayList<String> buildMain(ArrayList<Long> inputs){
        ArrayList<String> lines = new ArrayList<>();
        lines.add("#include <iostream>");
        lines.add("int main(){");
        lines.add("std::cout << \"Hello World\" << std::endl;");
        lines.add("}");
        return lines;
    }

}
