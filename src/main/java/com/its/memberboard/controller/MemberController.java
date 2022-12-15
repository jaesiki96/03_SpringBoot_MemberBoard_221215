package com.its.memberboard.controller;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.service.BoardService;
import com.its.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final BoardService boardService;

    // 회원가입 페이지
    @GetMapping("/save")
    public String saveForm() {
        return "memberPages/memberSave";
    }

    // 회원가입 처리
    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) throws IOException {
        memberService.save(memberDTO);
        return "index";
    }

    // 이메일 중복체크
    @PostMapping("/dup-check")
    public ResponseEntity emailDuplicateCheck(@RequestParam("inputEmail") String memberEmail) {
        String checkResult = memberService.emailDuplicateCheck(memberEmail);
        if (checkResult != null) {
            return new ResponseEntity<>("사용 가능", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("사용 불가", HttpStatus.CONFLICT);
        }
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "memberPages/memberLogin";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            return "boardPages/boardPaging";
        } else {
            return "memberPages/memberLogin";
        }
    }

}
