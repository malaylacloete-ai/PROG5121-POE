package registrationloginsystem;

import java.util.Scanner;

//Main class: runs the program in the console.
//The user registers first and then logs in.
public class Main {

    public static void main(String[] args) {
        // Scanner reads what the user types
        Scanner input = new Scanner(System.in);

        System.out.println("=== REGISTRATION ===");
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        // Create the Login object with the user's names
        Login login = new Login(firstName, lastName);

        // Keep asking for the username until it is correctly formatted
        String username;
        do {
            System.out.print("Enter a username: ");
            username = input.nextLine();
            System.out.println(login.captureUsername(username));
        } while (!login.checkUserName(username));

        // Keep asking for the password until it meets the complexity rules
        String password;
        do {
            System.out.print("Enter a password: ");
            password = input.nextLine();
            System.out.println(login.capturePassword(password));
        } while (!login.checkPasswordComplexity(password));

        // Keep asking for the cell number until it is correctly formatted
        String cell;
        do {
            System.out.print("Enter your cell phone number (e.g. +27838968976): ");
            cell = input.nextLine();
            System.out.println(login.captureCellNumber(cell));
        } while (!login.checkCellPhoneNumber(cell));

        // All details are valid, so register the user
        System.out.println(login.registerUser(username, password, cell));

        System.out.println();
        System.out.println("=== LOGIN ===");
        System.out.print("Enter your username: ");
        String loginUser = input.nextLine();
        System.out.print("Enter your password: ");
        String loginPass = input.nextLine();

        // Check the details and show the welcome or error message
        boolean success = login.loginUser(loginUser, loginPass);
        System.out.println(login.returnLoginStatus(success));

        input.close();
    }
}
