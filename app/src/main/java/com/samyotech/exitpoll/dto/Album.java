package com.samyotech.exitpoll.dto;

/**
 * Created by varun on 30/11/16.
 */
public class Album {
    public String id, name, post, partyname, whichpost, education, age, img;

    public Album() {
    }

    public Album(String id, String name, String post, String partyname, String whichpost, String education, String age, String img) {
        this.id = id;
        this.name = name;
        this.post = post;
        this.partyname = partyname;
        this.whichpost = whichpost;
        this.education = education;
        this.age = age;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPartyname() {
        return partyname;
    }

    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    public String getWhichpost() {
        return whichpost;
    }

    public void setWhichpost(String whichpost) {
        this.whichpost = whichpost;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
