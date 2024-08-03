
public class main {
    public static void main(String[] args) {
        int v = Integer.parseInt(args[0]);
        int b = 5;
        int count = 0;
        while(v != b)
            count++;
            //System.out.println("Values dont match");
        if(v > b)
            v--;
        if(v < b)
            v++;

        System.out.println(count);
    }
}