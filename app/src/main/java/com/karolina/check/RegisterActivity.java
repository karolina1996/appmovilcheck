package com.karolina.check;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.karolina.check.databinding.ActivityRegisterBinding;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.register.setOnClickListener(this);
        binding.cancel.setOnClickListener(this);
        binding.birthday.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                if(register()){
                    Toast.makeText(this, "El registro fue exitoso", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this, "No se pudo hacer el registro", Toast.LENGTH_SHORT).show();
                }
                break;
            case  R.id.cancel:
                finish();
                break;

            case R.id.birthday:
                datePicker();
                break;
        }
    }

    private boolean register(){
        return true; //TODO: webservice
    }

    private void datePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                binding.birthday.setText(year + "-" + (monthOfYear+1)+ "-" +dayOfMonth);
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
