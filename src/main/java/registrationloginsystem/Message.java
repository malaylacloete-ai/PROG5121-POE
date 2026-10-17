package registrationloginsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

// The Message class represents one message in QuickChat.
// It checks the details, creates the hash, and sends, stores or discards the message.
public final class Message {

    // Same cell number pattern as Part 1: + then country code then up to 10 digits
    private static final Pattern CELL_PATTERN = Pattern.compile("^\\+\\d{1,3}\\d{1,10}$");

    // These lists are shared by all messages while the program is running
    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static final ArrayList<Message> storedMessages = new ArrayList<>();

    // The file that stored messages are written to
    private static final String JSON_FILE = "messages.json";

    // Details of the message
    private final String messageID;
    private final int messageNumber;   
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    // Constructor used when the message ID is already known for example in the tests.
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Constructor that generates a random ten-digit message ID.
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    // Creates a random ten-digit number, leading zeros are kept, so it is a String.
    private static String generateMessageID() {
        long number = (long) (new Random().nextDouble() * 10_000_000_000L);
        return String.format("%010d", number);
    }

    // the following code does checks 
    // Checks weither the message ID is no more than ten characters long. 
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Checks wheither recipient cell number is no longer then 10 characters, starts with a code and then returns the matching message. 
    public String checkRecipientCell() {
        
        if (recipient != null && CELL_PATTERN.matcher(recipient).matches()) {
            return "Cell phone number successfully captured.";
        }
        
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.";
    }

    // The message must not be longer than 250 characters. 
    public String checkMessageLength() {
        int length = messageText.length();
        
        if (length <= 250) {
            return "Message ready to send.";
        }
        
        int tooLong = length - 250;
        return "Message exceeds 250 characters by " + tooLong + "; please reduce the size.";
    }

    // Returns the text for the message ID for example, "Message ID generated: 0012345678". 
    public String returnMessageIDText() {
        return "Message ID generated: " + messageID;
    }

    // Hash 

    // Creates the hash: first two digits of the ID, the message number, and the first and last words of the message, all in capitals.
    // Example: 00:0:HITONIGHT
    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);

        // Split the message into words
        String[] words = messageText.trim().split("\\s+");

        // Remove punctuation (like , ? !) from the first and last words
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");

        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Send, store, discard 

    // Handles the user's choice: 1 = send, 2 = disregard, 3 = store to send later.
    public String sentMessage(int choice) {
        
        switch (choice) {
            case 1 -> {
                sentMessages.add(this);
                return "Message successfully sent.";
            }
            case 2 -> {
                return "Press 0 to delete the message.";
            }
            case 3 -> {
                storeMessage();
                return "Message successfully stored.";
            }
            default -> {
            }
        }
        return "Invalid choice.";
    }

    // Returns the details of every message sent while the program is running. 
    public String printMessages() {
        StringBuilder sb = new StringBuilder();
        
        for (Message m : sentMessages) {
            sb.append(m.getDetails()).append("\n");
        }
        
        return sb.toString();
    }

    // Returns the total number of messages sent. 
    public int returnTotalMessages() {
        return sentMessages.size();
    }

    // Details in the required order: ID, Hash, Recipient, Message. 
    public String getDetails() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText + "\n";
    }

    // Stores the message in a JSON file, all stored messages are written each time. 
    // Reference for writing the messages to a file (FileWriter in storeMessage):
    // Oracle. 2026. Class FileWriter. Java Platform, Standard Edition 8 API Specification.
    // [Online]. Available at: https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html
    // [Accessed 17 October 2026].
    public void storeMessage() {
        storedMessages.add(this);

        // Build the JSON text 
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n")
                .append("    \"messageID\": \"").append(escape(m.messageID)).append("\",\n")
                .append("    \"messageHash\": \"").append(escape(m.messageHash)).append("\",\n")
                .append("    \"recipient\": \"").append(escape(m.recipient)).append("\",\n")
                .append("    \"message\": \"").append(escape(m.messageText)).append("\"\n")
                .append("  }");
            
            if (i < storedMessages.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");

        // Writes the text to the file
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write(json.toString());
            
        } catch (IOException e) {
            System.out.println("Could not store the message: " + e.getMessage());
        }
    }

    // Escapes backslashes and quotes so the JSON stays valid
    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Clears the lists that is used by the tests, so that each test starts fresh. 
    public static void resetMessages() {
        sentMessages.clear();
        storedMessages.clear();
    }

    // Getters
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}
