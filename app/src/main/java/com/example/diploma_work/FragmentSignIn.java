package com.example.diploma_work;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;


public class FragmentSignIn extends Fragment {

    Connection connect;
    String Connectionesult = "";

    private EditText loginEditText , passwordEditText ;
    private Button registerButton;
    TextView tx1, tx2;

    public FragmentSignIn(){
        super(R.layout.fragment_sign_in);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inflate the layout for this fragment



        loginEditText =  view.findViewById(R.id.login);
        passwordEditText  =  view.findViewById(R.id.pass);
        registerButton =  view.findViewById(R.id.btn_signin);


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




    }
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


}