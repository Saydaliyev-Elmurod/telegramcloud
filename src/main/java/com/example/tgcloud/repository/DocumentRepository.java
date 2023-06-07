package com.example.tgcloud.repository;

import com.example.tgcloud.model.DocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentRepository extends CrudRepository<DocumentEntity, Integer>, PagingAndSortingRepository<DocumentEntity, Integer> {
    //    @Query("from DocumentEntity where user.id=:userId and folder.id=:folderId ")
//    Page<DocumentEntity> getDocumentsInFolder(@Param("userId") Integer userId, @Param("folderId") Integer folderId, Pageable pageable);
    Page<DocumentEntity> getByUserIdAndFolderId(Integer u_id, Integer f_id, Pageable pageable);

    @Query("from DocumentEntity where user_id=:userId and id=:folderId ")
    DocumentEntity getCurrentFolder(@Param("userId") Integer userId, @Param("folderId") Integer folderId);

    @Modifying
    @Transactional
    @Query("update DocumentEntity set name=?1 where id=?2 ")
    void updateName(String text, Integer docId);

    @Query("from  DocumentEntity where user.userId=?1 ")
    List<DocumentEntity> getDoc(Long uId);

    @Query("from  DocumentEntity where user.userId=?2 and lower(name) like ?1 and folderId=?3")
    List<DocumentEntity> getDocQuery(String query, Long userId,Integer folderId);

    @Query("from DocumentEntity where folderId =?2 and name=?1")
    DocumentEntity getByName(String fileName, Integer folderId);

}


