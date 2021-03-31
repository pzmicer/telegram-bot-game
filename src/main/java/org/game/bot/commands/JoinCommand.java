package org.game.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.game.bot.Rooms;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@BotCommand(name="join")
@Slf4j
public class JoinCommand extends Command {
    public JoinCommand(String args) {
        super(args);
    }

    @Override
    public SendMessage execute(AbsSender sender, User user) {
        Long chatID = user.getId();
        if (Rooms.checkUser(chatID) != - 1) {
            return ReplyMessageService.getReplyMessage(chatID, "joinException");
        }
        try {
            int roomID = Integer.parseInt(args);
            if (!Rooms.checkRoom(roomID))
                return ReplyMessageService.getReplyMessage(chatID, "invalidArgs");
            for(var id : Rooms.getRoom(roomID)) {
                sender.execute(ReplyMessageService.getReplyMessage(id, "joinNotification", user.getUserName()));
            }
            Rooms.addUser(roomID, chatID);
            return ReplyMessageService.getReplyMessage(chatID, "joinPerson", roomID);
        } catch (NumberFormatException e) {
            return ReplyMessageService.getReplyMessage(chatID, "invalidArgs");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return ReplyMessageService.getReplyMessage(chatID, "error");
        }
    }
}