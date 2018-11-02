package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static final int port = 10252;
    private static String filepath = "text.txt";

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(port)) {
            Socket client = server.accept();
            System.out.print("Connection accepted.");

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (!client.isClosed()) {
                System.out.println("Server reading from channel");
                String entry = in.readLine();
                if (entry.equalsIgnoreCase("quit")) {
                    out.println("Server reply - " + entry + " - OK");
                    out.println("End.");
                    System.out.println("Client initialize connections suicide ...");
                    break;
                } else if (entry.equalsIgnoreCase("who")) {
                    out.println("Server reply - Tanya Zvereva, Variant_10 - Context search");
                    out.println("End.");
                    System.out.println("Server reply - " + "Tanya Zvereva, Variant_10 - Context search");
                } else if (entry.toLowerCase().contains("file to read:")) {
                    filepath = entry.split(":")[1].trim();
                    out.println("Server reply " + entry + " - OK");
                    out.println("End.");
                } else if (entry.toLowerCase().contains("find:")) {
                    String text = entry.split(":")[1].trim();
                    try {
                        HashMap<Integer, Integer> map = fileSearch(filepath, text);

                        if (map.isEmpty()) {
                            out.println("Server reply " + entry + " - no matches");
                            out.println("End.");
                            System.out.println("Server reply " + entry + " - no matches");
                        } else {
                            for (Object o : map.entrySet()) {
                                Map.Entry pair = (Map.Entry) o;
                                System.out.println(String.format("Server reply - '" + text + "' was found at position %d on line %d", pair.getValue(), pair.getKey()));
                                out.println(String.format("Server reply - \"" + text + "\" was found at position %d on line %d", pair.getValue(), pair.getKey()));
                            }
                            out.println("End.");
                        }
                    } catch (IOException e) {
                        out.println("Server reply - file" + filepath + " - isn't exist");
                        out.println("End.");
                        System.out.println("Server reply - file" + filepath + " - isn't exist");
                    }
                } else {
                    out.println("Server reply " + entry + " - False - command doesn't exist");
                    System.out.println("Command " + entry + " doesn't exist");
                }

            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");
            in.close();
            out.close();
            client.close();
            System.out.println("Closing connections & channels - DONE.");

        } catch (IOException e) {
            System.out.println("Oops...Error");
            e.printStackTrace();
        }
    }

    private static HashMap<Integer, Integer> fileSearch(String filename, String textToMatch) throws IOException {
        int lineID = 0;
        String line;
        HashMap<Integer, Integer> map = new HashMap<>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "Windows-1251"));
        while ((line = bufferedReader.readLine()) != null) {
            lineID++;
            if (line.toLowerCase().contains(textToMatch.toLowerCase())) {
                int posFound = line.indexOf(textToMatch);
                map.put(lineID, posFound);
            }
        }
        bufferedReader.close();

        return map;
    }
}
