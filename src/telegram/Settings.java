package telegram;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Settings {

    private String server;
    private int port;
    private String token;
    private int period;

    private final String SERVER_DEFAULT = "api.telegram.org";
    private final int PORT_DEFAULT = 443;
    private final int PERIOD_DEFAULT = 5000;
    private final String FILENAME = "settings.txt";

    Settings() throws IOException {
        Path path = Paths.get(FILENAME);
        if (!Files.exists(path)){
            createFile();
        }
        List<String> lines = Files.readAllLines(path);
        for (String line : lines){
            int index = line.indexOf(" ");
            if (index >= 0){
                String key = line.substring(0, index);
                String value = line.substring(index).trim();
                switch (key){
                    case "server":
                        server = value;
                        break;
                    case "port":
                        port = Integer.parseInt(value);
                        break;
                    case "period":
                        period = Integer.parseInt(value);;
                        break;
                    case "token":
                        token = value;
                        break;
                }
            }
        }
    }

    private void createFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(FILENAME);
        writer.println("server " + SERVER_DEFAULT);
        writer.println("port " + PORT_DEFAULT);
        writer.println("period " + PERIOD_DEFAULT);
        writer.println("token ");
        writer.close();
    }

    public String getServer(){
        return server;
    }

    public int getPort(){
        return port;
    }

    public String getToken(){
        return token;
    }

    public int getPeriod(){
        return period;
    }
}
