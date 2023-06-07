package com.example.tgcloud.model;

import com.example.tgcloud.enums.SortType;
import com.example.tgcloud.enums.UserStep;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "user_t")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Long userId;
    @Column()
    @Enumerated(EnumType.STRING)
    private UserStep step = UserStep.FREE;
    @Column()
    @Enumerated(EnumType.STRING)
    private SortType sortType = SortType.NAME_ASC;
    @OneToOne
    @JoinColumn(name = "folder_id",insertable = false,updatable = false)
    private DocumentEntity folder;
    @Column(name = "folder_id")
    private Integer folderId;
    @Column
    private Integer defaultFolderId;
    @Column
    private Integer currentDecimalDoc = 1;  // default 1
    @Column
    private Integer currentMessageId;
    @Column
    private Integer renameDocId;
    @Column(name = "button")
    private Boolean isClickButton;
}
