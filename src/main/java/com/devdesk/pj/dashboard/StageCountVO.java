package com.devdesk.pj.dashboard;


public class StageCountVO {

    private int applied;
    private int documentPass;
    private int firstInterview;
    private int secondInterview;
    private int thirdInterview;
    private int codingTest;
    private int passed;
    private int failed;
    private String tagColor;
    private String tagBg;
    private String timeAgo;


    // getter / setter 추가
    public int getApplied() {
        return applied;
    }


    public int getDocumentPass() {
        return documentPass;
    }

    public int getFirstInterview() {
        return firstInterview;
    }

    public int getSecondInterview() {
        return secondInterview;
    }

    public int getThirdInterview() {
        return thirdInterview;
    }

    public int getCodingTest() {
        return codingTest;
    }

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public void setApplied(int applied) {
        this.applied = applied;
    }

    public void setDocumentPass(int documentPass) {
        this.documentPass = documentPass;
    }

    public void setFirstInterview(int firstInterview) {
        this.firstInterview = firstInterview;
    }

    public void setSecondInterview(int secondInterview) {
        this.secondInterview = secondInterview;
    }

    public void setThirdInterview(int thirdInterview) {
        this.thirdInterview = thirdInterview;
    }

    public void setCodingTest(int codingTest) {
        this.codingTest = codingTest;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }


    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    public String getTagBg() {
        return tagBg;
    }

    public void setTagBg(String tagBg) {
        this.tagBg = tagBg;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}



