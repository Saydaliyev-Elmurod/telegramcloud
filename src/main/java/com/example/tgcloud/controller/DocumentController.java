package com.example.tgcloud.controller;


import com.example.tgcloud.dto.DocumentDTO;
import com.example.tgcloud.dto.UserDTO;
import com.example.tgcloud.enums.FileType;
import com.example.tgcloud.service.DocumentService;
import com.example.tgcloud.util.MessageUtil;
import com.example.tgcloud.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.*;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class DocumentController {
    private final MessageUtil messageUtil;
    private final DocumentService documentService;

    public void saveDocument(Message message, UserDTO userDTO, FileType type) {
        DocumentDTO document = new DocumentDTO();
        document.setUserId(userDTO.getId());
        if (userDTO.getFolderId() == null) {
            document.setFolderId(userDTO.getDefaultFolderId());
        } else {
            document.setFolderId(userDTO.getFolderId());
        }
        document.setType(type);
        document.setMessageId(message.getMessageId());
        document.setCreatedDate(LocalDateTime.now());
        if (message.getCaption() != null) {
            String caption = message.getCaption().length() > 498 ? message.getCaption().substring(0, 498) : message.getCaption();
            document.setContent(Util.getAdv(caption));
        }
        switch (type) {
            case VIDEO -> {
                Video video = message.getVideo();
                getDocument(userDTO, document, video.getFileName(), video.getFileId(), video.getDuration(), video.getFileSize());
            }
            case AUDIO -> {
                Audio audio = message.getAudio();
                getDocument(userDTO, document, audio.getFileName(), audio.getFileId(), audio.getDuration(), audio.getFileSize());
            }
            case DOCUMENT -> {
                Document doc = message.getDocument();
                if (documentService.getByName(doc.getFileName(), userDTO.getFolderId()) != null) {
                    messageUtil.sendMsg(userDTO.getUserId(), "This file already exist this folder");
                }
                document.setFileId(doc.getFileId());
                if (doc.getFileName() == null) document.setName(doc.getFileId());
                else document.setName(doc.getFileName());
                document.setSize(Util.fileSize(doc.getFileSize()));
            }
            case PHOTO -> {
                PhotoSize photo = message.getPhoto().get(message.getPhoto().size() - 1);
                document.setName(photo.getFileUniqueId());
                document.setFileId(photo.getFileId());
                document.setSize(Util.fileSize((long) photo.getFileSize()));
            }
        }
        documentService.saveDocument(document);
        messageUtil.deleteMsg(message.getMessageId(), userDTO.getUserId());
    }

    private void getDocument(final UserDTO userDTO, final DocumentDTO document, final String fileName, final String fileId, final Integer duration, final Long fileSize) {
        if (documentService.getByName(fileName, userDTO.getFolderId()) != null) {
            messageUtil.sendMsg(userDTO.getUserId(), "This file already exist this folder");
        }
        document.setFileId(fileId);
        document.setDuration(Util.duration(duration));
        if (fileName == null) document.setName(fileId);
        else document.setName(fileName);
        document.setSize(Util.fileSize(fileSize));
    }

    public void createFolder(UserDTO dto, String folder, String parent) {
        DocumentDTO folderDto = new DocumentDTO();
        folderDto.setName(folder);
    }
}
