package com.its.memberboard.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "member_file_table")
public class MemberFileEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    public static MemberFileEntity toSaveMemberFileEntity(MemberEntity memberEntity, String originalFileName, String storedFileName) {
        MemberFileEntity memberFileEntity = new MemberFileEntity();
        memberFileEntity.setOriginalFileName(originalFileName);
        memberFileEntity.setStoredFileName(storedFileName);
        memberFileEntity.setMemberEntity(memberEntity);
        return memberFileEntity;
    }
}
