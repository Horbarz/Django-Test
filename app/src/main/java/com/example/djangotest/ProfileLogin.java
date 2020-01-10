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

import com.example.djangotest.model.Login;
import com.example.djangotest.model.User;
import com.example.djangotest.util.InternetUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileLogin extends Fragment {

    EditText login_username;
    EditText login_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_login, container, false);

        Button logBtn = rootView.findViewById(R.id.login_button);
        Button to_reg_Btn = rootView.findViewById(R.id.to_registration_button);

        login_username = rootView.findViewById(R.id.reg_username);
        login_password = rootView.findViewById(R.id.reg_password);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logButtonClick();
            }
        });

        to_reg_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new ProfileRegister();
                replaceFragment(fragment);
            }
        });

        return rootView;
    }

    private void logButtonClick() {
        if(!isEmptyEditTextLogin()){
            if (InternetUtil.isInternetOnline(getActivity())){
                login();
            }
        }
    }

    private void login() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(PostApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        PostApi postApi = retrofit.create(PostApi.class);
        String uname = login_username.getText().toString();
        String password = login_password.getText().toString();

        Login login =new Login(uname,password);

        Call<User> call = postApi.login(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(response.body() !=null){

                        String token = response.body().getToken();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myprefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor myprefEditor = sharedPreferences.edit();
                        myprefEditor.putBoolean("loggedin",true);
                        myprefEditor.putString("token",token);
                        myprefEditor.commit();
                        Toast.makeText(getContext(), "Login Token:"+token, Toast.LENGTH_SHORT).show();

                        Fragment fragment = null;
                        fragment = new Home();
                        replaceFragment(fragment);

                    }

                }
                else{
                    Log.d("profile_login", "onResponse:failed ");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("profile_login", "onFailure: response failed ");
                Toast.makeText( getActivity(), "Server problem, check API", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean isEmptyEditTextLogin() {
        if(login_username.getText().toString().isEmpty()||login_password.getText().toString().isEmpty()){
            Toast toast = Toast.makeText(getActivity(),"Pls fill empty filleds", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return true;
        }else{
            return false;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
