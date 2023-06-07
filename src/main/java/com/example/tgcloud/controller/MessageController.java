package com.example.tgcloud.controller;


import com.example.tgcloud.MyTelegramBot;
import com.example.tgcloud.dto.UserDTO;
import com.example.tgcloud.enums.FileType;
import com.example.tgcloud.enums.UserStep;
import com.example.tgcloud.service.DocumentService;
import com.example.tgcloud.service.UserService;
import com.example.tgcloud.util.ButtonUtil;
import com.example.tgcloud.util.MessageUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
@AllArgsConstructor
public class MessageController {
    @Lazy
    private final MyTelegramBot myTelegramBot;
    private final UserService userService;
    private final ButtonUtil buttonUtil;
    private final MessageUtil messageUtil;
    private final DocumentController documentController;
    private final DocumentService documentService;


    public void start(Update update) {
        Long userId = update.getMessage().getChatId();
        UserDTO dto = userService.getUser(userId);
        Message message = update.getMessage();
        if (message.hasText()) {
            String text = message.getText();
            if (text.equals("/start")) {
                startBot(userId, dto);
            } else if (dto.getStep().equals(UserStep.CREATE_NEW_FOLDER)) {
                createFolder(text, dto);
            } else if (dto.getStep().equals(UserStep.RENAME_FILE)) {
                rename(text, dto);
            }
        }
        if (message.hasViaBot()) {
            return;
        } else if (message.hasAudio()) {
            documentController.saveDocument(message, dto, FileType.AUDIO);
        } else if (message.hasDocument()) {
            documentController.saveDocument(message, dto, FileType.DOCUMENT);
        } else if (message.hasPhoto()) {
            documentController.saveDocument(message, dto, FileType.PHOTO);
        } else if (message.hasVideo()) {
            documentController.saveDocument(message, dto, FileType.VIDEO);
        }

    }

    private void rename(String text, UserDTO dto) {
        Integer docId = dto.getRenameDocId();
        documentService.rename(docId, text, dto);
    }

    private void createFolder(String text, UserDTO dto) {
        documentService.createFolder(text, dto);
    }

    private void startBot(Long userId, UserDTO dto) {
        if (dto == null) {
            dto = userService.saveUser(userId);
        } else {
            dto.setFolderId(null);
            userService.updateUserFolder(dto);
        }
        documentService.start(dto);
    }
}
