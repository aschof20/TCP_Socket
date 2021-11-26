## TCP Socket
___
#### ZIP file contents
- readme.md
- Client.java
- Server.java
- AminoAcids.java
- OptimalCodons.java
- startServer.sh
- startClient.sh

## Instructions
___
**NOTE: The Server must be started prior to the client.**

#### Server instructions
___
**The startServer.sh, Server.java, AminoAcids.java and OptimalCodons.java must be in the same directory.**

- The RNA Optimisation server is started by running "./startServer.sh [PORT]" in the terminal.

- The server will remain open until it provides an invalid DNA string to the client or until its forced termination is commanded with a keyboard interrupt (usually CTRL+C).
- If the client provides an invalid DNA sequence then the server with close the socket and remain open for an new connection. 

#### Client instructions
___
**The startClient.sh, Client.java files must be in the same directory.**

- The RNA Optimisation client is started by running "./startClient.sh [HOST] [PORT]" in the terminal.

- A user interface will be displayed were the user can choose between the following options:
    1. RNA Optimisation
    2. Exit

    Option **[1]** will be prompted to input a DNA sequence to be sent to the server.
    Option **[2]** will close the socket and end the client session.

##### Client Input
Valid client input must have the following characteristics:

- A length divisable by 3 (the length of a codon).
- Contain only 'A', 'C', 'G' or 'T' characters that represent the DNA nucleotides.

 
