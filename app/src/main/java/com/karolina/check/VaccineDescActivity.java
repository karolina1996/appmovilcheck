package com.karolina.check;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.karolina.check.attrs.L;
import com.karolina.check.databinding.ActivityVaccineDescBinding;

public class VaccineDescActivity extends AppCompatActivity {

    ActivityVaccineDescBinding binding;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVaccineDescBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        binding.setVaccine(L.vaccineStaticList.get(position));

    }
}
