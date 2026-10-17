package registrationloginsystem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Unit tests for the Message class.
class MessageTest {

    // Test Data: Num Messages = 2
    // Test Data for Task 1, user selects Send.
    // Test Data for Message 2, user selects Discard.
    private Message message1;   
    private Message message2;   

    // Runs before every test: clears old messages and creates the two test messages
    @BeforeEach
    void setUp() {
        Message.resetMessages();

        // The message ID is normally auto generated. 
        // A fixed ID is used here, so the hash is the same every time.
        message1 = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        
        message2 = new Message("0198765432", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
    }

    //  Test: The message must not be more than 250 characters 
    // Success: The message is short enough
    @Test
    void messageLengthSuccess() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    // Failure: 255 characters is 5 more than the limit
    @Test
    void messageLengthFailure() {
        Message longMessage = new Message("0012345678", 0, "+27718693002", "a".repeat(255));
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.",
                longMessage.checkMessageLength());
    }

    // Test: The recipient number is correctly formatted 
    // Success: +27718693002 has an international code
    @Test
    void recipientCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    // Failure: 08575975889 has no international code
    @Test
    void recipientIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.", message2.checkRecipientCell());
    }

    //  Test: The message hash is correct 
    // Test Case 1 should give 00:0:HITONIGHT
    @Test
    void messageHashCorrect() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    // The remaining hashes are tested in a loop
    @Test
    void messageHashesInLoop() {
        Message[] messages = {message1, message2};
        String[] expectedHashes = {"00:0:HITONIGHT", "01:1:HIPAYMENT"};

        for (int i = 0; i < messages.length; i++) {
            assertEquals(expectedHashes[i], messages[i].createMessageHash());
        }
    }

    //  Test: the message ID is created 

    @Test
    void messageIDCreated() {
        assertEquals("Message ID generated: 0012345678", message1.returnMessageIDText());
    }

    // An automatically generated ID must be no more than ten characters
    @Test
    void generatedMessageIDIsTenCharacters() {
        Message generated = new Message(0, "+27718693002", "Hello there");
        assertTrue(generated.checkMessageID());
        assertEquals(10, generated.getMessageID().length());
    }

    //  Test: MessageSent (user choices 1, 2 and 3) 
    // 1) User selected 'Send Message'
    @Test
    void userSelectsSend() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    // 2) User selected 'Disregard Message'
    @Test
    void userSelectsDisregard() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    // 3) User selected 'Store Message'
    @Test
    void userSelectsStore() {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    //  Test data: 2 messages, then the total number sent 
    // Message 1 is sent and message 2 is discarded, so the total sent is 1
    @Test
    void totalNumberSentForTestData() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
        assertEquals(1, message1.returnTotalMessages());
    }

    // printMessages should include the details of the sent message
    @Test
    void printMessagesShowsSentMessages() {
        message1.sentMessage(1);
        assertTrue(message1.printMessages().contains("00:0:HITONIGHT"));
    }
}
