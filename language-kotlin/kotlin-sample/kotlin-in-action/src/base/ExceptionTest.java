package base;

import java.io.BufferedReader;
import java.io.IOException;

public class ExceptionTest {


    int readNumber( BufferedReader reader) throws IOException {
        try {
            String line = reader.readLine(); // throws IOException
            return Integer.parseInt(line);   // throws NumberFormatException
        } catch (NumberFormatException e) {
            return -1;
        } finally {
            reader.close(); // throws IOException
        }

    }

    int readNumber2( BufferedReader reader) throws IOException {
        String line = reader.readLine(); // throws IOException
        //NumberFormatException 不是 checked exception 所以可以不处理，编译并不会报错
        return Integer.parseInt(line);
    }

    /*int readNumber3( BufferedReader reader) throws IOException {
        try (reader) {
            String line = reader.readLine(); // throws IOException
            return Integer.parseInt(line);   // throws NumberFormatException
        } catch (NumberFormatException e) {
            return -1;
        }
        // throws IOException

    }
*/


    public static void main(String[] args){
        ExceptionTestKt.readNumber(null);
    }
}
