package com.example.quizapp.service;

import com.example.quizapp.dao.QuestionDao;
import com.example.quizapp.model.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    private QuestionDao questionDao;
    public ResponseEntity<List<Question>>getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("something went wrong", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> updateQuestion(Long id, Question updatedQuestion) {
        return questionDao.findById(Math.toIntExact(id))
                .map(existingQuestion -> {
                    // Use Spring's BeanUtils or a similar library to copy properties
                    BeanUtils.copyProperties(updatedQuestion, existingQuestion, "id");
                    questionDao.save(existingQuestion);
                    return new ResponseEntity<>("Question updated successfully.",HttpStatus.OK);
                })
                .orElse( new ResponseEntity<>("Question not found.", HttpStatus.NOT_FOUND));
    }


    public ResponseEntity<String> deleteQuestion(Long id) {
        if (questionDao.existsById(Math.toIntExact(id))) {
            questionDao.deleteById(Math.toIntExact(id));
            return new ResponseEntity<>("Question deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Question not found.", HttpStatus.NOT_FOUND);
        }
    }
}
