package com.example.diploma_work;

public class Words {


    int Id;

    String CategoryName;

    String Words;

    String Transcriptions;

    String Sentence;

    String TranslateWords;

    String TransSentence;

    byte[] Picture;

    private int currentWordId = 0;


    public int getId()
    {
        return Id;
    }
    public byte[] getImage() {
        return Picture;
    }
}
