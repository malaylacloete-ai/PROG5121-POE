package registrationloginsystem;

import java.util.Scanner;

// QuickChat: the user must log in (Part 1) before they can send messages (Part 2).
public class QuickChat {

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
                System.out.print("Enter your username: ");
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
            
            // The user says how many messages they want to enter
            System.out.print("How many messages do you want to enter? ");
            int allowed = readInt(input);
            int entered = 0;
            boolean totalShown = false;
            Message lastMessage = null;
            
            // Numeric menu that runs until the user selects Quit
            int option = 0;
            while (option != 3) {
                System.out.println();
                System.out.println("Option 1) Send Messages");
                System.out.println("Option 2) Show recently sent messages");
                System.out.println("Option 3) Quit");
                System.out.print("Choose an option: ");
                option = readInt(input);
                
                if (option == 1) {
                    
                    // Only the set number of messages may be entered
                    if (entered >= allowed) {
                        System.out.println("You have entered the maximum number of messages.");
                        continue;
                    }
                    
                    // Recipient: international code and no more than ten digits
                    String recipient;
                    Message check;
                    do {
                        System.out.print("Recipient cell number for example, +27718693000: ");
                        recipient = input.nextLine();
                        check = new Message(entered, recipient, "check");
                        System.out.println(check.checkRecipientCell());
                    } while (!check.checkRecipientCell().equals("Cell phone number successfully captured."));
                    
                    // Message: no more than 250 characters
                    String text;
                    do {
                        System.out.print("Enter your message: ");
                        text = input.nextLine();
                        check = new Message(entered, recipient, text);
                        
                        if (check.checkMessageLength().equals("Message ready to send.")) {
                            System.out.println("Message sent");
                            
                        } else {
                            
                            System.out.println("Please enter a message of less than 250 characters.");
                        }
                    } while (!check.checkMessageLength().equals("Message ready to send."));
                    
                    // Create the message (ID and hash are generated automatically)
                    Message message = new Message(entered, recipient, text);
                    System.out.println(message.returnMessageIDText());
                    entered++;
                    lastMessage = message;
                    
                    // Send, disregard or store the message
                    System.out.println("1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("3) Store Message to send later");
                    System.out.print("Choose: ");
                    int choice = readInt(input);
                    System.out.println(message.sentMessage(choice));
                    
                    if (choice == 1) {
                        // Full details in the order: ID, Hash, Recipient, Message
                        System.out.println(message.getDetails());
                        System.out.println("Number of messages sent: "
                                + message.returnTotalMessages());
                        
                    } else if (choice == 2) {
                        
                        System.out.print("Press 0 to delete the message: ");
                        String delete = input.nextLine();
                        
                        if (delete.equals("0")) {
                            System.out.println("Message deleted.");
                            
                        } else {
                            
                            System.out.println("Message was not deleted.");
                        }
                    }
                    
                    // Once all the messages have been entered, show the total
                    if (entered == allowed) {
                        System.out.println("\nAll messages have been entered.");
                        System.out.println("Total messages sent: "
                                + message.returnTotalMessages());
                        totalShown = true;
                    }
                    
                } else if (option == 2) {
                    
                    // Message still developing
                    System.out.println("Coming Soon.");
                }
            }
            
            // On quit, show the messages sent and the total
            if (lastMessage != null) {
                System.out.println("\nMessages sent:");
                System.out.println(lastMessage.printMessages());
                
                if (!totalShown) {
                    System.out.println("Total messages sent: "
                            + lastMessage.returnTotalMessages());
                }
                
            } else {
                
                System.out.println("Total messages sent: 0");
            }
        }
    }
}
