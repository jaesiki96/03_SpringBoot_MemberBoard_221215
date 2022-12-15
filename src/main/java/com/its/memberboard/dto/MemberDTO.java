package com.its.memberboard.dto;

import com.its.memberboard.entity.MemberEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MemberDTO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String memberMobile;

    private LocalDateTime memberCreatedDate;

    private MultipartFile memberProfile;
    private String originalFileName;
    private String storedFileName;
    private int fileAttached;

    public static MemberDTO toDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberMobile(memberEntity.getMemberMobile());
        memberDTO.setMemberCreatedDate(memberEntity.getCreatedTime());
        if (memberEntity.getFileAttached() == 1) {
            // 첨부파일 있음
            memberDTO.setFileAttached(memberEntity.getFileAttached()); // 1
            memberDTO.setOriginalFileName(memberEntity.getMemberFileEntityList().get(0).getOriginalFileName());
            memberDTO.setStoredFileName(memberEntity.getMemberFileEntityList().get(0).getStoredFileName());
        } else {
            // 첨부파일 없음
            memberDTO.setFileAttached(memberEntity.getFileAttached()); // 0
        }
        return memberDTO;
    }
}
