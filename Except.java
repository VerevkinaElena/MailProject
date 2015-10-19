/**
 * Created by Елена on 15.10.2015.
 */
import java.io.IOException;
import java.lang.Exception;
public class Except {
    public static void main(String args[]) {

        System.out.println(test());
    }


    public static String test()
    {
        //while (true) {
            try {
                String q = "jkl";
                //return q;
                System.out.println("try");
              //  break;
                throw new IOException();
                // System.exit(0);

            } catch (IOException e) {
                System.out.println("catch");
                throw new Exception();
                //return "return";
            } finally {
                System.out.println("finally");
                return "return in finally";
            }
       // }
    }
}
