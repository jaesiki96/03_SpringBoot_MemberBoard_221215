package com.its.memberboard.controller;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    // 회원가입 페이지
    @GetMapping("/save")
    public String saveForm() {
        return "memberPages/memberSave";
    }

    // 회원가입 처리
    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) throws IOException {
        memberService.save(memberDTO);
        return "memberPages/memberLogin";
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

    //로그인 페이지
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "redirectURL", defaultValue = "/board") String redirectURL,
                            Model model) {
        model.addAttribute("redirectURL", redirectURL);
        return "memberPages/memberLogin";
    }

    //로그인
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,
                        @RequestParam(value = "redirectURL", defaultValue = "/board") String redirectURL) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            return "redirect:" + redirectURL;
        } else {
            return "memberPages/memberLogin";
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    // 회원목록(관리자용)
    @GetMapping("/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "memberPages/memberList";
    }

    // 회원조회(관리자용)
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/memberDetail";
    }

    // 회원삭제(관리자용)
    @DeleteMapping("/{id}")
    public ResponseEntity deleteByAxios(@PathVariable Long id) {
        System.out.println("id = " + id);
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 마이페이지
    @GetMapping("/myPage")
    public String myPage(Model model, HttpSession session) {
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
        model.addAttribute("member", memberDTO);
        return "memberPages/myPage";
    }
}
