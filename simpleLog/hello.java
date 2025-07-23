import java.util.Scanner;
import java.util.HashMap;
import java.io.*;
import java.io.BufferedWriter;
import java.io.BufferedReader;

public class hello {
    public static void main (String[] args) {

        HashMap<String, String> map = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Login = 1\n" + "register = 2\n" + "quit = 3\n");
        int logChoice = new scanner.next();
        

        try {
            //establish the writer, reader, scanner, and hashmap
            FileWriter writer = new FileWriter("userInfo.txt");
            BufferedWriter WriteFileBuffer = new BufferedWriter(writer);
            FileReader reader = new FileReader("userInfo.txt");
            BufferedReader ReadFileBuffer = new BufferedReader(reader);

            String tempLine;

            while ((tempLine = ReadFileBuffer.readLine()) != null) {
                map.put(tempLine, "newpass");
                if (map.containsKey(tempLine)) {
                    System.out.println("success");
                }
            }

            //use the writer
            System.out.println("Username?");
            String input = scanner.next();
            WriteFileBuffer.write(input + "\n");
            WriteFileBuffer.close();

            //catch exceptions
        } catch (IOException e) {
            e.printStackTrace();
        }

        //login

    }
}