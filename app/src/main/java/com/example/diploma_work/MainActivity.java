package com.example.diploma_work;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity  {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Context appContext = getApplicationContext();

        // Использование контекста приложения
        GlobalVariables.loadState(appContext);
        setContentView(R.layout.activity_main);



//        FragmentRegistration fragmentRegistration = new FragmentRegistration();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.my_fragment, myFragment);
//        fragmentTransaction.commit();
//
        FragmentSignIn fragmentSignIn = new FragmentSignIn();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Frame_Layout, fragmentSignIn);
        ft.commit();

    }
}