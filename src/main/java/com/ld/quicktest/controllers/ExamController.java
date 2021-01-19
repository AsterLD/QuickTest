package com.ld.quicktest.controllers;

import com.ld.quicktest.models.Question;
import com.ld.quicktest.models.Result;
import com.ld.quicktest.models.Test;
import com.ld.quicktest.repos.ResultRepo;
import com.ld.quicktest.repos.TestRepo;
import com.ld.quicktest.repos.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/exam")
public class ExamController {

    private final ResultRepo resultRepo;
    private final TestRepo testRepo;
    private final UserRepo userRepo;

    public ExamController(ResultRepo resultRepo, TestRepo testRepo, UserRepo userRepo) {
        this.resultRepo = resultRepo;
        this.testRepo = testRepo;
        this.userRepo = userRepo;
    }

    @GetMapping()
    public String showTestList(Model model) {
        model.addAttribute("tests", testRepo.findAll());
        return "exam/testsList";
    }

    @GetMapping("/{testId}")
    public String showTest(Model model, @PathVariable("testId") Long testId) {
        model.addAttribute("test", testRepo.findByTestId(testId));
        return "exam/test";
    }

    @GetMapping("/results")
    public String showResults(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("results", resultRepo.findResultsByUser(userRepo.findByUsername(auth.getName())));
        return "exam/resultList";
    }

    @PostMapping()
    public String saveResult(@RequestParam HashMap<String, String> answers, Long testId, Result result) {
        int answerResult = 0;
        Test test = testRepo.findByTestId(testId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Question> questionList = test.getQuestionList();

        answers.keySet().removeIf(key -> !key.contains("answer"));
        System.out.println(answers.keySet());
        for(Map.Entry<String, String> answer : answers.entrySet()) {
            for (Question question: questionList) {
                if(answer.getKey().contains(question.getQuestionId().toString())) {
                    if (answer.getValue().equals(question.getAnswer()))
                        answerResult++;
                    break;
                    }
                }
            }
        result.setNumberOfCorrectAnswers(answerResult);
        result.setNumberOfQuestion(questionList.size());
        result.setPercentageOfCorrectAnswers((double) answerResult / questionList.size() * 100);
        result.setTest(test);
        result.setUser(userRepo.findByUsername(auth.getName()));
        resultRepo.save(result);
        return "exam/result";
    }
}