package org.game.bot.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.A;
import org.game.bot.models.Association;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.game.bot.service.RoomService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class CountdownTask implements Runnable {
    private AtomicInteger currentTime;
    private volatile ScheduledFuture<?> self;

    private final Room room;
    private final int repeats;
    private final User user;
    private final String word;
    private final Association association;
    private final User associationCreator;
    private final AbsSender sender;
    private final ReplyMessageService messageService;
    private final RoomService roomService;

    private final HashMap<User, Message> messages = new HashMap<>();

    @SneakyThrows
    @Override
    public void run() {
        if (currentTime.get() == repeats) {
            for (var _user : room.getUsers())
                sender.execute(messageService.getMessage(_user,"guessNotification",
                        user.getUserName(), associationCreator.getUserName()));
        }
        if (currentTime.get() == 0) {
            room.setCountdown(false);

            if (word.equals(association.getWord())) {
                room.getAssociations().clear();
                String newPrefix = roomService.openNextLetter(room);
                if (newPrefix.equals(room.getKeyword())) {
                    roomService.endGame(room);
                }
                for(var _user : room.getUsers()) {
                    sender.execute(messageService.getMessage(_user, "playersGuessed",
                            user.getUserName(), associationCreator.getUserName(), word));
                    if (!room.isInGame()) {
                        sender.execute(messageService.getMessage(_user, "keywordGuessed", user.getUserName(), association.getWord()));
                        sender.execute(messageService.getMessage(_user, "endGame"));
                    }
                    else {
                        sender.execute(messageService.getMessage(_user, "currentWord", newPrefix));
                    }
                }
            } else {
                sender.execute(messageService.getMessage(user, "guessFailure"));
            }

            boolean interrupted = false;
            try {
                while(self == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                }
                self.cancel(false);
            } finally {
                if(interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            for(var user : room.getUsers()) {
                if (currentTime.get() == repeats) {
                    messages.put(user, sender.execute(new SendMessage(user.getId().toString(), currentTime.toString())));
                } else {
                    EditMessageText editMsg = new EditMessageText();
                    editMsg.setChatId(user.getId().toString());
                    editMsg.setMessageId(messages.get(user).getMessageId());
                    editMsg.setText(String.valueOf(currentTime));
                    sender.execute(editMsg);
                }
            }
            currentTime.decrementAndGet();
        }
    }

    public ScheduledFuture<?> start(ScheduledExecutorService executor, long period, TimeUnit unit) {
        currentTime = new AtomicInteger(repeats);
        self = executor.scheduleAtFixedRate(this, 0, period, unit);
        return self;
    }
}
