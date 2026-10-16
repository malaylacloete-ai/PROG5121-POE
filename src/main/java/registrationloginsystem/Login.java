package registrationloginsystem;

/*
* Part 1 - Registration and Login Features 
* This class contains:
* - Username validation
* - Password validation
* - South African phone number validation
* - User registration
* - User login
* - Login status messages
*/

public class Login {
    // Validation to store the user's registration details
    private String username;
    private String password;
    private String phoneNumber;
    
    // Variables to store the user's name
    private String firstName;
    private String lastName;
    
    // Variable used to store the login result
    private boolean loginSuccessful;
    
    /*
    * Constructor used to create a Login object
    */
    public Login(){
        loginSuccessful = false;
    }
    
    /*
    * Constructor with user details
    */
    public Login(String username, String password, String phoneNumber, String firstName, String lastName){
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginSuccessful = false;
    }
    
    /*
    *Boolean: checkUserName()
    *
    * Checks that:
    * 1. The username contains an underscore (_)
    * 2. The username is no more than 5 characters long
    */
    public boolean checkUserName(){
        return username.contains("_") && username.length() <= 5;
    }
    
    /*
    *Boolean: checkPasswordComplexity()
    *
    & The password must:
    * - Have at leaste 8 characters
    * - Contain a capital letter
    * - Contain a number 
    * - Contain a special character
    */
    public boolean checkPasswordComplexity(){
        
        // Check if it has at least 8 characters
        if (password.length() < 8){
            return false;
        }
        
        // Variable used to check password
        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;
        
        // Checking each character in the password
        for (int i = 0; i < password.length(); i ++){
            char character = password.charAt(i);
        
        // Check for Capital letter
        if (Character.isUpperCase(character)){
            hasCapitalLetter = true;
        }
        
        // Check for a number 
        if (Character.isDigit(character)){
            hasNumber = true;
        }
        
        // Check for a special character
        if (!Character.isLetterOrDigit(character)){
            hasSpecialCharacter = true;
        }   
    }
    // Return true only when all requirements are met
    return hasCapitalLetter
            && hasNumber
            && hasSpecialCharacter;
    }
    
    /*
    * Boolean: checkCellPhoneNumber()
    * Checks that the phone number has an internasional country code and that it is no more than 10 characters long
    * (Oracle, 2025) Oracle.2025.Pattern(Java Platform SE).Oracle Java Documentation
    */
    public boolean checkCellPhoneNumber(){
        String phoneRegex = "^\\+27[0-9]{10}$";
        return phoneNumber.matches(phoneRegex);
    }
    
    /*
    * String: registerUser
    * Returns the correct registration message depending on whether the username and password is valid
    */
    public String registerUser(){
        if (!checkUserName()){
            
           return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in lenght.";
           
        } else if (!checkPasswordComplexity()){
            
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter a number, and a special character.";
            
        } else if (! checkCellPhoneNumber()){
            
            return "Cell phone number incorrectly formatted or does not contain internasional code.";
            
        } else {
            
            return """
                   Username successfully captured. 
                   Password successfully captured. 
                   Cell phone number successfully added.""";
        }
    }
    
    /*
    * Boolean: loginUser()
    * Checks whether the username and password entered during login match the registred details.
    */
    public boolean loginUser(String enteredUsername, String enteredPassword){
        
        // Comparing entered details with stored details
        loginSuccessful = enteredUsername.equals(username)
                && enteredPassword.equals(password);
        
        return loginSuccessful;
    }
    
    /* String: returnLoginStatus()
    *  Returns a message depending on whether the login was successful or unsuccessful.
    */
    public String returnLoginStatus(){
        
        if (loginSuccessful){
            
            return "Welcome " + firstName + ","
                    + lastName + " it is great to see you again.";
            
        } else {
            
            return "Username is not correctly formatted,"
                    + "please ensure that your username contains an underscore and is no more than five characters in lenght";
        }
    }
    
    // Gets the stored details
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
}

