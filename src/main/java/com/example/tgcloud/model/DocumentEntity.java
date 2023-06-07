package com.example.tgcloud.model;

import com.example.tgcloud.enums.FileType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "document")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer user_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",insertable = false,updatable = false)
    private UserEntity user;
    @Column(name = "folder_id")
    private Integer folderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id",insertable = false,updatable = false)
    private DocumentEntity folder;
    @Column
    private String fileId;
    @Column
    @Enumerated(EnumType.STRING)
    private FileType type;
    @Column
    private String name;
    @Column
    private String size;
    @Column
    private String duration;
    @Column
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(length =500)
    private String content;
    @Column
    private Integer messageId;

}
