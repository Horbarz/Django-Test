package com.example.djangotest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djangotest.model.User;
import com.example.djangotest.util.InternetUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileRegister extends Fragment {
    EditText reg_username;
    EditText reg_password;
    EditText reg_email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_register, container, false);

        Button regBtn = rootView.findViewById(R.id.registration_button);
        Button logBtn = rootView.findViewById(R.id.to_login_button);

        reg_username = rootView.findViewById(R.id.reg_username);
        reg_password = rootView.findViewById(R.id.reg_password);
        reg_email = rootView.findViewById(R.id.reg_email);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("loginTag", "onClick: Am now here");
                Fragment fragment = null;
                fragment = new ProfileLogin();
                replaceFragment(fragment);

            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reg_username_str = reg_username.getText().toString();
                String reg_password_str = reg_password.getText().toString();
                String reg_email_str = reg_email.getText().toString();

                User userModel = new User(6,
                        reg_email_str,
                        reg_username_str,
                        reg_password_str,
                        "sldfhslfhos");

                if (!isEmptyEditTextLogin()){
                    if(InternetUtil.isInternetOnline(getActivity())){
                        RegisterInServer(userModel);

                    }
                }
            }
        });

        return rootView;
    }

    private Boolean isEmptyEditTextLogin(){
        if(reg_password.getText().toString().isEmpty()||reg_username.getText().toString().isEmpty()||reg_email.getText().toString().isEmpty()){

            Toast toast = Toast.makeText(getActivity(), "Empty EditText", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return true;
        }else{
            return false;
        }
    }

    public void RegisterInServer(User userModel){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostApi postApi = retrofit.create(PostApi.class);
        Call<User> call = postApi.registerationUser(userModel);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        SharedPreferences preferences = getActivity().getSharedPreferences("myprefs",Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefLoginEdit = preferences.edit();
                        prefLoginEdit.putBoolean("registration",true);
                        prefLoginEdit.commit();
                        Toast.makeText(getActivity(), "Succesfully registered", Toast.LENGTH_SHORT).show();


                    }
                }
                else{
                    Log.d("fail", "onResponse: failed");
                    Toast.makeText(getActivity(), "Unable to register", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Fail", "onFailure: Failed");


            }
        });


    }

    public void replaceFragment(Fragment someFragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
