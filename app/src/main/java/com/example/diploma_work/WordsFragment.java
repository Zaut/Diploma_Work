package com.example.diploma_work;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class WordsFragment extends Fragment {


    private List<Words> words;

    TextView test;
    ListView listView;

    private WordsAdapter wordsAdapter;
    private String selectedCategories;

    private FragmentManager fragmentManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_words, container, false);

        selectedCategories = getArguments().getString("selectedCategories");
        Log.e("selectedCategories", selectedCategories + ": ");

        fragmentManager = requireActivity().getSupportFragmentManager();
        listView = view.findViewById(R.id.list_view);

//
//        test = view.findViewById(R.id.qwe);
//        test.setText(String.valueOf(selectedCategories));





        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        words = new GetData().getWords(selectedCategories);
        wordsAdapter = new WordsAdapter(requireActivity(), fragmentManager, words);
        listView.setAdapter(wordsAdapter);

    }
}
