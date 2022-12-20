package com.its.memberboard.service;

import com.its.memberboard.dto.BoardDTO;
import com.its.memberboard.entity.BoardEntity;
import com.its.memberboard.entity.BoardFileEntity;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.BoardFileRepository;
import com.its.memberboard.repository.BoardRepository;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final MemberRepository memberRepository;

    // 글 작성
    public Long save(BoardDTO boardDTO) throws IOException {
        MemberEntity memberEntity = memberRepository.findByMemberEmail(boardDTO.getBoardWriter()).get();
        if (boardDTO.getBoardFile().get(0).isEmpty()) {
            System.out.println("파일 없음");
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO, memberEntity);
            return boardRepository.save(boardEntity).getId();
        } else {
            System.out.println("파일 있음");
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO, memberEntity);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity entity = boardRepository.findById(savedId).get();
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                String originalFileName = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
                String savePath = "D:\\springboot_img\\" + storedFileName;
                boardFile.transferTo(new File(savePath));
                BoardFileEntity boardFileEntity =
                        BoardFileEntity.toSaveBoardFileEntity(entity, originalFileName, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
            return savedId;
        }
    }

    // 글 목록
    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toDTO(boardEntity));
        }
        return boardDTOList;
    }

    // 페이징 목록
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        final int pageLimit = 5;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        Page<BoardDTO> boardList = boardEntities.map(
                board -> new BoardDTO(board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle(),
                        board.getBoardHits(),
                        board.getCreatedTime()
                )
        );
        return boardList;
    }

    // 글 상세조회
    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            return BoardDTO.toDTO(optionalBoardEntity.get());
        } else {
            return null;
        }
    }

    // 조회수
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    // 글 수정
    public void update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    // 글 삭제
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // 검색 처리
    @Transactional // 중요 ★★★
    public List<BoardDTO> search(String type, String q) {
        List<BoardDTO> boardDTOList = new ArrayList<>();
        List<BoardEntity> boardEntityList = null;
        if (type.equals("boardWriter")) {
            boardEntityList = boardRepository.findByBoardWriterContainingOrderByIdDesc(q);
        } else if (type.equals("boardTitle")) {
            boardEntityList = boardRepository.findByBoardTitleContainingOrderByIdDesc(q);
        } else {
            boardEntityList = boardRepository.findByBoardTitleContainingOrBoardWriterContainingOrderByIdDesc(q, q);
        }

        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toDTO(boardEntity));
        }
        return boardDTOList;
    }
}
