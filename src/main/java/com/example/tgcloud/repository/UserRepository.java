package com.example.tgcloud.repository;

import com.example.tgcloud.enums.SortType;
import com.example.tgcloud.enums.UserStep;
import com.example.tgcloud.model.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findByUserId(Long id);

    @Transactional
    @Modifying
    @Query("update UserEntity set folderId=:folderId where id=:userId")
    void updateUserFolder(@Param("folderId") Integer folderId, @Param("userId") Integer userId);
    @Transactional
    @Modifying
    @Query("update UserEntity set step=:step where id=:userId")
    void updateUserStep(@Param("step") UserStep step, @Param("userId") Integer userId);
    @Transactional
    @Modifying
    @Query("update UserEntity set currentDecimalDoc=:decimal where id=:userId")
    void updateUserDecimal(@Param("decimal") Integer decimal, @Param("userId") Integer userId);
    @Transactional
    @Modifying
    @Query("update UserEntity set sortType=:type where id=:userId")
    void updateUserSortType(@Param("type") SortType type, @Param("userId") Integer userId);
}
