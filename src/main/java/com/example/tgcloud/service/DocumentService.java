package com.example.tgcloud.service;


import com.example.tgcloud.dto.DocumentDTO;
import com.example.tgcloud.dto.UserDTO;
import com.example.tgcloud.enums.FileType;
import com.example.tgcloud.enums.UserStep;
import com.example.tgcloud.model.DocumentEntity;
import com.example.tgcloud.repository.DocumentRepository;
import com.example.tgcloud.repository.UserRepository;
import com.example.tgcloud.util.ButtonUtil;
import com.example.tgcloud.util.MessageUtil;
import com.example.tgcloud.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    private MessageUtil messageUtil;
    @Autowired
    private ButtonUtil buttonUtil;
    @Autowired
    private UserRepository userRepository;

    public void saveDocument(DocumentDTO dto) {
        DocumentEntity entity = new DocumentEntity();
        entity.setFileId(dto.getFileId());
        entity.setFolderId(dto.getFolderId());
        entity.setContent(dto.getContent());
        entity.setDuration(dto.getDuration());
        entity.setType(dto.getType());
        entity.setSize(dto.getSize());
        entity.setName(dto.getName());
        entity.setUser_id(dto.getUserId());
//        entity.setUser(userRepository.findById(dto.getUserId()).get());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setMessageId(dto.getMessageId());
        documentRepository.save(entity);
    }

    public void start(UserDTO dto) {
        DocumentEntity folder = documentRepository.getCurrentFolder(dto.getId(), dto.getFolderId());
        boolean back = true;
        if (dto.getFolderId() == null) {
            back = false;
        }
        String name = null;

        if (folder == null) {
            name = "My Telegram Cloud";
        } else {
            name = folder.getName();
        }
        // update dto
        dto.setIsClickButton(true);
        userService.update(dto);
        // send msg
        Page<DocumentEntity> documentList = documentRepository.getByUserIdAndFolderId(dto.getId(), null, Util.pageableDocument(dto));
        messageUtil.sendMsg(dto.getUserId(), name,
                buttonUtil.replyButton(toList(documentList.getContent()), documentList.hasNext(), documentList.hasPrevious(), back), documentList.getTotalElements(), documentList.getPageable().getPageNumber());
    }

    public void sendButton(UserDTO dto) {
        DocumentEntity folder = documentRepository.getCurrentFolder(dto.getId(), dto.getFolderId());
        Boolean back = true;
        if (dto.getFolderId() == null) {
            back = false;
        }
        Page<DocumentEntity> documentList = documentRepository.getByUserIdAndFolderId(dto.getId(), dto.getFolderId(), Util.pageableDocument(dto));
        messageUtil.sendMsg(dto.getUserId(), folder == null ? "" : folder.getName(),
                buttonUtil.replyButton(toList(documentList.getContent()), documentList.hasNext(), documentList.hasPrevious(), back), documentList.getTotalElements(), documentList.getPageable().getPageNumber());
    }

    public void editButton(UserDTO dto) {
        DocumentEntity folder = documentRepository.getCurrentFolder(dto.getId(), dto.getFolderId());
        Boolean back = true;
        if (dto.getFolderId() == null) {
            back = false;
        }
        Page<DocumentEntity> documentList = documentRepository.getByUserIdAndFolderId(dto.getId(), dto.getFolderId(), Util.pageableDocument(dto));
        messageUtil.editMsg(dto.getUserId(), dto.getCurrentMessageId(), folder == null ? "" : folder.getName(),
                buttonUtil.replyButton(toList(documentList.getContent()), documentList.hasNext(), documentList.hasPrevious(), back), documentList.getTotalElements(), documentList.getPageable().getPageNumber());
    }

    public List<DocumentEntity> getDocList(Long u_id) {
        return documentRepository.getDoc(u_id);
    }


    private DocumentDTO toDTO(DocumentEntity entity) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser_id());
        dto.setName(entity.getName());
        dto.setSize(entity.getSize());
        dto.setDuration(entity.getDuration());
        dto.setFolderId(entity.getFolderId());
        dto.setType(entity.getType());
        dto.setContent(entity.getContent());
        dto.setFileId(entity.getFileId());
        dto.setMessageId(entity.getMessageId());
        return dto;
    }

    private List<DocumentDTO> toList(List<DocumentEntity> entityList) {
        List<DocumentDTO> dtoList = new ArrayList<>();
        entityList.forEach(entity -> dtoList.add(toDTO(entity)));
        return dtoList;
    }

    public void createFolder(String text, UserDTO dto) {
        DocumentEntity folder = new DocumentEntity();
        folder.setName(text);
        folder.setFolderId(dto.getFolderId());
        folder.setUser_id(dto.getId());
        folder.setType(FileType.FOLDER);
        documentRepository.save(folder);
        // update user status to free
        dto.setStep(UserStep.FREE);
        // user ga button send qilindi u buttonni bossa msg id ol
        dto.setIsClickButton(true);
        userService.update(dto);

        sendButton(dto);
    }

    public void signInFolder(Integer folderId, UserDTO user) {
        user.setFolderId(folderId);
        userService.updateUserFolder(user);
        // edit msg
        editButton(user);
    }

    public void getDocument(Integer documentId, UserDTO userDTO) {
        DocumentEntity document = documentRepository.findById(documentId).get();
        FileType type = document.getType();
        String name = null;
        switch (type) {
            case VIDEO, AUDIO -> {
                if (document.getContent() != null) {
                    name = document.getName() + "  " + document.getSize() + "  " + document.getDuration() + "\n" + document.getContent();
                } else {
                    name = document.getName() + "  " + document.getSize() + "  " + document.getDuration();
                }
            }
            case PHOTO, DOCUMENT -> {
                if (document.getContent() != null) {
                    name = document.getName() + "  " + document.getSize() + "  ";
                } else {
                    name = document.getName() + "  " + document.getSize() + "  ";
                }
            }
        }
        messageUtil.sendDocument(name, document, userDTO, buttonUtil.replyDocument(documentId));
    }

    public void prevDocuments(Long userId) {
        UserDTO dto = userService.getUser(userId);
        dto.setCurrentDecimalDoc(dto.getCurrentDecimalDoc() - 1);
        userService.updateUserDecimal(dto);
        editButton(dto);
    }

    public void nextDocuments(Long userId) {
        UserDTO dto = userService.getUser(userId);
        dto.setCurrentDecimalDoc(dto.getCurrentDecimalDoc() + 1);
        userService.updateUserDecimal(dto);
        editButton(dto);
    }

    public void delete(Integer documentId, Long userId) {
        documentRepository.deleteById(documentId);
    }

    public void rename(Integer docId, String text, UserDTO dto) {
        //rename doc
        documentRepository.updateName(text, docId);
        //send button
        sendButton(dto);
        // update user step to free
        dto.setStep(UserStep.FREE);
        dto.setIsClickButton(true);
        userService.update(dto);
    }

    public List<DocumentEntity> getDocQuery(String query, Long userId) {

        return documentRepository.getDocQuery("%" + query.toLowerCase() + "%", userId, userService.getUser(userId).getFolderId());
    }

    public DocumentEntity getByName(String fileName, Integer folderId) {
        return documentRepository.getByName(fileName, folderId);
    }
}
