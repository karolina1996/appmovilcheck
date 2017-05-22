package com.karolina.check;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.karolina.check.databinding.ActivityNewVaccineBinding;
import com.karolina.check.db.VaccineDao;
import com.karolina.check.models.Vaccine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewVaccineActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityNewVaccineBinding  binding;
    VaccineDao vaccineDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewVaccineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vaccineDao = new VaccineDao(this);

        binding.accept.setOnClickListener(this);
        binding.cancel.setOnClickListener(this);
        binding.date.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept:
                if (newVaccine()) {
                    Toast.makeText(this, "Se agregó correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.cancel:
                finish();
                break;
            case  R.id.date:
                datePicker();
                break;
        }
    }

    private boolean newVaccine() {
        Vaccine nVaccine = new Vaccine();
        nVaccine.setName(binding.name.getText().toString());
        nVaccine.setDesc(binding.desc.getText().toString());
        nVaccine.setImage(binding.image.getText().toString());
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try{
            nVaccine.setDate(dateFormat.parse(binding.date.getText().toString()));
        }catch (ParseException e){
            e.printStackTrace();
        }
        vaccineDao.insert(nVaccine);
        return true;
    }

    private void datePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                binding.date.setText(year + "-" + (monthOfYear+1)+ "-" +dayOfMonth);
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}