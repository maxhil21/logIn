import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class hello {

    // Main method
    public static void main(String[] args) {
        Map<String, String> users = new HashMap<>();

        try (Scanner scanner = new Scanner(System.in);
             BufferedWriter writeFileBuffer = new BufferedWriter(new FileWriter("userInfo.txt", true));
             BufferedReader readFileBuffer = new BufferedReader(new FileReader("userInfo.txt"))) {

            // Load existing users
            String temp;
            while ((temp = readFileBuffer.readLine()) != null) {
                String[] parts = temp.trim().split(":", 2);
                if (parts.length == 2) {
                    users.put(parts[0].toLowerCase(), parts[1]);
                }
            }

            System.out.println("""
                    Login = 1
                    register = 2
                    quit = 3
                    """);
            int logChoice = scanner.nextInt();
            
            if (logChoice == 1) {
                System.out.println("enter username");
                String username = scanner.next().toLowerCase();
                String currentUser = username;
                System.out.println("enter passcode");
                String passcode = scanner.next();
                
                if (users.containsKey(username) && users.get(username).equals(passcode)) {
                    System.out.println("Login successful!");
                    userLogged(scanner, users, writeFileBuffer, currentUser);
                } else {
                    System.out.println("Incorrect username or password");
                }
                
            } else if (logChoice == 2) {
                registerUser(users, scanner, writeFileBuffer);
            } else if (logChoice == 3) {
                System.exit(0);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Creating new user database");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Delete user method
    private static void deleteUser(Scanner scanner, Map<String, String> users, String currentUser) throws IOException {
        System.out.println("enter your password");
        String password = scanner.next();
        if ((users.get(currentUser).equals(password))) {
            System.out.println("what user should I delete?");
            String deleteUser = scanner.next().trim().toLowerCase();
            if (!users.containsKey(deleteUser)) {
                System.out.println("User does not exist");
            } else {
                users.remove(deleteUser);
                persistAllUsers(users);
                System.out.println("successfully deleted");
            }
        } else {
            System.out.println("wrong password");
        }

    }
    // Change password method
    private static void changePassword(Scanner scanner, Map<String, String> users, String newPasscode, String currentUser) throws IOException {
        System.out.println("confirm current password");
        String passcode = scanner.next();
        if ((!users.get(currentUser).equals(passcode))) {
            System.out.println("wrong password");
            changePassword(scanner, users, newPasscode, currentUser);
        } else {
            System.out.println("enter new password");
            newPasscode = scanner.next();
            System.out.println("confirm new password");
            String newPasscodeTemp = scanner.next();
            if (!newPasscode.equals(newPasscodeTemp)) {
                System.out.println("passwords must match");
                changePassword(scanner, users, newPasscode, currentUser);
            }
            users.put(currentUser, newPasscode);
            persistAllUsers(users);
            System.out.println("successfully changed password");
        }
    }
    // method to keep all users on other methods
    private static void persistAllUsers(Map<String, String> users) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("userInfo.txt", false))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
            writer.flush();
        }
    }
    // Save a newly registered user method
    private static void saveUser(String username, String passcode, BufferedWriter writer) throws IOException {
        writer.write(username + ":" + passcode + "\n");
        writer.flush();
        System.out.println("User registered successfully");
    }
    // register a new user method
    private static void registerUser(Map<String, String> users, Scanner scanner, BufferedWriter writer) throws IOException {
        System.out.println("new Username?");
        String username = scanner.next().toLowerCase().trim();
        
        if (users.containsKey(username)) {
            System.out.println("Username already exists");
            return;
        }
        
        System.out.println("new passcode");
        String tempPasscode = scanner.next().trim();
        System.out.println("confirm passcode");
        String passcode = scanner.next().trim();

        if (tempPasscode.equals(passcode)) {
            users.put(username, passcode);
            saveUser(username, passcode, writer);
        } else {
            System.out.println("passcodes must match");
        }
    }
    // methods for logged in users
    private static void userLogged(Scanner scanner, Map<String, String> users, BufferedWriter writer, String currentUser) throws IOException {

        System.out.println("How can I help you?\n\n");

        System.out.println("""
                delete user(AUTH REQUIRED) 1.
                add log entry 2.
                change password 3.
                logout 4.
                """);
        int loggedChoice = scanner.nextInt();
        switch (loggedChoice) {
            case 1:
                deleteUser(scanner, users, currentUser);
                break;

            case 2:
                System.out.println("enter log entry");
                String logEntry = scanner.next();
                System.out.println("thank you");
                break;

            case 3:
                String newPasscode = "";
                changePassword(scanner, users, newPasscode, currentUser);
                break;

            case 4:
                System.out.println("goodbye");
                System.exit(0);
                break;
        }
    }

}