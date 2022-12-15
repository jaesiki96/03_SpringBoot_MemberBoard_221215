package com.its.memberboard.entity;

import com.its.memberboard.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "member_table")
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String memberEmail;

    @Column(length = 20, nullable = false)
    private String memberPassword;

    @Column(length = 20, nullable = false)
    private String memberName;

    @Column(length = 30, nullable = false)
    private String memberMobile;

    @Column
    private int fileAttached;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberFileEntity> memberFileEntityList = new ArrayList<>();

    // 글 저장 Entity
    public static MemberEntity toSaveEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setFileAttached(0);
        return memberEntity;
    }

    // 파일 저장 Entity
    public static MemberEntity toSaveFileEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setFileAttached(1);
        return memberEntity;
    }


}
