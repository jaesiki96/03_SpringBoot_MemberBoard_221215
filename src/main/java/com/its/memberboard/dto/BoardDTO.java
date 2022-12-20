package com.its.memberboard.dto;

import com.its.memberboard.entity.BoardEntity;
import com.its.memberboard.entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String boardTitle;
    private String boardWriter;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private int fileAttached;
    private List<MultipartFile> boardFile;
    private List<String> originalFileName;
    private List<String> storedFileName;

    public static BoardDTO toDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        // 파일 관련된 내용 추가
        if (boardEntity.getFileAttached() == 1) {
            // 첨부파일 있음
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 1
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            // 첨부파일 이름 가져옴
            for (BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()) {
                // BoardDTO 의 originalFileName 이 List 이기 때문에 add()를 이용하여
                // boardFileEntity 에 있는 originalFileName 을 옮겨 담음.
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        } else {
            // 첨부파일 없음
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 0
        }
        return boardDTO;
    }

    // 페이징 목록에 보여줄 것들
    public BoardDTO(Long id, String boardTitle, String boardWriter, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardTitle = boardTitle;
        this.boardWriter = boardWriter;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }
}
