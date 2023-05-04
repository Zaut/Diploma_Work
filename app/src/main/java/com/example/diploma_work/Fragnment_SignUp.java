package com.example.diploma_work;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.res.ColorStateList;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class Fragnment_SignUp extends Fragment {

    PasswordHasher hesh;
    EditText Name,Email,Pass,Pass2;
    TextView Error;
    Button SigIn,Cancel;

    Connection connect;

    String hashedPassword;
 //   EditText[] editTextArray;


    public Fragnment_SignUp() {
       super(R.layout.fragment_fragnment__sign_up);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Name = view.findViewById(R.id.FirstName);
        Email = view.findViewById(R.id.Email);
        Pass = view.findViewById(R.id.pass1);
        Pass2 = view.findViewById(R.id.pass2);

        SigIn = view.findViewById(R.id.btn_singin2);
        Cancel = view.findViewById(R.id.btn_cancel);
        Error = view.findViewById(R.id.error);

//        editTextArray = new EditText[] {
//                view.findViewById(R.id.FirstName),
//                view. findViewById(R.id.Email),
//                view. findViewById(R.id.pass1),
//                view. findViewById(R.id.pass2)
//        };


        SigIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if (isAllFieldsFilled((ViewGroup) getView())) {
                    if(isValidEmail(Email.getText().toString())){
                        if(CheckPass(Pass,Pass2))
                        {

                            Error.setVisibility(View.GONE);
                            GetTextFromSQL(hashedPassword);



                            Toast toast = Toast.makeText(getContext(), "Вы успешно зарегестрировались", Toast.LENGTH_SHORT);
                            toast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    transition();
                                }
                            }, 3000);

                        }
                        else {
                            Error.setText("Пароли не совпадают");
                            Error.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        Error.setText("Поле Email заполнено неверно");
                        Error.setVisibility(View.VISIBLE);
                    }


                } else {
                    Error.setText("Заполните все поля");


                    Error.setVisibility(View.VISIBLE);
                }


            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                transition();
            }
        });



    }
    public  void GetTextFromSQL(String pass)
    {
        try{
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect!=null)
            {
                String query = "INSERT INTO Users (Name, UserEmail, UserPassword) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1, Name.getText().toString());
                preparedStatement.setString(2, Email.getText().toString());
                preparedStatement.setString(3, pass);
                preparedStatement.executeUpdate();
            }
            else
            {

            }
        }
        catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
    }

    public boolean CheckPass(EditText p1, EditText p2){

        if(p1.getText().toString().equals(p2.getText().toString()))
        {
            String password  = Pass.getText().toString();
            try {

                hashedPassword  =  hesh.hashPassword(password);
                Log.d("HashedPassword", hashedPassword);
                return true;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        else {
            Error.setVisibility(View.VISIBLE);
        }
        return false;
    }

    public boolean isAllFieldsFilled(ViewGroup viewGroup) {
        boolean isAllFieldsFilled = true;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                isAllFieldsFilled &= isAllFieldsFilled((ViewGroup) view);
            } else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String text = editText.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    editText.setBackgroundResource(R.drawable.my_edit_text_bg_exeption);
                    isAllFieldsFilled = false;
                } else {
                    editText.setBackgroundResource(R.drawable.my_edit_text_bg);
                }
            }
        }
        return isAllFieldsFilled;
    }

    public void transition()
    {
        Fragment LogIn = new FragmentSignIn();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Frame_Layout, LogIn);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}

//    public void setEditTextBackgroundOnTouch(EditText[] editTextArray, @DrawableRes int drawableRes) {
//        for (EditText editText : editTextArray) {
//            editText.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        editText.setBackgroundResource(drawableRes);
//                        return true;
//                    }
//                    return false;
//                }
//            });
//        }
//    }

//}