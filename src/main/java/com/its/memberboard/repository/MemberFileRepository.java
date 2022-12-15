package com.its.memberboard.repository;

import com.its.memberboard.entity.MemberFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFileRepository extends JpaRepository<MemberFileEntity, Long> {
}
