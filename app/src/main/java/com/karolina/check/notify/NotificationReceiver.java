package com.karolina.check.notify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.karolina.check.MainActivity;
import com.karolina.check.R;
import com.karolina.check.VaccineDescActivity;
import com.karolina.check.attrs.L;
import com.karolina.check.db.VaccineDao;
import com.karolina.check.models.Vaccine;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by RicardoAndrés on 09/05/2017.
 */

public class NotificationReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {

        Random id = new Random();

        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        for (int i=0; i<L.vaccineStaticList.size(); i++) {

            Log.i("Notificacion", L.vaccineStaticList.get(i).getName());

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(L.vaccineStaticList.get(i).getDate());

            if((calendar1.get(Calendar.YEAR) == yyyy) && (calendar1.get(Calendar.MONTH) == mm) && (calendar1.get(Calendar.DAY_OF_MONTH) == dd)){
                buildNotification(context, L.vaccineStaticList.get(i).getName(), L.vaccineStaticList.get(i).getDesc(), L.vaccineStaticList.get(i).getImage(), i, id.nextInt());
            }
        }

    }

    private void buildNotification(Context context, String nTitle, final String nText, String image, int position, int notId){

        try{
            final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent repeatingIntent = new Intent(context, VaccineDescActivity.class);
            repeatingIntent.putExtra("position", position);
            repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context,notId, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            final NotificationCompat.BigPictureStyle bpStyle = new NotificationCompat.BigPictureStyle();

            if(image != null && !image.equals("")){
                Picasso.with(context).load(image).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bpStyle.bigPicture(bitmap);
                        bpStyle.setSummaryText(nText);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.notify_icon)
                    .setTicker("Hora de la vacuna")
                    .setContentTitle(nTitle)
                    .setContentText(nText)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
                    .setStyle(bpStyle);

            notificationManager.notify(notId, builder.build());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
