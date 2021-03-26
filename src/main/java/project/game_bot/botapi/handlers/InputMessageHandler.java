package project.game_bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import project.game_bot.botapi.BotState;

public interface InputMessageHandler {
    SendMessage handle(Message message);
    BotState getHandlerName();
}
