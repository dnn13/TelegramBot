package telegram;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Bot {
    private Settings settings;
    private Map<String, String> commands;
    private Gson gson;
    private String url;
    private final String nameFileLastMessage = "LastMessage.txt";

    Bot(Settings settings, Map<String, String> commands){
        this.settings = settings;
        this.commands = commands;
        gson = new Gson();
        url = "https://" + settings.getServer() + ":" + settings.getPort() + "/bot" + settings.getToken();
    }

    public void start(){
        Timer timer = new Timer();
        timer.schedule(new CommandHandler(), 0, settings.getPeriod());
    }

    private void updateLastMessage(int lastMessage) throws IOException {
            PrintWriter writer = new PrintWriter(nameFileLastMessage);
            writer.println(lastMessage);
            writer.close();
        }

    private int getLastMessage() throws IOException {
        int lastMessage = 0;
        Path path = Paths.get(nameFileLastMessage);
        if (!Files.exists(path)){
            Files.createFile(path);
        }
        FileInputStream in = new FileInputStream(nameFileLastMessage);
        Scanner scanner = new Scanner(in);
        if (scanner.hasNextLine()){
            lastMessage = scanner.nextInt();
        }
        scanner.close();
        return lastMessage;
    }

    private HttpsURLConnection getConnection(String url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setRequestProperty("Content-Type", "application/json");
        return connection;
    }

    private Updates getUpdates() throws IOException {
        Updates updates = null;

        int lastMessage = getLastMessage();
        String url = null;
        if (lastMessage > 0){
            url = this.url + "/getUpdates?offset=" + (lastMessage+1);
        } else {
            url = this.url + "/getUpdates";
        }
        HttpsURLConnection connection = getConnection(url);
        int code = connection.getResponseCode();
        if (code == 200) {
            StringBuilder content = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            updates = gson.fromJson(content.toString(), Updates.class);
        }
        return updates;
    }

    private void sendMessage(int chat_id, String text) throws IOException {
        HttpsURLConnection connection = getConnection(url + "/sendMessage?chat_id=" + chat_id + "&text=" + text);
        int code = connection.getResponseCode();
    }

    class CommandHandler extends TimerTask {
        @Override
        public void run() {
            Updates updates = null;
            try {
                updates = getUpdates();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (updates == null) {
                System.out.println("Не удалось получить сообщения с сервера");
                return;
            }

            for (Updates.Result result : updates.result){
                String command = result.getCommand();
                System.out.println("Получена команда " + command);
                String responce = commands.get(command);
                if (responce == null) responce = "bad command";

                try {
                    sendMessage(result.message.from.id, responce);
                    updateLastMessage(result.update_id);
                    System.out.println("Отправлен ответ " + responce);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
