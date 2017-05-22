package com.karolina.check;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.karolina.check.adapters.ItemAdapter;
import com.karolina.check.attrs.L;
import com.karolina.check.databinding.ActivityVaccineListBinding;
import com.karolina.check.db.VaccineDao;
import com.karolina.check.models.Vaccine;

import java.util.ArrayList;
import java.util.List;

public class VaccineListActivity extends AppCompatActivity implements ItemAdapter.OnItemClickAdapter {

    ActivityVaccineListBinding binding;
    VaccineDao vaccineDao;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVaccineListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        L.vaccineStaticList = new ArrayList<>();
        vaccineDao = new VaccineDao(this);

        adapter = new ItemAdapter(this, L.vaccineStaticList, this);
        binding.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData(){
        L.vaccineStaticList.clear();

        List<Vaccine> result = vaccineDao.getAllVaccines();
        for(Vaccine vaccine : result){
            L.vaccineStaticList.add(vaccine);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onIClick(View v) {
        int position = binding.list.getChildAdapterPosition(v);
        Intent intent = new Intent(this, VaccineDescActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
