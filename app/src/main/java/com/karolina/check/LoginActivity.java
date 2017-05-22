package com.karolina.check;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.karolina.check.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding binding;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("preferencias",MODE_PRIVATE);
        boolean logged = preferences.getBoolean("logged", false);
        if(logged){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.login.setOnClickListener(this);
        binding.register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                if(login()){
                    Intent intentL = new Intent(this, MainActivity.class);
                    startActivity(intentL);
                }
                break;
            case R.id.register:
                Intent intentR = new Intent(this, RegisterActivity.class);
                startActivity(intentR);
                break;
        }
    }

    private boolean login(){
        //TODO: networking login

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("logged", true); //TODO: cambiar a true para no mostrar login
        editor.commit();

        return true;
    }
}
