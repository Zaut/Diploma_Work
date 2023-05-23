package com.example.diploma_work;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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



    Connection connect;
    String ConnectionResult = "";

    private FragmentManager fragmentManager;

   private  ListView listView;
    private int selectedLevelId;
    private CategoriesAdapter adaptercategories;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        selectedLevelId = getArguments().getInt("selectedLevel");
        listView = view.findViewById(R.id.list_categoriee);


        fragmentManager = requireActivity().getSupportFragmentManager();
        return view;


    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        categories = new GetData().getData(selectedLevelId);
        adaptercategories = new CategoriesAdapter(requireActivity(), fragmentManager, categories);
        listView.setAdapter(adaptercategories);
    }


}