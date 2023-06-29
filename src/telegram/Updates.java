package telegram;

import java.util.List;

public class Updates {
    boolean ok;
    List<Result> result;

    class Result{
        int update_id;
        Message message;

        public String getCommand(){
            String command = "";
            int index = message.text.indexOf(" ");
            if (index >= 0){
                command = message.text.substring(0, index);
            } else{
                command = message.text;
            }
            return command;
        }
    }

    class Message{
        int message_id;
        From from;
        Chat chat;
        int date;
        String text;
    }

    class From{
        int id;
        boolean is_bot;
        String first_name;
        String last_name;
        String username;
        String language_code;
    }

    class Chat{
        int id;
        String first_name;
        String last_name;
        String username;
        String type;
    }
}
