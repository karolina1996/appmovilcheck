package com.karolina.check;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.karolina.check.adapters.ItemAdapter;
import com.karolina.check.attrs.L;
import com.karolina.check.databinding.ActivityMainBinding;
import com.karolina.check.db.VaccineDao;
import com.karolina.check.models.Vaccine;
import com.karolina.check.notify.NotificationReceiver;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ItemAdapter.OnItemClickAdapter, View.OnLongClickListener {

    private static final int ALARM_TIME_HOUR = 15;
    private static final int ALARM_TIME_MINUTE = 25;
    private static final int ALARM_TIME_SECOND = 0;

    int hour;
    int minute;

    ActivityMainBinding binding;
    PendingIntent pendingIntent;
    VaccineDao vaccineDao;
    ItemAdapter adapter;
    Vaccine nextVaccine;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("preferencias",MODE_PRIVATE);
        hour = preferences.getInt("notiHour", 7);
        minute = preferences.getInt("notMinute", 0);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vaccineDao = new VaccineDao(this);

        setSupportActionBar(binding.toolbar);
        setTitle("");
        binding.collapsingToolbar.setTitle("");
        initCollapsingToolbar();
        binding.collapsingToolbar.setTitle("");

        L.vaccineStaticList = new ArrayList<>();
        adapter = new ItemAdapter(this, L.vaccineStaticList, this);

        binding.include.list.setLayoutManager(new LinearLayoutManager(this));
        binding.include.list.setItemAnimator(new DefaultItemAnimator());
        binding.include.setAdapter(adapter);

        binding.fab.setOnClickListener(this);
        binding.fabNot.setOnClickListener(this);
        binding.fabNot.setOnLongClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initCollapsingToolbar(){

        binding.appBar.setExpanded(true);

        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1){
                    scrollRange = binding.appBar.getTotalScrollRange();
                }
                if(scrollRange + verticalOffset == 0){
                    binding.collapsingToolbar.setTitle("Vacunas");
                    isShow = true;
                    binding.fabs.setVisibility(View.GONE);
                }else if (isShow){
                    binding.collapsingToolbar.setTitle("");
                    isShow = false;
                    binding.fabs.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void loadData(){
        L.vaccineStaticList.clear();

        List<Vaccine> result = vaccineDao.getAllVaccines();
        for(Vaccine vaccine : result){
            L.vaccineStaticList.add(vaccine);
            Picasso.with(this).load(vaccine.getImage()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        nextVaccine = nextVaccine();
        binding.setVaccine(nextVaccine);
        Picasso.with(this).load(nextVaccine.getImage()).into(binding.nextImage);
        adapter.notifyDataSetChanged();
    }

    private Vaccine nextVaccine(){
        Vaccine vaccine = null;

        Calendar calendar1 = Calendar.getInstance();
        int d1 = calendar1.get(Calendar.DAY_OF_MONTH);
        int m1 = calendar1.get(Calendar.MONTH);
        int y1 = calendar1.get(Calendar.YEAR);

        for(int i=0; i<L.vaccineStaticList.size(); i++){
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(L.vaccineStaticList.get(i).getDate());
            int d2 = calendar2.get(Calendar.DAY_OF_MONTH);
            int m2 = calendar2.get(Calendar.MONTH);
            int y2 = calendar2.get(Calendar.YEAR);

            if(d2>=d1 && m2>=m1 && y2>=y1 && vaccine == null){
                vaccine = L.vaccineStaticList.get(i);
                break;
            }
        }

        if(vaccine == null){
            vaccine = new Vaccine();
        }

        return vaccine;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.fab:
                Intent intent = new Intent(this, NewVaccineActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_not:
                changeAlarmState();
                break;
        }
    }

    private void changeAlarmState(){
        if(pendingIntent == null){
            alarmOn();
        }else{
            alarmOff();
        }
    }

    private void alarmOff(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        binding.fabNot.setImageResource(R.drawable.ic_notifications_off);
        pendingIntent = null;

        Toast.makeText(this, "Notificaciones Desactivadas ", Toast.LENGTH_SHORT).show();
    }

    private void alarmOn(){

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, ALARM_TIME_SECOND);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);

        binding.fabNot.setImageResource(R.drawable.ic_notifications_on);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Notificaciones Activadas ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onIClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.fab_not:
                timePicker();
                break;
        }
        return false;
    }

    private void timePicker(){

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("notiHour",hourOfDay);
                editor.putInt("notiMinute",minute);
                editor.commit();
                Toast.makeText(MainActivity.this, "La hora de las notificaciones ha cambiado a las "+hourOfDay+":"+minute, Toast.LENGTH_SHORT).show();
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
}