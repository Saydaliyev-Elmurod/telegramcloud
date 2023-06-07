package com.example.tgcloud.util;

import com.example.tgcloud.MyTelegramBot;
import com.example.tgcloud.dto.UserDTO;
import com.example.tgcloud.enums.FileType;
import com.example.tgcloud.model.DocumentEntity;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


@Component
public class MessageUtil {
    @Autowired
    @Lazy
    private MyTelegramBot myTelegramBot;

    public void sendMsg(Long userId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText(text);
        myTelegramBot.send(message);
    }

    public void sendMsg(Long userId, String text, InlineKeyboardMarkup keyboard, long totalCount, int page) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText(text + " " + page * 8 + "-" + (page + 1) * 8 + "  from " + totalCount);
        message.setReplyMarkup(keyboard);
        myTelegramBot.send(message);
    }

    public void deleteMsg(Integer messageId, Long userId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(userId);
        deleteMessage.setMessageId(messageId);
        myTelegramBot.send(deleteMessage);
    }

    public void sendDocument(String name, DocumentEntity document, UserDTO userDTO, InlineKeyboardMarkup replyDocument) {
        FileType type = document.getType();
        Long userId = userDTO.getUserId();
        String fileId = document.getFileId();
        switch (type) {
            case PHOTO -> sendPhoto(name, fileId, userId, replyDocument);
            case AUDIO -> sendAudio(name, fileId, userId, replyDocument);
            case VIDEO -> sendVideo(name, fileId, userId, replyDocument);
            case DOCUMENT -> sendFile(name, fileId, userId, replyDocument);
        }
    }

    private void sendFile(String name, String fileId, Long userId, InlineKeyboardMarkup replyDocument) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setDocument(new InputFile(fileId));
        sendDocument.setChatId(userId);
        sendDocument.setReplyMarkup(replyDocument);
        sendDocument.setCaption(name);
        myTelegramBot.send(sendDocument);
    }

    private void sendVideo(String name, String fileId, Long userId, InlineKeyboardMarkup replyDocument) {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setVideo(new InputFile(fileId));
        sendVideo.setChatId(userId);
        sendVideo.setReplyMarkup(replyDocument);
        sendVideo.setCaption(name);
        myTelegramBot.send(sendVideo);
    }

    private void sendAudio(String name, String fileId, Long userId, InlineKeyboardMarkup replyDocument) {
        SendAudio sendAudio = new SendAudio();
        sendAudio.setAudio(new InputFile(fileId));
        sendAudio.setChatId(userId);
        sendAudio.setReplyMarkup(replyDocument);
        sendAudio.setCaption(name);
        myTelegramBot.send(sendAudio);
    }

    private void sendPhoto(String name, String fileId, Long userId, InlineKeyboardMarkup replyDocument) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(fileId));
        sendPhoto.setChatId(userId);
        sendPhoto.setReplyMarkup(replyDocument);
        sendPhoto.setCaption(name);
        myTelegramBot.send(sendPhoto);
    }

    public void editMsg(Long userId,Integer msgId, String text, InlineKeyboardMarkup keyboard, long totalCount, int page) {
       try{
           EditMessageText editMessageText = new EditMessageText();
           editMessageText.setChatId(userId);
           editMessageText.setText(text + " " + page * 8 + "-" + (page + 1) * 8 + "  from " + totalCount);
           editMessageText.setReplyMarkup(keyboard);
           editMessageText.setMessageId(msgId);
           myTelegramBot.send(editMessageText);
       }catch (BadRequestException e){

       }
    }
}
