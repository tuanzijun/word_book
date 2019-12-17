package com.example.word_book;

import java.security.PublicKey;

public class WordBean {
    public static final String WORD = "word";
    public static final String MEANING = "mean";

    private String id;
    private String word;
    private String mean;

    public WordBean(String word, String mean, String id){
        this.id = id;
        this.word = word;
        this.mean = mean;
    }

    public WordBean(){}

    public String getWord() {
        return word;
    }


    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return mean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMeaning(String meaning) {
        this.mean = meaning;
    }
    public String toString(){
        return this.word+"  "+this.mean+"  "+this.id;
    }

}
