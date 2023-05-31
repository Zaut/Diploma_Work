package com.example.diploma_work;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;


import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;
import java.util.Locale;


public class WordsFragment extends Fragment implements TextToSpeech.OnInitListener {


    private List<Words> wordsList;

    private  TextView engl_word, ukrain_word, transcription, sentence, transSentence;

    private Button next_word, textToSpeechButton;

    private  EasyFlipView easyFlipView;

    private EditText input_word;

    private ImageButton cancelButton;

    private  ImageView leftArrow, rightArrow;
    private TextToSpeech textToSpeech;


    private String selectedCategories;
    private int currentIndex;

    private Button  ok_btn;

    private int currentWordId;

    private AlertDialog dialog;



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



        handler.postDelayed(colorUpdateRunnable, 1000);



        View alertCustomDialog = LayoutInflater.from(requireContext()).inflate(R.layout.customdialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setView(alertCustomDialog);
        dialog = alertDialogBuilder.create();


       // cancelButton =(ImageButton) alertCustomDialog.findViewById(R.id.cancelID);
        ok_btn = alertCustomDialog.findViewById(R.id.ok_btn);








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


        ok_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.cancel();
                Toast.makeText(requireContext(), "Thanks for watching", Toast.LENGTH_SHORT).show();


                Fragment newFragment = new CategoriesFragment();

                // Получаем FragmentManager из активности
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Выполняем операцию замены текущего фрагмента на новый фрагмент
                fragmentManager.beginTransaction()
                        .replace(R.id.Frame_Layout, newFragment) // замените R.id.fragment_container на ID вашего контейнера фрагментов
                        .commit();

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
            checkAndUpdateNextButton();
            handler.postDelayed(this, 1000);
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

    private void saveWordId(int wordId) {
        currentWordId = wordId;
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
//                int wordId = nextWord.getId();
//                // Сохраняем id слова в переменную для использования из других частей приложения
//                saveWordId(wordId);
            } else {

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
//                Words lastWord = wordsList.get(wordsList.size() - 1);
//                int lastWordId = lastWord.getId();
//                // Сохраняем id последнего слова в переменную для использования из других частей приложения
//                saveWordId(lastWordId);
//                Log.e("currentIndex", currentIndex + ": ");


                // Все слова просмотрены
                // Можно выполнить дополнительные действия или вернуться к началу списка

            }
        }
    }
}
