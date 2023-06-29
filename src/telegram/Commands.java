package telegram;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Commands {
    private Map<String, String> commands = new HashMap<>();
    private final String CATALOG = "commands";

    Commands() throws IOException {
        Path path = Paths.get(CATALOG);
        if (!Files.exists(path)){
            Files.createDirectory(path);
        }
        DirectoryStream<Path> files = Files.newDirectoryStream(path);
        for (Path file : files){
            String com = file.getFileName().toString().replace(".txt", "");
            String content = new String(Files.readAllBytes(file), Charset.forName("Cp1251"));
            commands.put(com, content);
        }
    }

    public Map<String, String> getCommands(){
        return commands;
    }
}
