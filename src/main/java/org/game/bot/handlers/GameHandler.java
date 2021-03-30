package org.game.bot.handlers;

import org.game.bot.GameTelegramBot;
import org.game.bot.command.Command;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class GameHandler extends Handler {
    private GameTelegramBot bot;

    public GameHandler(GameTelegramBot bot, ReplyMessageService replyMessageService) {
        super(replyMessageService);
        this.bot = bot;
    }

    //TODO Handle Game(Lobby) commands (/join, /guess, ...)

    @Override
    public SendMessage handle(String chatId, Command command, Update update) {
        return null;
    }
}
