package application.wisefile.chk;

public enum DatePattern {

    DATE_PATTERN1("HH24:MI", "^([01]?[0-9]|2[0-3]):([0-5][0-9])$"),
    DATE_PATTERN2("YYYY-MM-DD", "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$"),
    DATE_PATTERN3("YYYY-MM-DD HH", "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-3])$"),
    DATE_PATTERN4("MM-dd HH:mm", "^(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-3]):(0[1-9]|[1-5][0-9])$"),
    DATE_PATTERN5("HH:mm:ss", "^(0[1-9]|1[0-9]|2[0-3]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$"),
    DATE_PATTERN6("YYYY-MM-DD HH:mm", "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-3]):(0[1-9]|[1-5][0-9])$"),
    DATE_PATTERN7("YYYY-MM", "^\\d{4}-(0[1-9]|1[012])$"),
    DATE_PATTERN8("MM-dd", "^(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$"),
    DATE_PATTERN9("YYYY-MM-DD HH24:MI:SS", "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-3]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$"),
    DATE_PATTERN10("YYYYMMDDHH24MISS", "^\\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-9]|2[0-3])(0[1-9]|[1-5][0-9])(0[1-9]|[1-5][0-9])$");

    private String patternName;
    private String regExp;

    DatePattern(String patternName, String regExp) {
        this.patternName = patternName;
        this.regExp = regExp;
    }

    public String getRegExp() {return regExp;};
    public String getPatternName() {return patternName;};

}
