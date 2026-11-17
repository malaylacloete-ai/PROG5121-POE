package registrationloginsystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Holds the stored messages in parallel arrays and reads or writes the JSON file.
 * The same index in every array belongs to the same message.
 */
public class StoredMessages {

    private static String jsonFile = "messages.json";

    private static final ArrayList<String> ids = new ArrayList<>();
    private static final ArrayList<String> hashes = new ArrayList<>();
    private static final ArrayList<String> senders = new ArrayList<>();
    private static final ArrayList<String> recipients = new ArrayList<>();
    private static final ArrayList<String> messages = new ArrayList<>();

    // Lets the tests use a different file
    public static void setFile(String fileName) {
        jsonFile = fileName;
    }

    public static void clear() {
        ids.clear();
        hashes.clear();
        senders.clear();
        recipients.clear();
        messages.clear();
    }

    public static int count() {
        return ids.size();
    }

    // Adds a message to the arrays and saves the file
    public static void add(String id, String hash, String sender, String recipient, String message) {
        ids.add(id);
        hashes.add(hash);
        senders.add(sender);
        recipients.add(recipient);
        messages.add(message);
        writeJson();
    }

    // Reads the JSON file into the arrays
    public static void readJson() {
        clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"messageID\"")) {
                    ids.add(getValue(line));
                    
                } else if (line.startsWith("\"messageHash\"")) {
                    hashes.add(getValue(line));
                    
                } else if (line.startsWith("\"sender\"")) {
                    senders.add(getValue(line));
                    
                } else if (line.startsWith("\"recipient\"")) {
                    recipients.add(getValue(line));
                    
                } else if (line.startsWith("\"message\"")) {
                    messages.add(getValue(line));
                }
            }
        } catch (IOException e) {
            
        }
    }

    // Gets the text between the quotes on a line like:  "key": "value",
    private static String getValue(String line) {
        int start = line.indexOf(": \"") + 3;
        int end = line.lastIndexOf('"');
        return line.substring(start, end).replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static void writeJson() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < ids.size(); i++) {
            json.append("  {\n")
                .append("    \"messageID\": \"").append(escape(ids.get(i))).append("\",\n")
                .append("    \"messageHash\": \"").append(escape(hashes.get(i))).append("\",\n")
                .append("    \"sender\": \"").append(escape(senders.get(i))).append("\",\n")
                .append("    \"recipient\": \"").append(escape(recipients.get(i))).append("\",\n")
                .append("    \"message\": \"").append(escape(messages.get(i))).append("\"\n")
                .append("  }");
            if (i < ids.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");

        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.out.println("Could not save the messages: " + e.getMessage());
        }
    }

    // Escapes backslashes and quotes so the JSON stays valid
    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Full details of the message at one index
    private static String details(int i) {
        return "Message ID: " + ids.get(i)
                + "\nMessage Hash: " + hashes.get(i)
                + "\nSender: " + senders.get(i)
                + "\nRecipient: " + recipients.get(i)
                + "\nMessage: " + messages.get(i) + "\n";
    }

    // Sender and recipient of all stored messages
    public static String displaySendersAndRecipients() {
        if (ids.isEmpty()) {
            return "No stored messages.";
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            result.append("Sender: ").append(senders.get(i))
                  .append(" | Recipient: ").append(recipients.get(i)).append("\n");
        }
        
        return result.toString();
    }

    // Longest stored message
    public static String longestMessage() {
        if (ids.isEmpty()) {
            return "No stored messages.";
        }
        
        int longest = 0;
        for (int i = 1; i < messages.size(); i++) {
            if (messages.get(i).length() > messages.get(longest).length()) {
                longest = i;
            }
        }
        
        return messages.get(longest);
    }

    // Search for a message ID (stored and sent messages)
    public static String searchByID(String id) {
        int index = ids.indexOf(id);
        if (index != -1) {
            return messages.get(index);
        }
        
        for (Message sent : Message.getSentMessages()) {
            if (sent.getMessageID().equals(id)) {
                return sent.getMessageText();
            }
        }
        
        return "Message ID not found.";
    }

    public static String getRecipientByID(String id) {
        int index = ids.indexOf(id);
        if (index != -1) {
            return recipients.get(index);
        }
        
        for (Message sent : Message.getSentMessages()) {
            if (sent.getMessageID().equals(id)) {
                return sent.getRecipient();
            }
        }
        
        return "Message ID not found.";
    }

    // All sent or stored messages for one recipient
    public static ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> found = new ArrayList<>();
        for (Message sent : Message.getSentMessages()) {
            if (sent.getRecipient().equals(recipient)) {
                found.add(sent.getMessageText());
            }
        }
        
        for (int i = 0; i < ids.size(); i++) {
            if (recipients.get(i).equals(recipient)) {
                found.add(messages.get(i));
            }
        }
        
        return found;
    }

    public static boolean containsHash(String hash) {
        return hashes.contains(hash);
    }

    // Delete a stored message using its hash
    public static String deleteByHash(String hash) {
        int index = hashes.indexOf(hash);
        if (index == -1) {
            
            return "Message hash not found.";
        }
        String text = messages.get(index);
        ids.remove(index);
        hashes.remove(index);
        senders.remove(index);
        recipients.remove(index);
        messages.remove(index);
        writeJson();
        return "Message: \"" + text + "\" successfully deleted.";
    }

    // Report of all stored messages
    public static String report() {
        if (ids.isEmpty()) {
            return "No stored messages.";
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            result.append(details(i)).append("\n");
        }
        
        return result.toString();
    }
}