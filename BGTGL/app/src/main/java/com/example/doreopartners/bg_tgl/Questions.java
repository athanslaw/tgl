package com.example.doreopartners.bg_tgl;

/**
 * Created by Doreo Partners on 04/01/2018.
 */

public class Questions {
    String questionNo;
    String question;

    String id, applicantname, iknumber, qus1, qus2, qus3, qus4, qus5, qus6, qus7, qus8, qus9, qus10, qus11, qus12, date,
            village, amik, tfm_date, tfm_time;

    String ikNumber;
    String ikName;
    String interviewer;
    String score;
    String status;

    String grade;

    public Questions() {
    }

    public Questions(String id, String applicantname, String iknumber, String qus1, String qus2, String qus3, String qus4, String qus5, String qus6, String qus7, String qus8, String qus9, String qus10, String qus11, String qus12, String score, String status, String interviewer, String date,
                     String village, String amik, String tfm_date, String tfm_time){
        this.id = id; this.applicantname = applicantname; this.iknumber=iknumber;
        this.qus1= qus1; this.qus2=qus2; this.qus3=qus3; this.qus4=qus4; this.qus5=qus5; this.qus6=qus6; this.qus7= qus7; this.qus8 = qus8; this.qus9=qus9; this.qus10 = qus10;
        this.qus11=qus11; this.qus12=qus12; this.score=score; this.status=status; this.interviewer=interviewer; this.date=date;
        this.village=village; this.amik=amik; this.tfm_date=tfm_date; this.tfm_time=tfm_time;

    }
    public Questions(String questionNo, String ikNumber, String ikName, String grade, String score, String village, String tfm_date, String tfm_time, String amik) {
        this.ikName = ikName;
        this.ikNumber = ikNumber;
        this.grade = grade;
        this.score = score;
        this.village = village;
        this.questionNo = questionNo;
        this.tfm_date = tfm_date;
        this.tfm_time = tfm_time;
        this.amik = amik;
    }

    public String getDate() {
        return date;
    }

    public String getVillage() {
        return village;
    }

    public String getAmik() {
        return amik;
    }

    public String getTfm_date() {
        return tfm_date;
    }

    public String getTfm_time() {
        return tfm_time;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public String getQuestion() {
        return question;
    }

    public String getIkNumber() {
        return ikNumber;
    }

    public String getIkName() {
        return ikName;
    }

    public String getGrade() {
        return grade;
    }

    public String getScore() {
        return score;
    }

    public String getStatus() {
        return status;
    }

}
