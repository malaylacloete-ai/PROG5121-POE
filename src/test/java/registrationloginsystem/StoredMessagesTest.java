package registrationloginsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StoredMessagesTest {

    private static final String TEST_FILE = "test_stored_messages.json";

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    // Creates the test data and fills the arrays using the flags
    @BeforeEach
    void setUp() {
        new File(TEST_FILE).delete();
        StoredMessages.setFile(TEST_FILE);
        StoredMessages.clear();
        Message.resetMessages();
        Message.setSender("Test Sender");

        // Message IDs are entered by the developer so the hashes are always the same
        message1 = new Message("0012345678", 0, "+27834557896", "Did you get the cake?");
        message2 = new Message("0198765432", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        message3 = new Message("0234567890", 2, "+27834484567", "Yohoooo, I am at your gate.");
        message4 = new Message("0838884567", 3, "0838884567", "It is dinner time!");
        message5 = new Message("0456789012", 4, "+27838884567", "Ok, I am leaving without you.");

        // Sent
        message1.sentMessage(1);  
        
        // Stored
        message2.sentMessage(3);  
        
        // Disregard
        message3.sentMessage(2);  
        
        // Sent
        message4.sentMessage(1);  
        
        // Stored
        message5.sentMessage(3);   
    }

    // Sent messages array contains the expected test data
    @Test
    void sentMessagesArrayCorrectlyPopulated() {
        ArrayList<String> sentTexts = new ArrayList<>();
        for (Message sent : Message.getSentMessages()) {
            sentTexts.add(sent.getMessageText());
        }
        assertEquals(List.of("Did you get the cake?", "It is dinner time!"), sentTexts);
    }

    @Test
    void displayTheLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                StoredMessages.longestMessageDetails());
    }

    @Test
    void searchForMessageID() {
        assertEquals("It is dinner time!", StoredMessages.searchByID("0838884567"));
    }

    @Test
    void searchMessagesForARecipient() {
        assertEquals(List.of("Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you."),
                StoredMessages.searchByRecipient("+27838884567"));
    }

    @Test
    void deleteAMessageUsingTheMessageHash() {
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                StoredMessages.deleteByHash("01:1:WHERETIME"));
    }

    @Test
    void displayReportOfSentMessages() {
        String expected = "Message ID: 0012345678\n"
                + "Message Hash: 00:0:DIDCAKE\n"
                + "Recipient: +27834557896\n"
                + "Message: Did you get the cake?\n\n"
                + "Message ID: 0838884567\n"
                + "Message Hash: 08:3:ITTIME\n"
                + "Recipient: 0838884567\n"
                + "Message: It is dinner time!\n\n";
        assertEquals(expected, Message.printMessages());
    }
}
