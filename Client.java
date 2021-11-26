import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        // Client exit when hostname or port omitted at start-up.
        if (args.length < 2) {
            System.out.println("Correct Command: ./startClient.sh <hostname> <port>");
            System.exit(0);
        }

        // Variables required to establish server connection.
        String hostname = args[0];
        String port = args[1];

        try {
            // Open connection to the Server.
            Socket socket = new Socket(hostname, Integer.parseInt(port));

            //Create input and output streams to read from and write to the server.
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Read client input.
            Console clientIn = System.console();

            // String variable for user input from console.
            String userInput;

            // Variable to make menu option selections.
            boolean menuOptionSelected = false;

            //Variable to tack if a START RNA message has been sent in the session.
            boolean startRNASent = false;

            // Keep the connection to the server open until socket is closed or forced system exit.
            while (!socket.isClosed()) {
                // Loop over the menu when an option is not selected.
                while (true) {

                    // Variable to store user chosen option.
                    String menuOption;
                    // Call the menu function to display menu options.
                    menuOption = Menu();

                    // Menu Options: 1 - DNA optimisation, 2 - System Exit.
                    if (menuOption.equalsIgnoreCase("1")) {
                        menuOptionSelected = true;
                        break;
                    } else if (menuOption.equalsIgnoreCase("2")) {
                        // Send disconnect message to serve to exit client side.
                        if(startRNASent){
                            userInput = "Disconnect";
                            toServer.println(userInput.toUpperCase());
                        }

                        menuOptionSelected = false;

                        // Call closeSocket function to close streams and socket and exit the system.
                        closeSocket(socket, toServer, fromServer);
                        break;
                    }

                    // If an valid menu option selected user prompted to re-enter option.
                    if (!menuOption.equalsIgnoreCase("1") || !menuOption.equalsIgnoreCase("2")) {
                        System.out.println("*************************************************************");
                        System.out.println("Invalid Menu Option Entered - only option 1 and 2 available.");
                        System.out.println("*************************************************************");
                    }

                }

                /* Loop to validate user input and send client input to server for optimisation. */
                while (menuOptionSelected) {

                    System.out.println("##############################################################");
                    System.out.println("#                                                            #");
                    System.out.println("#                     Please enter DNA                       #");
                    System.out.println("#    (Only DNA nucleotides of length divisible by 3 valid)   #");
                    System.out.println("#                                                            #");
                    System.out.println("##############################################################");

                    // Get user DNA sequence input.
                    userInput = clientIn.readLine("User DNA sequence: ");
                    System.out.println("#                                                            #");
                    System.out.println("##############################################################");
                    // Validate that the user input is a correct DNA sequence.
                    if (userInput.length() % 3 != 0 || !(userInput.matches("^[ATCGatcg]+$"))) {

                        if(userInput.length() % 3 != 0) {
                            System.out.println("");
                            System.out.println("         Error - DNA sequences must be divisible by 3");
                            System.out.println("");
                        }else if (!(userInput.matches("^[ATCGatcg]+$"))) {
                            System.out.println("");
                            System.out.println("    Error - DNA must only contain DNA nucleotides(A,T,G,C)");
                            System.out.println("");
                        }

                        break;
                    }

                    // If the user input is valid, end the server the START RNA message.
                    String startServer = "start RNA";
                    toServer.println(startServer.toUpperCase());
                    startRNASent = true;

                    //Send the user input DNA sequence converted to uppercase to the server.
                    toServer.println(userInput.toUpperCase());

                    /* Assign the optimised DNA sequence received from the serve to the
                    optimisedRNA variable.*/
                    String optimisedDNA = fromServer.readLine();

                    // If the optimised DNA sequence received from the server is invalid close the socket.
                    if (optimisedDNA.length() % 3 != 0 || !(optimisedDNA.matches("^[ATCGatcg]+$"))) {
                        System.out.println("Invalid input");
                        socket.close();
                    }else{
                        // Output the validated optimised DNA sequence into the console.
                        System.out.println("");
                        System.out.println("Optimised DNA: " + optimisedDNA);
                        System.out.println("");

                        // Re-set menu selection to return to the menu.
                        menuOptionSelected = false;
                    }
                }
            }
        } catch (UnknownHostException e) {
            // Error if the hostname invalid.
            System.out.println("Server not found: " + e.getMessage());

        } catch (IOException e) {
            // Error if server not started.
            System.out.println("Server error: " + e.getMessage());

        }
    }

    /**
     * Function:        closeSocket()
     * Description:     Function that closes the client socket and streams and exits the system.
     *
     * @param socket - variable that represents the client side socket.
     * @param pw     - printwriter for sending client input to server.
     * @param br     - bufferreader for receiving output from the server.
     */
    private static void closeSocket(Socket socket, PrintWriter pw, BufferedReader br) throws IOException {
        pw.close();
        br.close();
        socket.close();
        System.out.println("Client shutting down.");
        System.exit(0);
    }


    /**
     * Function:       Menu()
     * Description:    Function that provides a user interface for the program.
     *
     * @return the menu option selected by the user.
     */
    private static String Menu() {
        //User Interface
        //Variable to record menu selection
        String selection;
        Scanner inputMenu = new Scanner(System.in);
        System.out.println("##############################################################");
        System.out.println("#                                                            #");
        System.out.println("#                     Select a Menu Item                     #");
        System.out.println("#                                                            #");
        System.out.println("##############################################################");
        System.out.println("# Option 1 - Optimise DNA                                    #");
        System.out.println("# Option 2 - Exit                                            #");
        System.out.println("##############################################################");
        System.out.print("Select Option: ");
        selection = inputMenu.next();
        return selection;
    }
}