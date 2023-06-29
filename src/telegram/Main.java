package telegram;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        Settings settings = null;
        try {
            settings = new Settings();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Commands commands = null;
        try {
            commands = new Commands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Bot bot = new Bot(settings, commands.getCommands());
        bot.start();
        System.out.println("Бот запущен. Период обновления " + settings.getPeriod());

    }

}


