package com.example.tgcloud.service;


import com.example.tgcloud.dto.UserDTO;
import com.example.tgcloud.enums.FileType;
import com.example.tgcloud.enums.SortType;
import com.example.tgcloud.model.DocumentEntity;
import com.example.tgcloud.model.UserEntity;
import com.example.tgcloud.repository.DocumentRepository;
import com.example.tgcloud.repository.UserRepository;
import com.example.tgcloud.util.ButtonUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final DocumentRepository documentRepository;
    private final DocumentService documentService;
    public UserDTO getUser(Long userId) {
        UserEntity entity = repository.findByUserId(userId);
        if (entity == null) return null;
        return toDto(entity);
    }

    private UserDTO toDto(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setStep(entity.getStep());
        dto.setFolderId(entity.getFolderId());
        dto.setDefaultFolderId(entity.getDefaultFolderId());
        dto.setCurrentDecimalDoc(entity.getCurrentDecimalDoc());
        dto.setSortType(entity.getSortType());
        dto.setCurrentMessageId(entity.getCurrentMessageId());
        dto.setRenameDocId(entity.getRenameDocId());
        dto.setIsClickButton(entity.getIsClickButton());
        return dto;
    }

    private UserEntity toEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setStep(dto.getStep());
        entity.setDefaultFolderId(dto.getDefaultFolderId());
        entity.setFolderId(dto.getFolderId());
        entity.setCurrentDecimalDoc(dto.getCurrentDecimalDoc());
        entity.setSortType(dto.getSortType());
        entity.setCurrentMessageId(dto.getCurrentMessageId());
        entity.setRenameDocId(dto.getRenameDocId());
        entity.setIsClickButton(dto.getIsClickButton());
        return entity;
    }

    public UserDTO saveUser(Long userId) {
        UserEntity entity = new UserEntity();
        entity.setUserId(userId);
        entity = repository.save(entity);

        DocumentEntity folder = new DocumentEntity();
        folder.setName("My Files ");
        folder.setFolderId(null);
        folder.setType(FileType.FOLDER);
        folder.setUser_id(entity.getId());
        folder = documentRepository.save(folder);

        entity.setDefaultFolderId(folder.getId());
//        entity.setFolder(folder);
        repository.save(entity);

        return toDto(entity);
    }

    public void updateUserStep(UserDTO userDTO) {
        repository.updateUserStep(userDTO.getStep(), userDTO.getId());
    }

    public void updateUserDecimal(UserDTO userDTO) {
        repository.updateUserDecimal(userDTO.getCurrentDecimalDoc(), userDTO.getId());
    }

    public void updateUserSortType(UserDTO userDTO) {
        repository.updateUserSortType(userDTO.getSortType(), userDTO.getId());
    }

    public void updateUserFolder(UserDTO dto) {
        repository.updateUserFolder(dto.getFolderId(), dto.getId());
    }

    public void update(UserDTO dto) {
        repository.save(toEntity(dto));
    }

    public void backFolder(Long userId) {
        // get user
        UserDTO userDTO = getUser(userId);
        // get current folder
        DocumentEntity folder = documentRepository.getCurrentFolder(userDTO.getId(), userDTO.getFolderId());
        // set parent folder
        userDTO.setFolderId(folder.getFolderId());
        userDTO.setCurrentDecimalDoc(1);
        // update user folder
        update(userDTO);
        //edit button
        documentService.editButton(userDTO);
    }

    public void sortBySize(Long userId) {
        UserDTO userDTO = getUser(userId);
        if (userDTO.getSortType().equals(SortType.SIZE_ASC)) {
            userDTO.setSortType(SortType.SIZE_DESC);
        } else {
            userDTO.setSortType(SortType.SIZE_ASC);
        }
        updateUserSortType(userDTO);
        documentService.editButton(userDTO);
    }

    public void sortByName(Long userId) {
        UserDTO userDTO = getUser(userId);
        if (userDTO.getSortType().equals(SortType.NAME_ASC)) {
            userDTO.setSortType(SortType.NAME_DESC);
        } else {
            userDTO.setSortType(SortType.NAME_ASC);
        }
        updateUserSortType(userDTO);
        documentService.editButton(userDTO);
    }

    public void sortByDate(Long userId) {
        UserDTO userDTO = getUser(userId);
        if (userDTO.getSortType().equals(SortType.DATE_ASC)) {
            userDTO.setSortType(SortType.DATE_DESC);
        } else {
            userDTO.setSortType(SortType.DATE_ASC);
        }
        updateUserSortType(userDTO);
        documentService.editButton(userDTO);
    }
}
