package com.suresoft.sw_test_forum.question_answer.domain.enums;

public enum QuestionAnswerType {
    STATIC_TEST_QUESTION("정적시험 질문"),
    DYNAMIC_TEST_QUESTION("동적시험 질문"),
    TOOL_QUESTION("도구 질문"),
    ETC_QUESTION("기타 질문"),
    ANSWER("답변"),
    COMPLETE("완료");

    private final String questionAnswerType;

    QuestionAnswerType(String questionAnswerType) {
        this.questionAnswerType = questionAnswerType;
    }

    public String getQuestionAnswerCategory() {
        return this.questionAnswerType;
    }
}