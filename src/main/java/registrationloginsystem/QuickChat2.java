package registrationloginsystem;

import java.util.ArrayList;
import java.util.Scanner;

// QuickChat: the user must log in (Part 1) before they can send messages (Part 2).
public class QuickChat2 {

    // Reads a whole number from the user and asks again if they type something else
    private static int readInt(Scanner input) {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a number: ");
            }
        }
    }

    private static String askRecipient(Scanner input, int messageNumber) {
        
        // Recipient: international code and no more than ten digits
        String recipient;
        Message check;
        do {
            System.out.print("Recipient cell number for example, +27718693000: ");
            recipient = input.nextLine();
            check = new Message(messageNumber, recipient, "check");
            
            System.out.println(check.checkRecipientCell());
            
        } while (!check.checkRecipientCell().equals("Cell phone number successfully captured."));
        
        return recipient;
    }

    private static String askMessageText(Scanner input, int messageNumber, String recipient) {
        
        // Message: no more than 250 characters
        String text;
        Message check;
        do {
            System.out.print("Enter your message: ");
            text = input.nextLine();
            check = new Message(messageNumber, recipient, text);
            
            if (check.checkMessageLength().equals("Message ready to send.")) {
                System.out.println("Message sent");
                
            } else {
                System.out.println("Please enter a message of less than 250 characters.");
            }
            
        } while (!check.checkMessageLength().equals("Message ready to send."));
        return text;
    }

    // Fourth main menu option: Stored Messages
    private static void storedMessagesMenu(Scanner input) {
        StoredMessages.readJson();   
        System.out.println("Stored messages loaded: " + StoredMessages.count());

        int choice = 0;
        while (choice != 8) {
            System.out.println();
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for messages sent or stored for a recipient");
            System.out.println("5) Delete a message using the message hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("7) Display a report of all sent messages");
            System.out.println("8) Back to main menu");
            System.out.print("Choose an option: ");
            choice = readInt(input);

            switch (choice) {
                case 1 -> System.out.println(StoredMessages.displaySendersAndRecipients());
                case 2 -> {
                    System.out.println("Longest message:");
                    System.out.println(StoredMessages.longestMessageDetails());
                }
                case 3 -> {
                    System.out.print("Enter the message ID: ");
                    String id = input.nextLine().trim();
                    String message = StoredMessages.searchByID(id);
                    if (message.equals("Message ID not found.")) {
                        System.out.println(message);
                    } else {
                        System.out.println("Recipient: " + StoredMessages.getRecipientByID(id));
                        System.out.println("Message: " + message);
                    }
                }
                case 4 -> {
                    System.out.print("Enter the recipient number: ");
                    ArrayList<String> found = StoredMessages.searchByRecipient(input.nextLine().trim());
                    if (found.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    }   for (String text : found) {
                        System.out.println(text);
                    }
                }
                case 5 -> {
                    System.out.print("Enter the message hash: ");
                    System.out.println(StoredMessages.deleteByHash(input.nextLine().trim().toUpperCase()));
                }
                case 6 -> System.out.println(StoredMessages.report());
                case 7 -> System.out.println(Message.printMessages());
                default -> {
                }
            }
        }
    }

    public static void main(String[] args) {
        // Register and log in (Part 1)
        try (Scanner input = new Scanner(System.in)) {
            
            System.out.print("Enter your first name: ");
            String firstName = input.nextLine();
            
            System.out.print("Enter your last name: ");
            String lastName = input.nextLine();
            
            Login login = new Login(firstName, lastName);
            
            // Keep asking until each registration detail is correct
            String username;
            do {
                System.out.print("Enter your username:");
                username = input.nextLine();
                
                System.out.println(login.captureUsername(username));
                
            } while (!login.checkUserName(username));
            
            String password;
            do {
                System.out.print("Enter your password: ");
                password = input.nextLine();
                System.out.println(login.capturePassword(password));
            } while (!login.checkPasswordComplexity(password));
            
            String cell;
            do {
                System.out.print("Enter your cell number for example, +27838968976: ");
                cell = input.nextLine();
                System.out.println(login.captureCellNumber(cell));
            } while (!login.checkCellPhoneNumber(cell));
            
            System.out.println(login.registerUser(username, password, cell));
            
            System.out.print("Login with username: ");
            String loginUser = input.nextLine();
            
            System.out.print("Login with password: ");
            String loginPass = input.nextLine();
            
            boolean loggedIn = login.loginUser(loginUser, loginPass);
            System.out.println(login.returnLoginStatus(loggedIn));
            
             // Users may only send messages if they have logged in successfully
            if (!loggedIn) {
                input.close();
                return;
            }
            
            // Welcome message
            System.out.println("Welcome to QuickChat.");
            Message.setSender(firstName + " " + lastName);
            StoredMessages.readJson();
            
            // The user says how many messages they want to enter
            System.out.print("How many messages do you want to enter? ");
            int allowed = readInt(input);
            boolean messagesEntered = false;
            boolean totalShown = false;
            
            // Numeric menu that runs until the user selects Quit
            int option = 0;
            while (option != 3) {
                System.out.println();
                System.out.println("Option 1) Send Messages");
                System.out.println("Option 2) Show recently sent messages");
                System.out.println("Option 3) Quit");
                System.out.println("Option 4) Stored Messages");
                System.out.print("Choose an option: ");
                option = readInt(input);
                
                if (option == 1) {
                    
                    // Only the set number of messages may be entered
                    if (messagesEntered) {
                        System.out.println("You have entered the maximum number of messages.");
                        continue;
                    }
                    
                    // The loop counter i is the message number
                    for (int i = 0; i < allowed; i++) {
                        System.out.println("\nMessage " + (i + 1) + " of " + allowed);
                        
                        String recipient = askRecipient(input, i);
                        String text = askMessageText(input, i, recipient);
                        
                        Message message = new Message(i, recipient, text);
                        System.out.println(message.returnMessageIDText());
                        
                        // Send, disregard or store the message
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Choose: ");
                        int choice = readInt(input);
                        System.out.println(message.sentMessage(choice));
                        
                        if (choice == 1) {
                            System.out.println("----- Message details -----");
                            System.out.println(message.getDetails());
                            System.out.println("---------------------------");
                            
                        } else if (choice == 2) {
                            System.out.print("Press 0 to delete the message: ");
                            if (input.nextLine().equals("0")) {
                                message.deleteMessage();
                                System.out.println("Message deleted.");
                                
                            } else {
                                System.out.println("Message was not deleted.");
                            }
                        }
                    }
                    
                    messagesEntered = true;
                    // Once all the messages have been entered, show the total
                    System.out.println("\nAll messages have been entered.");
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    totalShown = true;
                    
                } else if (option == 2) {
                    System.out.println("Coming Soon.");
                    
                } else if (option == 4) {
                    storedMessagesMenu(input);
                }
            }
            
            // On quit, show the messages sent and the total
            System.out.println("\nMessages sent:");
            System.out.println(Message.printMessages());
            if (!totalShown) {
                System.out.println("Total messages sent: " + Message.returnTotalMessages());
            }
        }
    }
}