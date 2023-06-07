package com.example.tgcloud.dto;

import com.example.tgcloud.enums.SortType;
import com.example.tgcloud.enums.UserStep;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    private Integer id;
    private Long userId;
    private UserStep step;
    private SortType sortType;
    private Integer folderId;
    private Integer defaultFolderId;
    private Integer currentDecimalDoc;
    private Integer currentMessageId;
    private Integer renameDocId;
    private Boolean isClickButton;
}
