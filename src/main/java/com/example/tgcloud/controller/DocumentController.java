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
        if (message.getCaption() != null){
            String caption = message.getCaption().length()>498?message.getCaption().substring(0,498):message.getCaption();
            document.setContent(Util.getAdv(caption));
        }
        switch (type) {
            case VIDEO -> {
                Video video = message.getVideo();
                if (documentService.getByName(video.getFileName(), userDTO.getFolderId()) != null) {
                    messageUtil.sendMsg(userDTO.getUserId(), "This file already exist this folder");
                }
                document.setFileId(video.getFileId());
                document.setDuration(Util.duration(video.getDuration()));
                if (video.getFileName() == null) document.setName(video.getFileId());
                else document.setName(video.getFileName());
                document.setSize(Util.fileSize(video.getFileSize()));
            }
            case AUDIO -> {
                Audio audio = message.getAudio();
                if (documentService.getByName(audio.getFileName(), userDTO.getFolderId()) != null) {
                    messageUtil.sendMsg(userDTO.getUserId(), "This file already exist this folder");
                }
                document.setFileId(audio.getFileId());
                document.setDuration(Util.duration(audio.getDuration()));
                if (audio.getFileName() == null) document.setName(audio.getFileId());
                else document.setName(audio.getFileName());
                document.setSize(Util.fileSize(audio.getFileSize()));
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

    public void createFolder(UserDTO dto, String folder, String parent) {
        DocumentDTO folderDto = new DocumentDTO();
        folderDto.setName(folder);

    }
}
