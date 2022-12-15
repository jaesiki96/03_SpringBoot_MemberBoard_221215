package com.its.memberboard.service;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.entity.MemberFileEntity;
import com.its.memberboard.repository.MemberFileRepository;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberFileRepository memberFileRepository;

    // 회원가입 처리
    public Long save(MemberDTO memberDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리
        if (memberDTO.getMemberProfile().isEmpty()) {
            // 첨부 파일 없음
            MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
            return memberRepository.save(memberEntity).getId();
        } else {
            // 첨부 파일 있음
            MultipartFile memberProfile = memberDTO.getMemberProfile();
            String originalFileName = memberProfile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
            String savePath = "D:\\springboot_img\\" + storedFileName;
            memberProfile.transferTo(new File(savePath));

            MemberEntity memberEntity = MemberEntity.toSaveFileEntity(memberDTO);
            Long savedId = memberRepository.save(memberEntity).getId();

            MemberEntity entity = memberRepository.findById(savedId).get();
            MemberFileEntity memberFileEntity =
                    MemberFileEntity.toSaveMemberFileEntity(entity, originalFileName, storedFileName);
            memberFileRepository.save(memberFileEntity);
            return savedId;
        }
    }

    // 이메일 중복체크
    public String emailDuplicateCheck(String memberEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);
        if (optionalMemberEntity.isEmpty()) {
            return "ok";
        } else {
            return null;
        }
    }

    // 로그인 처리
    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                return MemberDTO.toDTO(memberEntity);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
