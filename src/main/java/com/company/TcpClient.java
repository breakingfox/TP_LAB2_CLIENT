package com.company;

import com.company.messages.Gameboard;
import com.company.messages.Move;
import com.google.gson.Gson;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 11122;

    public static void main(String[] args) {
        //Определяем хост сервера и порт
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            host = args[0];
        }
        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        try {
            //Создаем сокет для полученной пары хост/порт
            Socket socket = new Socket(host, port);

            System.out.println("CONNECTED TO SERVER.\n");

            // получаем потоки для чтения и записи в сокет
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            Console console = System.console();
            
            while (true) {
                String fromServer = Messaging.readBytes(in);
                Gson gson = new Gson();
                Gameboard gameboard = gson.fromJson(fromServer, Gameboard.class);
                gameboard.getCards().forEach((key, value) -> {
                    System.out.println(" - :" + key + " " + value);
                });
                //TODO будущая проверка на то какой пользователь продолжает игру
//                if(gameboard.getCurPlayer() == player)
                Move move = new Move();
                System.out.println("Верите ли вы второму игроку");
                System.out.println("Введите Y/N");
                String input = console.readLine();
                if (input.equalsIgnoreCase("y")) {
                    System.out.println("Введите какую карту хотите положить");
                    move.setBelieve(true);

                    String card = console.readLine();
                    move.setCard(card);
                    //TODO проверки на корректный ввод
                    System.out.println("Какую карту называете");
                    String toldCard = console.readLine();
                    move.setToldCard(toldCard);
                } else {
                    //все вычисление делаем на сервере в этом случае
                    move.setBelieve(false);

                }
                //отправка результата хода на сервер
                System.out.println(gson.toJson(move));
                Messaging.writeBytes(out, gson.toJson(move));


            }

        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост: " + host);
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
