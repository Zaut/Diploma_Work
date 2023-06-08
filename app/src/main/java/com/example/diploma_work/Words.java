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

    int Completed;

    private int currentWordId = 0;


    public int getCompleted() {
        return Completed;
    }

    public void setCompleted(int completed) {
        Completed = completed;
    }


    public int getId()
    {
        return Id;
    }
    public byte[] getImage() {
        return Picture;
    }
}
