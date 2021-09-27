package com.company;

import com.company.messages.Gameboard;
import com.company.messages.Move;
import com.company.messages.Position;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

public class TcpClient {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 11122;
    private static Scanner scanner = new Scanner(System.in);

    private static final List<String> types = List.of("1", "2", "3");

    public static void main(String[] args) {
        //Определяем хост сервера и порт
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        try {
            //Создаем сокет для полученной пары хост/порт
            Socket socket = new Socket(host, port);
            System.out.println(socket.toString());
            System.out.println("CONNECTED TO SERVER.\n");
            Player player = new Player(String.valueOf(socket.getPort()));
            // получаем потоки для чтения и записи в сокет
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            Gson gson = new Gson();

            String fromServer = Messaging.readBytes(in);
            Position name = gson.fromJson(fromServer, Position.class);
            System.out.println(name.getPosition());
            player.setName(name.getPosition());

            String card;
            while (!socket.isClosed()) {
                Move move = new Move();
                fromServer = Messaging.readBytes(in);
                Gameboard gameboard = gson.fromJson(fromServer, Gameboard.class);
                String winner = getWinner(gameboard);
                if (winner.equalsIgnoreCase("1")) {
                    System.out.println("Победил игрок 1");
                    break;
                } else if (winner.equalsIgnoreCase("2")) {
                    System.out.println("Победил игрок 2");
                    break;
                }
                player.setCardsFromBoard(gameboard);
                boardInfo(gameboard, player);

                if (gameboard.getCurPlayer().equalsIgnoreCase(player.getName())) {
                    if (!isEmptyBoard(gameboard)) {
                        System.out.println("Верите ли вы второму игроку");
                        System.out.println("Введите Y/N");
                        String input = scanner.nextLine();
                        while (!(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("n"))) {
                            System.out.println("Введите Y/N");
                            input = scanner.nextLine();
                        }
                        if (input.equalsIgnoreCase("y")) {
                            System.out.println("Введите какую карту хотите положить");
                            move.setBelieve(true);
                            card = readType();
                            while (!player.isPossibleToDecrease(card)) {
                                System.out.println("У вас нет карт этой масти! Введите другую");
                                card = readType();
                            }
                            move.setCard(card);

                        } else {
                            //все вычисление делаем на сервере в этом случае
                            move.setBelieve(false);
                            move.setCard(gameboard.getLastCard());
                        }
                    } else {
                        move = emptyBoardMove(player);
                    }
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

    public static String readType() {
        String card = scanner.nextLine();
        while (!types.contains(card)) {
            System.out.println("Введите правильный тип карты");
            card = scanner.nextLine();
        }
        return card;
    }

    public static void boardInfo(Gameboard gameboard, Player player) {
        System.out.println("ВАШИ КАРТЫ: ");
        player.getCards().forEach((key, value) -> {
            System.out.println("масть " + key + " количество  " + value);
        });
        if (gameboard.getBoardCard() != null && !isEmptyBoard(gameboard))
            System.out.println("Масть карты на столе : " + gameboard.getBoardCard());

    }

    public static Move emptyBoardMove(Player player) {
        Move move = new Move();
        System.out.println("Введите какую карту хотите положить");
        move.setBelieve(true);
        String card = readType();
        while (!player.isPossibleToDecrease(card)) {
            System.out.println("У вас нет карт этой масти! Введите другую");
            card = readType();
        }
        move.setCard(card);
        System.out.println("Какую карту называете");
        String toldCard = readType();
        move.setToldCard(toldCard);
        return move;
    }

    public static boolean isEmptyBoard(Gameboard gameboard) {
        boolean isEmpty = true;
        for (Integer number : gameboard.getCards().values()) {
            if (number > 0) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    public static String getWinner(Gameboard gameboard) {
        try {
            boolean isPlayerFirstWin = true;
            boolean isPlayerSecondWin = true;
            for (Integer number : gameboard.getPlayerFirstCards().values()) {
                if (number > 0) {
                    isPlayerFirstWin = false;
                    break;
                }
            }
            for (Integer number : gameboard.getPlayerSecondCards().values()) {
                if (number > 0) {
                    isPlayerSecondWin = false;
                    break;
                }
            }
            if (isPlayerFirstWin)
                return "1";
            else if (isPlayerSecondWin)
                return "2";
            else return "0";
        } catch (NullPointerException e) {
            System.out.println("ОШИБКА ПРИ ЗАГРУЗКЕ ДАННЫХ С СЕРВЕРА");
        }
        return "-1";
    }
}
