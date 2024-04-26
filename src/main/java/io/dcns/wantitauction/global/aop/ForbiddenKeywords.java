package io.dcns.wantitauction.global.aop;

public enum ForbiddenKeywords {
    ALCOHOL("주류", "alcohol"),
    TOBACCO("담배", "tobacco"),
    DRUG("마약", "drug"),
    GUN("총", "gun");

    private final String korean;
    private final String english;

    ForbiddenKeywords(String korean, String english) {
        this.korean = korean;
        this.english = english;
    }

    public String getKorean() {
        return korean;
    }

    public String getEnglish() {
        return english;
    }
}
