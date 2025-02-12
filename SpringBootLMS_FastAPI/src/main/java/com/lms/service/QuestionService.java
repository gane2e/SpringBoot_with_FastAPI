package com.lms.service;

import com.lms.dto.QuestionDto;
import com.lms.entity.Questions;
import com.lms.repository.QuestionsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class QuestionService {

    private final QuestionsRepository questionsRepository;

    // 랜덤 10문제 추출하기
    public List<QuestionDto> getQuestionDtoList(Long subCategoryId){
        List<Questions> questionsList =
                questionsRepository.findBySubCategory_SubCategoryId(subCategoryId);

        List<QuestionDto> questionDtoList = questionsList.stream()
                .map(QuestionDto::of)  // 각 Questions 객체를 QuestionDto로 변환
                .collect(Collectors.toList()); // 변환된 객체들을 리스트로 모은다

        Collections.shuffle(questionDtoList);
        return questionDtoList.subList(0, Math.min(3, questionDtoList.size()));
    }

    // 사용자 정답제출시 문제별 정답 DB에서 가져와 사용자정답과 비교하기
    public boolean checkAnswer(Long questionId, int userAnswer) {
        Questions questions = questionsRepository.findById(questionId)
                .orElseThrow();
        int correctAnswer = questions.getCorrectAnswer();

        log.info("사용자가 선택한 정답 : " + userAnswer);
        log.info("실제 정답 : " + correctAnswer);
        
        if (correctAnswer == userAnswer) {
            log.info("정답을 맞췄습니다.");
            return true;
        }
        log.info("정답을 맞추지 못했습니다.");
        return false;
    }

    
}

