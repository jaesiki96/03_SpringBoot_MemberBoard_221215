package com.its.memberboard.service;

import com.its.memberboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

}
