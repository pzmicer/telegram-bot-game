package org.game.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.utils.Pair;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandParser {
    private final String PREFIX_FOR_COMMAND ="/";

    public Command parseCommand(String text) {
        String trimText = text.trim();
        Command result = new Command(CommandType.NONE, trimText);
        if (trimText.isEmpty())
            return result;
        Pair<String, String> commandText = getDelimCommandFromText(trimText);
        if(isCommand(commandText.getFirst())) {
            CommandType commandForParse = getCommandFromText(commandText.getFirst());
            result.setText(commandText.getSecond());
            result.setCommand(commandForParse);
        }
        return result;
    }

    private CommandType getCommandFromText(String text) {
        String upperCase = text.substring(1).toUpperCase().trim();
        CommandType commandType = CommandType.NONE;
        try {
            commandType = CommandType.valueOf(upperCase);
        } catch (IllegalArgumentException e) {
            log.debug("Can't parse command: " + text);
        }
        return commandType;
    }

    private boolean isCommand(String text) {
        return text.startsWith(PREFIX_FOR_COMMAND);
    }

    private Pair<String, String> getDelimCommandFromText(String trimText) {
        Pair<String, String> commandText;
        if (trimText.contains(" ")) {
            int indexOfSpace = trimText.indexOf(" ");
            commandText = new Pair<>(trimText.substring(0, indexOfSpace), trimText.substring(indexOfSpace + 1));
        } else commandText = new Pair<>(trimText, "");
        return commandText;
    }
}
