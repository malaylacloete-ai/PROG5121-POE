package registrationloginsystem;

import java.util.regex.Pattern;

//The Login class handles user registration and login.
//It checks the username, password and cell phone number, stores them when the user registers, and later checks the login details against what was stored.
public class Login {
   // Oracle, 2018. Lesson: Regular Expressions. The Java Tutorials. [Online].
   // Available at: [https://stackoverflow.com/questions/51994128/oracle-regular-expression-to-format-phone-number] [Accessed: 16 October 2026].
    private static final Pattern CELL_PATTERN = Pattern.compile("^\\+\\d{1,3}\\d{1,10}$");

    // The user's names, used in the welcome message
    private final String firstName;
    private final String lastName;

    // These are stored when the user registers successfully
    private String username;
    private String password;
    private String cellNumber;

    // Constructor: creates a Login object for a user with the given names.
    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //Checks that the username contains an underscore and is no more than five characters long.
     public boolean checkUserName(String username) {
        // The null check stops the program crashing if no username is given
        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    //Checks the password complexity rules: at least eight characters,a capital letter, a number and a special character.
     public boolean checkPasswordComplexity(String password) {
        // This is when the password is too short
        if (password == null || password.length() < 8) {
            return false;
        }

        // Start by assuming none of the required characters are present
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        // Look at every character in the password one by one
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;                 
            } else if (Character.isDigit(c)) {
                hasNumber = true;                  
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;                 
            }
        }

        // All three must have been found
        return hasCapital && hasNumber && hasSpecial;
    }

    //Checks that the cell number has an international code followed by a number of no more than ten digits.
    public boolean checkCellPhoneNumber(String cellNumber) {
        // matches() is true only if the WHOLE text fits the pattern
        return cellNumber != null && CELL_PATTERN.matcher(cellNumber).matches();
    }

    // Returns the message for the username if it successful or  has a error. 
    public String captureUsername(String username) {
        if (checkUserName(username)) {
            return "Username successfully captured.";
        }
        return "Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.";
    }

    //Returns the message for the password if it is successful or has a error. 
    public String capturePassword(String password) {
        if (checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        }
        return "Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, and a special character.";
    }

    //Returns the message for the cell number if it is successful or has a error. 
    public String captureCellNumber(String cellNumber) {
        if (checkCellPhoneNumber(cellNumber)) {
            return "Cell number successfully captured.";
        }
        return "Cell number is incorrectly formatted or does not contain an international code; "
                + "please correct the number and try again.";
    }

    //Registers the user. Details are only stored if all three are valid.
    //Returns the message that says what happened.
    public String registerUser(String username, String password, String cellNumber) {
        // Check one detail at a time and stop at the first problem
        if (!checkUserName(username)) {
            return captureUsername(username);
        }
        if (!checkPasswordComplexity(password)) {
            return capturePassword(password);
        }
        if (!checkCellPhoneNumber(cellNumber)) {
            return captureCellNumber(cellNumber);
        }

        // Everything is valid, so store the details for login later
        this.username = username;
        this.password = password;
        this.cellNumber = cellNumber;
        return "User has been registered successfully.";
    }

    //Checks the entered login details against the stored registration details.
    //Returns true only if both the username and password match.
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        // If nobody has registered yet, the login must fail
        return username != null
                && password != null
                && username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    //Returns the message for a successful or failed login.
    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
}
