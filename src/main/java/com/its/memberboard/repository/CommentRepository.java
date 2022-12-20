package com.its.memberboard.repository;

import com.its.memberboard.dto.CommentDTO;
import com.its.memberboard.entity.BoardEntity;
import com.its.memberboard.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
