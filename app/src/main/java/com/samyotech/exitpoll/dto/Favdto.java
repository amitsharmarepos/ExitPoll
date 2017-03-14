package com.samyotech.exitpoll.dto;

/**
 * Created by samyotech on 16/12/16.
 */

public class Favdto {
    private String sno, useremail, politicsid;

    public Favdto() {
    }

    public Favdto(String sno, String useremail, String politicsid) {
        this.sno = sno;
        this.useremail = useremail;
        this.politicsid = politicsid;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getPoliticsid() {
        return politicsid;
    }

    public void setPoliticsid(String politicsid) {
        this.politicsid = politicsid;
    }
}
