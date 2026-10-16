package registrationloginsystem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Unit tests for the Login class 
//assertEquals compares the expected message with the actual message.
//assertTrue / assertFalse check that a method returns true / false
class LoginTest {

    private Login login;

    // Runs before every test: creates a fresh Login object and registers a valid user
    @BeforeEach
    void setUp() {
        login = new Login("Kyle", "Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    // Username "kyl_1" is valid, so the user can log in and gets the welcome message
    @Test
    void usernameCorrectlyFormatted() {
        assertEquals("Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus(login.loginUser("kyl_1", "Ch&&sec@ke99!")));
    }

    // Username has no underscore and is too long, so the error message is expected
    @Test
    void usernameIncorrectlyFormatted() {
        assertEquals("Username is not correctly formatted; please ensure that your username "
                        + "contains an underscore and is no more than five characters in length.",
                login.captureUsername("kyle!!!!!!!"));
    }

    // Password meets all the complexity rules
    @Test
    void passwordMeetsComplexity() {
        assertEquals("Password successfully captured.",
                login.capturePassword("Ch&&sec@ke99!"));
    }

    // "password" has no capital letter, number or special character
    @Test
    void passwordDoesNotMeetComplexity() {
        assertEquals("Password is not correctly formatted; please ensure that the password "
                        + "contains at least eight characters, a capital letter, a number, and a special character.",
                login.capturePassword("password"));
    }

    // Number starts with +27, so it is correctly formatted
    @Test
    void cellPhoneCorrectlyFormatted() {
        assertEquals("Cell number successfully captured.",
                login.captureCellNumber("+27838968976"));
    }

    // Number has no international code, so the error message is expected
    @Test
    void cellPhoneIncorrectlyFormatted() {
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; "
                        + "please correct the number and try again.",
                login.captureCellNumber("08966553"));
    }

    // Correct username and password: login succeeds
    @Test
    void loginSuccessful() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    // Wrong password: login fails
    @Test
    void loginFailed() {
        assertFalse(login.loginUser("kyl_1", "wrongPass1!"));
    }

    @Test
    void usernameCorrectlyFormattedTrue() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void usernameIncorrectlyFormattedFalse() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    void passwordMeetsComplexityTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void passwordDoesNotMeetComplexityFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    void cellPhoneCorrectlyFormattedTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void cellPhoneIncorrectlyFormattedFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}



