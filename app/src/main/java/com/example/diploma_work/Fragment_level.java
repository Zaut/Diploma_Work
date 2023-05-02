package com.example.diploma_work;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class Fragment_level extends Fragment {

    private ListView listView;
    private LevelsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_level, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Levels> data = new GetData().getGroups();
        adapter = new LevelsAdapter(getActivity(), data);
        listView.setAdapter(adapter);
    }
}


//public class Fragment_level extends Fragment {
//
//    private ListView listView;
//    private LevelsAdapter adapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_level, container, false);
//
//        listView = (ListView) view.findViewById(R.id.list_view);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Get the selected level
//                Levels selectedLevel = (Levels) adapter.getItem(position);
//                int levelId = selectedLevel.getId();
//
//                // Create the fragment and pass the level ID as an argument
//                CategoriesFragment categoriesFragment = new CategoriesFragment();
//                Bundle args = new Bundle();
//                args.putInt("levelId", levelId);
//                categoriesFragment.setArguments(args);
//
//                // Open the categories fragment
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.Frame_Layout, categoriesFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        List<Levels> data = new GetData().getGroups();
//        adapter = new LevelsAdapter(getActivity(), data);
//        listView.setAdapter(adapter);
//    }
//}