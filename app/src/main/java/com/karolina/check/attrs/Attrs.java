package com.karolina.check.attrs;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by RicardoAndrés on 09/05/2017.
 */

public class Attrs {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BindingAdapter("app:imgFromUrl")
    public static void loadImage(ImageView img, String url){
        if(url != null){
            if(!url.equals("")){
                Picasso.with(img.getContext())
                        .load(Uri.parse(url))
                        .into(img);
            }
        }
    }

    @BindingAdapter("app:dateToText")
    public static void loadDate(TextView txt, Date date){
        if(date != null){
            txt.setText(dateFormat.format(date));
        }else{
            txt.setText("");
        }
    }

}
