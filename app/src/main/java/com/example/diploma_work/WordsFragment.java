package com.example.diploma_work;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Locale;


public class WordsFragment extends Fragment implements TextToSpeech.OnInitListener {


    private List<Words> wordsList;

    ConnectionHelper connect;

    private  TextView engl_word, ukrain_word, transcription, sentence, transSentence;

    private Button next_word, textToSpeechButton;

    private  EasyFlipView easyFlipView;

    private EditText input_word;

    private ImageButton cancelButton;

    private DatabaseHelper dbHelp;
    private  ImageView leftArrow, rightArrow, image_card, image_card2;
    private TextToSpeech textToSpeech;

    private ImageButton button1,button2,button3;

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
        image_card = view.findViewById(R.id.image_card);
        image_card2 = view.findViewById(R.id.image_card2);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);





        textToSpeech = new TextToSpeech(getActivity(), this);

        wordsList = new GetData().getWords(requireContext(), selectedCategories);
        currentIndex = 0;



        handler.postDelayed(colorUpdateRunnable, 1000);



        View alertCustomDialog = LayoutInflater.from(requireContext()).inflate(R.layout.customdialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setView(alertCustomDialog);
        dialog = alertDialogBuilder.create();


       // cancelButton =(ImageButton) alertCustomDialog.findViewById(R.id.cancelID);
        ok_btn = alertCustomDialog.findViewById(R.id.ok_btn);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentLevel = new Fragment_level();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Frame_Layout, fragmentLevel);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        ConnectionHelper connectionHelper = new ConnectionHelper();



        // Отображение первого слова в TextView
        if (wordsList != null && !wordsList.isEmpty()) {

            Words firstWord = wordsList.get(currentIndex);
            Log.e("currentIndex", currentIndex+ ": ");
            Log.e("firstWord", firstWord.Id + ": ");
            engl_word.setText(firstWord.Words);
            ukrain_word.setText(firstWord.TranslateWords);
            transcription.setText(firstWord.Transcriptions);
            sentence.setText(firstWord.Sentence);
            transSentence.setText(firstWord.TransSentence);
            SVGConverter.displaySVGImage(firstWord.getImage(), image_card);
            SVGConverter.displaySVGImage(firstWord.getImage(), image_card2);

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
                Fragment newFragment = new CategoriesFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.Frame_Layout, newFragment)
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

            }
        } else {

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
        dbHelp = new DatabaseHelper(requireContext());
        if (wordsList != null && !wordsList.isEmpty()) {
            currentIndex++;
            if (currentIndex < wordsList.size()) {
                Words nextWord = wordsList.get(currentIndex);
                engl_word.setText(nextWord.Words);
                ukrain_word.setText(nextWord.TranslateWords);
                transcription.setText(nextWord.Transcriptions);
                sentence.setText(nextWord.Sentence);
                transSentence.setText(nextWord.TransSentence);
                SVGConverter.displaySVGImage(nextWord.getImage(), image_card);
                SVGConverter.displaySVGImage(nextWord.getImage(), image_card2);
//


                if (currentIndex >= 0) {
                    Words previousWord = wordsList.get(currentIndex -1);
                    Log.e("currentIndex", currentIndex + ": ");
                    previousWord.setCompleted(1);
                    Log.e("previousWord", previousWord.Completed + ": ");
                    dbHelp.updateWordCompletionStatus(previousWord.Words, previousWord.getCompleted(), selectedCategories);
                }
                GlobalVariables.saveState(getContext());

            } else {

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
//

                // Все слова просмотрены
                // Можно выполнить дополнительные действия или вернуться к началу списка
                GlobalVariables.listSucces.add(selectedCategories);
                GlobalVariables.saveState(getContext());

            }
        }
    }
}
