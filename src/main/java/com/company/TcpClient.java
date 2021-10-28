package com.company;

import com.company.messages.GameMsg;
import com.company.messages.Move;
import com.company.messages.Position;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class TcpClient {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 11122;
    private static Scanner scanner = new Scanner(System.in);

    private static final List<String> STRENGTHS = List.of("1", "2", "3", "4", "5", "6", "7", "8");

    public static void main(String[] args) {
        //Определяем хост сервера и порт
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        try {
            //Создаем сокет для полученной пары хост/порт
            Socket socket = new Socket(host, port);
            System.out.println(socket.toString());
            System.out.println("Присоединились к серверу \n");
            Player player = new Player(String.valueOf(socket.getPort()));
            // получаем потоки для чтения и записи в сокет
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            Gson gson = new Gson();

            String fromServer = Messaging.readBytes(in);
            //получаем порядковый номер хода
            Position name = gson.fromJson(fromServer, Position.class);
            System.out.println(name.getPosition());
            player.setName(name.getPosition());

            String card;
            while (!socket.isClosed()) {
                Move move = new Move();
                fromServer = Messaging.readBytes(in);
                GameMsg gameMsg = gson.fromJson(fromServer, GameMsg.class);
                String winner = getWinner(gameMsg, player);
                if (winner.equalsIgnoreCase("1")) {
                    System.out.println("Победил игрок 1");
                    break;
                } else if (winner.equalsIgnoreCase("2")) {
                    System.out.println("Победил игрок 2");
                    break;
                } else if (winner.equalsIgnoreCase("3"))
                    System.out.println("Ничья");
                player.setStrengthFromGame(gameMsg);
                logInfo(gameMsg, player);


                if (gameMsg.getCurPlayer().equalsIgnoreCase(player.getName())) {
                    move.setStrength(readType(player));

                    //отправка результата хода на сервер
//                    System.out.println(gson.toJson(move));
                    Messaging.writeBytes(out, gson.toJson(move));
                }

            }

        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост: " + host);
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static String readType(Player player) {
        System.out.println("Введите число от 1 до 8, которое вы не вводили до этого");
        String strength = scanner.nextLine();
        HashSet<String> strengthSet = new HashSet<>();
        for (Character i : player.getStrength().toCharArray())
            strengthSet.add(String.valueOf(i));
        while (!STRENGTHS.contains(strength) || strengthSet.contains(strength)) {
            System.out.println("Введите число от 1 до 8, которое вы не вводили до этого");
            strength = scanner.nextLine();
        }
        return strength;
    }

    public static void logInfo(GameMsg gameMsg, Player player) {
        System.out.println("ВАШИ СИЛЫ: ");
        for (int i = 0; i < player.getStrength().length(); i++)
            System.out.println("   ( •_•)⠀\n" +
                    +Character.getNumericValue(player.getStrength().charAt(i)) + "  ( ง )ง\n" +
                    "   /︶\\");


    }


    public static String getWinner(GameMsg gameMsg, Player player) {
        int playerFirstPoints = 0;
        int playerSecondPoints = 0;
        if (gameMsg.getPlayerFirst().length() == 8 && gameMsg.getPlayerSecond().length() == 8) {
            for (int i = 0; i < 8; i++) {
                int playerFirstStrength = gameMsg.getPlayerFirst().charAt(i);
                int playerSecondStrength = gameMsg.getPlayerSecond().charAt(i);
                if (playerFirstStrength > playerSecondStrength)
                    playerFirstPoints += 1;
                else if (playerFirstStrength < playerSecondStrength)
                    playerSecondPoints += 1;
            }
            if (player.getName().equalsIgnoreCase("1")) {
                drawBattle(gameMsg.getPlayerFirst(), gameMsg.getPlayerSecond());
            } else {
                drawBattle(gameMsg.getPlayerSecond(), gameMsg.getPlayerFirst());
            }
            if (playerFirstPoints > playerSecondPoints)
                return "1";
            else if (playerFirstPoints < playerSecondPoints)
                return "2";
            else return "3";
        } else
            return "0";
    }

    public static void drawBattle(String leftPlayer, String rightPlayer) {
        for (int i = 0; i < 8; i++) {
            System.out.println("     ( •_•)     (•_• )     \n" +
                    leftPlayer.charAt(i) + "    ( ง )ง     ୧( ୧ )     " + rightPlayer.charAt(i) +"\n"+
                    "     /︶\\        /︶\\     ");
        }
    }
}
