package com.example.tgcloud;

import com.example.tgcloud.config.BotConfig;
import com.example.tgcloud.controller.CallBackQueryController;
import com.example.tgcloud.controller.InlineQueryController;
import com.example.tgcloud.controller.MessageController;
import com.example.tgcloud.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class MyTelegramBot extends TelegramLongPollingBot {
    @Autowired
    private BotConfig config;
    @Autowired
    @Lazy
    private CallBackQueryController callBackQueryController;
    @Autowired
    @Lazy
    private MessageController messageController;
    @Autowired
    @Lazy
    private InlineQueryController inlineQueryController;





    public MyTelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
        listOfCommands.add(new BotCommand("/settings", "set your preferences"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
         log.error(String.valueOf(e.getCause()));
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
//        if (update.hasMessage()) {
//            Message message = update.getMessage();
//            Long chatId = message.getChatId();
//            forwardMessagesToUser(chatId, chatId, message.getMessageId()-1);
//        }

        //____________________________________________________
        if (update.hasMessage()) {
            messageController.start(update);
        } else if (update.hasCallbackQuery()) {
            callBackQueryController.start(update);
        } else if (update.hasInlineQuery()) {
            inlineQueryController.start(update);
        }
    }
    public void forwardMessagesToUser(Long fromChatId, Long toChatId, int messageId) {
        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setChatId(toChatId);
        forwardMessage.setFromChatId(5813334324l);
        forwardMessage.setMessageId(messageId);

        try {
            execute(forwardMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public Message send(Object object) {
        try {
            if (object instanceof SendMessage) {
                return execute((SendMessage) object);
            } else if (object instanceof EditMessageText) {
                execute((EditMessageText) object);
            } else if (object instanceof SendPhoto) {
                return execute((SendPhoto) object);
            } else if (object instanceof SendVideo) {
                return execute((SendVideo) object);
            } else if (object instanceof SendDocument) {
                return execute((SendDocument) object);
            } else if (object instanceof SendVoice) {
                return execute((SendVoice) object);
            } else if (object instanceof DeleteMessage) {
                execute((DeleteMessage) object);
            } else if (object instanceof SendAudio) {
                execute((SendAudio) object);
            } else if (object instanceof AnswerInlineQuery) {
                execute((AnswerInlineQuery) object);
            }
        } catch (TelegramApiRequestException e) {
            log.error(e.getMessage());
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}


