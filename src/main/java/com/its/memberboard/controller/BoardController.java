package com.its.memberboard.controller;

import com.its.memberboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
}
