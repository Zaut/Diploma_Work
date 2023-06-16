package com.example.diploma_work;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


public class FragmentSignIn extends Fragment {

    Connection connect;
    String Connectionesult = "";

    private EditText loginEditText , passwordEditText ;
    private Button registerButton, SignIntButton;
    TextView tx1, tx2;

    private List<Users> users;
    public FragmentSignIn(){
        super(R.layout.fragment_sign_in);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inflate the layout for this fragment



        loginEditText =  view.findViewById(R.id.EmailSignIn);
        passwordEditText  =  view.findViewById(R.id.PasswordSignIn);
        registerButton =  view.findViewById(R.id.btn_signin);
        SignIntButton = view.findViewById(R.id.btn_login);




        GetData getData = new GetData();

        users = getData.getUsers();
        getData = new GetData();
        List<Users> userList = getData.getUsers();
        for (Users user : userList) {
            Log.e("User", user.Name);
            Log.e("Email", user.Email);
            Log.e("Separator", "----------------------");
        }

        SignIntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredPassword = passwordEditText.getText().toString();
                String loginName = loginEditText.getText().toString();

                boolean isNameFound = false; // Флаг для проверки совпадения имени пользователя
                for (Users user : userList) {
                    if (user.Email.equals(loginName)) {

                        Log.e("users", user.Email + ": ");
                        Log.e("loginName", loginName + ": ");





                        isNameFound = true; // Устанавливаем флаг в true, если найдено совпадение имени
                        try {
                            String hashedPassword = PasswordHasher.hashPassword(enteredPassword);
                            if (user.Password.equals(hashedPassword)) {

                                GlobalVariables.globalLogin = user.Email;
                                Log.e("globalLogin", GlobalVariables.globalLogin + ": ");

                                Fragment SignIn = new Fragment_level();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.Frame_Layout, SignIn);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            } else {
                                // Неверный пароль
                                Toast.makeText(getContext(), "Неверный пароль", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                if (!isNameFound) {
                    // Имя пользователя не найдено
                    Toast.makeText(getContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                        Fragment SignIn = new Fragnment_SignUp();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.Frame_Layout, SignIn);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

            }

        });

//    public  void GetTextFromSQL(View view)
//    {
//        tx1 = view.findViewById(R.id.loginTxt);
//        tx1 = view.findViewById(R.id.passTxt);
//
//
//        try{
//            ConnectionHelper connectionHelper = new ConnectionHelper();
//            connect = connectionHelper.connectionclass();
//            if(connect!=null)
//            {
//                String query = "Select * from Users";
//                Statement st = connect.createStatement();
//                ResultSet rs = st.executeQuery(query);
//
//                while(rs.next())
//                {
//                    tx1.setText(rs.getString(1));
//                    tx2.setText(rs.getString(2));
//                }
//            }
//            else
//            {
//                Connectionesult = "Check Connection";
//            }
//        }
//        catch (Exception ex) {
//            Log.e("error", ex.getMessage());
//        }
//    }
//
//
}
}