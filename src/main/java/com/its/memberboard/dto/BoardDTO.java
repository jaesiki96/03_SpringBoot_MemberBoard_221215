package com.its.memberboard.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardDTO {
    private Long id;
    private String boardTitle;
    private String boardWriter;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedDate;
    private int fileAttached;
}
