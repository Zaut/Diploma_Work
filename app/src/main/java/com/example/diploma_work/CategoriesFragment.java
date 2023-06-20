package com.example.diploma_work;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class CategoriesFragment extends Fragment {
    private List<Categories> categories;


    private ImageButton button1,button2,button3;



    private FragmentManager fragmentManager;

   private  ListView listView;
    private int selectedLevelId;
    private CategoriesAdapter adaptercategories;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        //selectedLevelId = getArguments().getInt("selectedLevel");
        selectedLevelId = GlobalVariables.globalSelectedLevel;
        listView = view.findViewById(R.id.list_categoriee);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);


        fragmentManager = requireActivity().getSupportFragmentManager();



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


        return view;


    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        categories = new DatabaseHelper(requireContext()).getData(selectedLevelId);
        adaptercategories = new CategoriesAdapter(requireActivity(), fragmentManager, categories);
        listView.setAdapter(adaptercategories);
    }


}