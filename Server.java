import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class Server {

    public static void main(String[] args) throws NullPointerException, IOException {

        // Server exit if port omitted at start-up.
        if (args.length == 0){
            System.out.println( "Correct Command: ./startServer.sh <port>" );
            System.exit(0);
        }

        // Variable to store port number.
        int port = Integer.parseInt(args[0]);

        // Open connection to the Client.
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port " + port);

        // serverSocket object keeps listening until system terminates.
        while(true) {
            // Socket to accept incoming request to the server.
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");

            // BufferReader for client input stream.
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Printwriter to send response back to the client.
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Variable to store client DNA sequence input.
            String sequence;

            // Variable to confirm server received START RNA message.
            boolean serverReady = false;

            // server keeps listening to the client until socket closed.
            while (true) {

                // Assign the client input sequence to sequence variable.
                sequence = clientInput.readLine();

                // If the program was opened and close without sending "start RNA" close the socket.
                if(sequence == null){
                    System.out.println("Waiting for a new connection....");
                    socket.close();
                    break;
                }

                // Print the input from the client.
                System.out.println("CLIENT: " + sequence);

                // If the disconnect message is received, close the socket and wait for new connection.
                if (sequence.equalsIgnoreCase("disconnect")) {
                    System.out.println("Waiting for a new connection....");
                    socket.close();
                    break;
                }

                /* If the start RNA message is received proceed to accepting input DNA sequence, otherwise
                close the socket if an invalid message was receieved.*/
                if (sequence.equalsIgnoreCase("start rna")) {
                    // Update the serverReady variable to allow DNA string from client.
                    serverReady = true;
                    continue;
                } else if (!sequence.matches("^[ATCGatcg]+$") || sequence.length() % 3 != 0) {
                    System.out.println("Closing socket, invalid DNA sequence received");
                    socket.close();
                    break;
                }
                // Call the optimiseDNA function to optimise DNA sequence for optimal RNA output.
                String optimalSequence = optimiseDNA(sequence);

                // Send the optimal sequence to the client.
                writer.println(optimalSequence);
            }
        }
    }

    /**
     * Function:        getCodons()
     * Description:     Function that takes a DNA string and partitions it into codons and adds
     *                  the codons to a list.
     * @param str       - DNA string to be partitioned.
     * @return          List of codons from the client DNA string.
     */
    // Convert into array of triple nucleotides
    private static List<String> getCodons(String str) {
        List<String> parts = new ArrayList<>();
        int len = str.length();
        for (int i=0; i<len; i+= 3)
        {
            parts.add(str.substring(i, Math.min(len, i + 3)));

        }
        return parts;
    }

    /**
     * Function:     opimtiniseDNA()
     * Description:  A function that breaks DNA into codons, identifies the associated amino acid and
     *               retrieves the optimal codon for the amino acid.
     * @param str    - DNA oinput string from client.
     * @return       Optimised DNA sequence
     */
    private static String optimiseDNA(String str){

        // Call the getCodons function to partition string into codons stored in a list.
        List<String> codonList = getCodons(str);

        // String buffer to store to optimised sequence.
        StringBuilder optimisedSequence = new StringBuilder();

        // Empty string array to store optimal codon conversion.
        String[] optimalCodonList = new String[codonList.size()];

        /* Loop over codon list and identify the associated amino acid and the optimal codon
           representing that amino acid.*/
        for (int i = 0; i < codonList.size(); i++) {

            // Convert codon into optimal codon.
            String optimalCodon = OptimalCodons.valueOf(AminoAcids.valueOf(codonList.get(i)).toString()).toString();

            // Add optimal codon to a list of codons representing the sequence received from the client.
            optimalCodonList[i] = String.valueOf(optimalCodon);
        }

        // Loop over the optimal codon list and add to the stringbuffer to create the optimal string.
        for (String s : optimalCodonList) optimisedSequence.append(s);

        return optimisedSequence.toString();
    }
}