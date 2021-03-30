package org.game.bot.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.glassfish.grizzly.utils.Pair;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Slf4j
public class Command {
    CommandType command = CommandType.NONE;
    String text= "";

    //PARSE SECTION

    private static String PREFIX_FOR_COMMAND ="/";

    public static Command parseCommand(String text) throws ParseException {
        if (text == null)
            return new Command(CommandType.NONE, "");
        String trimText = text.trim();
        if (trimText.startsWith("/")) {
            int spaceIndex = trimText.indexOf(' ');
            String command = trimText.substring(1, spaceIndex).toUpperCase();
            String message = trimText.substring(spaceIndex + 1);
            try {
                return new Command(CommandType.valueOf(command), message);
            } catch (IllegalArgumentException e) {
                log.error("Can't parse command: " + text);
                throw new ParseException();
            }
        }
        return new Command(CommandType.NONE, trimText);
    }
}
