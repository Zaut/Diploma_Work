package com.example.diploma_work;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;


public class WordsFragment extends Fragment {


    private List<Words> wordsList;

    TextView engl_word, ukrain_word, transcription, sentence, transSentence;

    Button next_word;

    EasyFlipView easyFlipView;

    EditText input_word;

    ImageView leftArrow, rightArrow;

    private String selectedCategories;
    private int currentIndex;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_words, container, false);

        selectedCategories = getArguments().getString("selectedCategories");
        Log.e("selectedCategories", selectedCategories + ": ");




        engl_word = view.findViewById(R.id.engl_word);
        ukrain_word = view.findViewById(R.id.ukrain_word);
        transcription = view.findViewById(R.id.transcription);
        sentence = view.findViewById(R.id.sentence);
        transSentence = view.findViewById(R.id.transSentence);
        next_word = view.findViewById(R.id.next_word);
        input_word = view.findViewById(R.id.input_word);
        easyFlipView = view.findViewById(R.id.easyFlipView);
         leftArrow = view.findViewById(R.id.leftArrow);
         rightArrow = view.findViewById(R.id.rightArrow);

        wordsList = new GetData().getWords(selectedCategories);
        currentIndex = 0;

        // Отображение первого слова в TextView
        if (wordsList != null && !wordsList.isEmpty()) {
            Words firstWord = wordsList.get(currentIndex);
            engl_word.setText(firstWord.Words);
            ukrain_word.setText(firstWord.TranslateWords);
            transcription.setText(firstWord.Transcriptions);
            sentence.setText(firstWord.Sentence);
            transSentence.setText(firstWord.TransSentence);
        }

        next_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textViewText = engl_word.getText().toString();
                String editTextText = input_word.getText().toString();
                if(textViewText.equals(editTextText) )
                {
                    showNextWord();
                }
                else {
                    next_word.setEnabled(false);
                    next_word.setBackgroundColor(getResources().getColor(R.color.red));


                }

            }
        });

        input_word.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String editTextText = input_word.getText().toString();
                    if (!editTextText.isEmpty()) {
                        next_word.setEnabled(true);
                        next_word.setBackgroundResource(R.drawable.button_registration);
                    }
                } else {
                    next_word.setEnabled(false);
                    next_word.setBackgroundColor(getResources().getColor(R.color.red));
                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();
            }
        });


        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void showNextWord() {
        if (wordsList != null && !wordsList.isEmpty()) {
            currentIndex++;
            if (currentIndex < wordsList.size()) {
                Words nextWord = wordsList.get(currentIndex);
                engl_word.setText(nextWord.Words);
                ukrain_word.setText(nextWord.TranslateWords);
                transcription.setText(nextWord.Transcriptions);
                sentence.setText(nextWord.Sentence);
                transSentence.setText(nextWord.TransSentence);
            } else {
                // Все слова просмотрены
                // Можно выполнить дополнительные действия или вернуться к началу списка
            }
        }
    }
}
