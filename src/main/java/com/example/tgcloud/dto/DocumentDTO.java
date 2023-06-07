package com.example.tgcloud.dto;

import com.example.tgcloud.enums.FileType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentDTO {
    private Integer id;
    private Integer userId;
    private Integer folderId ;
    private String fileId;
    private FileType type;
    private String name;
    private String content;
    private String size;
    private String duration;
    private LocalDateTime createdDate;
    private Integer messageId;

}
