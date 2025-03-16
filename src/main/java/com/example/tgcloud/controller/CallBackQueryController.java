package com.example.tgcloud.controller;


import com.example.tgcloud.MyTelegramBot;
import com.example.tgcloud.dto.UserDTO;
import com.example.tgcloud.enums.UserStep;
import com.example.tgcloud.service.DocumentService;
import com.example.tgcloud.service.UserService;
import com.example.tgcloud.util.MessageUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
@AllArgsConstructor
public class CallBackQueryController {
    private final UserService userService;
    private final DocumentService documentService;
    private final MessageUtil messageUtil;

    public void start(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long userId = callbackQuery.getFrom().getId();
        String data = callbackQuery.getData();
        // get user and set message id
        UserDTO dto = userService.getUser(userId);
        if (dto.getCurrentMessageId() == null || dto.getIsClickButton()) {
            dto.setCurrentMessageId(callbackQuery.getMessage().getMessageId());
            dto.setIsClickButton(false);
            userService.update(dto);
        }

        if (data.equals("createFolder")) {
            createFolder(dto);
        } else if (data.startsWith("f/")) {
            signInFolder(data, dto);
        } else if (data.startsWith("d/")) {
            getDocument(data, dto);
        } else if (data.equals("back")) {
            backFolder(userId);
        } else if (data.equals("next")) {
            nextDocuments(userId);
        } else if (data.equals("prev")) {
            prevDocuments(userId);
        } else if (data.equals("date")) {
            sortByDate(userId);
        } else if (data.equals("name")) {
            sortByName(userId);
        } else if (data.equals("size")) {
            sortBySize(userId);
        } else if (data.startsWith("rename/")) {
            rename(data, dto);
        } else if (data.startsWith("delete/")) {
            delete(data, dto, update.getCallbackQuery().getMessage().getMessageId());
        }
    }

    private void delete(String data, UserDTO userDTO, Integer msgId) {
        //delete from tg
        messageUtil.deleteMsg(msgId, userDTO.getUserId());

        // delete from database
        Integer documentId = Integer.valueOf(data.substring(7));
        documentService.delete(documentId, userDTO.getUserId());

        // edit msg
        documentService.editButton(userDTO);
    }

    private void rename(String data, UserDTO userDTO) {
        //send msg to user that enter new name
        messageUtil.sendMsg(userDTO.getUserId(), "Enter new name document");
        // find renamed doc
        Integer documentId = Integer.valueOf(data.substring(7));

        //  profile set step RENAME DOC
        userDTO.setRenameDocId(documentId);
        userDTO.setStep(UserStep.RENAME_FILE);
        userService.update(userDTO);
    }

    private void sortBySize(Long userId) {
        userService.sortBySize(userId);
    }

    private void sortByName(Long userId) {
        userService.sortByName(userId);
    }

    private void sortByDate(Long userId) {
        userService.sortByDate(userId);
    }

    private void prevDocuments(Long userId) {
        documentService.prevDocuments(userId);
    }

    private void nextDocuments(Long userId) {
        documentService.nextDocuments(userId);
    }

    private void backFolder(Long userId) {
        userService.backFolder(userId);
    }

    private void getDocument(String data, UserDTO userDTO) {
        Integer documentId = Integer.valueOf(data.substring(2));
        documentService.getDocument(documentId, userDTO);
    }

    private void signInFolder(String data, UserDTO userDTO) {
        Integer folderId = Integer.valueOf(data.substring(2));

        documentService.signInFolder(folderId, userDTO);
    }

    private void createFolder(UserDTO userDTO) {
        messageUtil.sendMsg(userDTO.getUserId(), "Enter name folder");

        userDTO.setStep(UserStep.CREATE_NEW_FOLDER);
        userService.updateUserStep(userDTO);
    }
}
