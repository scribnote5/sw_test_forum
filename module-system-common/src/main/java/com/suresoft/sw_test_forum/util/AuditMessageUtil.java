package com.suresoft.sw_test_forum.util;

public class AuditMessageUtil {
    public static String getInsertAuditMessage(String message) {
        return "'" + message + "'가 등록 되었습니다.";
    }

    public static String getUpdateAuditMessage(String message) {
        return "'" + message + "'가 수정 되었습니다.";
    }

    public static String getDeleteAuditMessage(String message) {
        return "'" + message + "'가 삭제 되었습니다.";
    }

    public static String getInsertCommentAuditMessage(long postIdx) {
        return "'" + postIdx + "번 게시글'의 댓글이 등록 되었습니다.";
    }

    public static String getUpdateCommentAuditMessage(long postIdx) {
        return "'" + postIdx + "번 게시글'의 댓글이 수정 되었습니다.";
    }

    public static String getDeleteCommentAuditMessage(long postIdx) {
        return "'" + postIdx + "번 게시글'의 댓글이 삭제 되었습니다.";
    }

    public static String getLikeAuditMessage(long postIdx) {
        return "'" + postIdx + "번 게시글'을 좋아요 하였습니다.";
    }

    public static String getCancelLikeAuditMessage(long postIdx) {
        return "'" + postIdx + "번 게시글'을 좋아요 취소 하였습니다.";
    }

    public static String getLoginFailureMessage(String message) {
        return "'" + message + "'가 로그인에 실패 하였습니다.";
    }

    public static String getLoginSuccessMessage(String message) {
        return "'" + message + "'가 로그인에 성공 하였습니다.";
    }
}
