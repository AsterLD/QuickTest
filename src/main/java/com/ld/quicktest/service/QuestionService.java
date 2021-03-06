package com.ld.quicktest.service;

import com.ld.quicktest.models.Question;
import com.ld.quicktest.repos.QuestionRepo;
import com.ld.quicktest.repos.TestRepo;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/*
 * Класс QuestionService, хранит в себе логику, для работы QuestionController.
 * createNewQuestion - Создает пустой вопрос и сохраняет его в БД, а после передает страницу для заполнения данными,
 * editQuestion - ищет вопрос по ID, и передает страницу для редактирования
 * updateQuestion - сохраняет вопрос в БД, привязывает к тестированию и передает страницу полученную от контроллера,
 * deleteQuestion - удаляет вопрос из БД.
 */

@Service
public class QuestionService {

    private final TestRepo testRepo;
    private final QuestionRepo questionRepo;

    public QuestionService(TestRepo testRepo, QuestionRepo questionRepo) {
        this.testRepo = testRepo;
        this.questionRepo = questionRepo;
    }

    public String createNewQuestion(Model model, Long testId) {
        Question question = new Question();
        questionRepo.save(question);
        model.addAttribute("question", question);
        model.addAttribute("test", testRepo.findTestByTestId(testId));
        return "question/newQuestionsPage";
    }

    public String editQuestion(Model model, Long questionId) {
        model.addAttribute("question", questionRepo.findQuestionByQuestionId(questionId));
        return "question/editQuestionPage";
    }

    public String updateQuestion(Long testId, Question question, String page) {
        question.setTest(testRepo.findTestByTestId(testId));
        questionRepo.save(question);
        return page;
    }

    public String deleteQuestion(Question question) {
        questionRepo.delete(question);
        return "redirect:/tests/{testId}/edit";
    }
}
