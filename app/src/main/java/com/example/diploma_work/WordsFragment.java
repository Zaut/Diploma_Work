package com.example.diploma_work;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;


import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;
import java.util.Locale;


public class WordsFragment extends Fragment implements TextToSpeech.OnInitListener {


    private List<Words> wordsList;

    private  TextView engl_word, ukrain_word, transcription, sentence, transSentence;

    private Button next_word, textToSpeechButton;

    private  EasyFlipView easyFlipView;

    private EditText input_word;

    private  ImageView leftArrow, rightArrow;
    private TextToSpeech textToSpeech;


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
        textToSpeechButton = view.findViewById(R.id.textToSpeechButton);


        textToSpeech = new TextToSpeech(getActivity(), this);


        wordsList = new GetData().getWords(selectedCategories);
        currentIndex = 0;


        // В методе onCreate() или в другом методе инициализации
        handler.postDelayed(colorUpdateRunnable, 1000); // Запускаем проверку сразу и повторно каждую секунду



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

                String textViewText = engl_word.getText().toString().toLowerCase();
                String editTextText = input_word.getText().toString().toLowerCase();

                if (textViewText.equals(editTextText)) {
                    showNextWord();
                    easyFlipView.flipTheView();
                    next_word.setVisibility(View.GONE);
                    input_word.getText().clear();
                }





            }
        });




        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                easyFlipView.flipTheView();
                next_word.setVisibility(View.VISIBLE);

            }
        });


        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                easyFlipView.flipTheView();
                next_word.setVisibility(View.GONE);

            }
        });



        textToSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = view.findViewById(R.id.engl_word);
                String text = textView.getText().toString();
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });



        return view;
    }


    private Handler handler = new Handler();
    private Runnable colorUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            checkAndUpdateNextButton(); // Вызываем метод для проверки и обновления цвета кнопки
            handler.postDelayed(this, 1000); // Повторно запускаем проверку через 1 секунду
        }
    };



    private boolean checkAndUpdateNextButton() {
        String textViewText = engl_word.getText().toString().toLowerCase();
        String editTextText = input_word.getText().toString().toLowerCase();

        if (textViewText.equals(editTextText)) {
            next_word.setEnabled(true);
            next_word.setBackgroundResource(R.drawable.button_registration);
            return true;
        } else {
            next_word.setEnabled(false);
            next_word.setBackgroundResource(R.drawable.button_unavailable);
            return false;
        }
    }



    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Обработка ошибки недоступности языка
            }
        } else {
            // Обработка ошибки инициализации TextToSpeech
        }
    }
    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
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
