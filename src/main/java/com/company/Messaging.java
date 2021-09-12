package com.company;

import java.io.*;

public class Messaging {

    public static String readBytes(java.io.InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            int length = reader.read();

            String string = "";
            for (int i = 0; i < length; ++i) {
                string += (char) reader.read();
            }

            return string;
        } catch (IOException ex) {
            System.out.println("I/O Error!");
        }
        return null;
    }

    public static void writeBytes(java.io.OutputStream stream, String string) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
            writer.write(string.length());
            writer.write(string);
            writer.flush();
        } catch (IOException ex) {
            System.out.println("I/O Error!");
        }
    }

    public static String readString(java.io.InputStream stream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            return br.readLine();
        } catch (IOException ex) {
            System.out.println("I/O Error!");
        }
        return null;
    }

    public static void writeString(java.io.OutputStream stream, String string) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
            writer.write(string + "\n");
            writer.flush();
        } catch (IOException ex) {
            System.out.println("I/O Error!");
        }
    }
}
