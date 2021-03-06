package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Date;


public class Client {

    public static void main(String[] args) {
        String lineSeparator = System.getProperty("line.separator");
        try (Socket socket = new Socket("localhost", 10252);
             BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             FileWriter writer = new FileWriter("report.txt", true)) {

            System.out.println("Client connected to socket.");
            while (!socket.isOutputShutdown()) {
                System.out.println("Client start writing in channel...");
                String clientCommand = keyboard.readLine();
                out.println(clientCommand);
                System.out.println("Client sent message \"" + clientCommand + "\" to server.");
                writer.write((new Date()).toString() + " : " + "Client sent message \"" + clientCommand + "\" to server." + lineSeparator);
                writer.flush();
                if (clientCommand.equalsIgnoreCase("quit")) {
                    System.out.println("Client kill connections");
                    String sin = in.readLine();
                    System.out.println(sin);
                    break;
                }
                String sin;
                while (!(sin = in.readLine()).equals("End.")) {
                    System.out.println(sin);
                }
            }
            System.out.println("Closing connections & channels on clientSide - DONE.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
