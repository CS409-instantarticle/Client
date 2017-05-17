package com.example.rho_eojin1.a409_prototype13;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

public class MainListAdapter extends ArrayAdapter<MainListElement> {
    ArrayList<MainListElement> items;
    ImageView imageView;
    TextView textView1;
    TextView textView2;
    public MainListAdapter(Context context, int textViewResourceId, ArrayList<MainListElement> items){
        super(context, textViewResourceId, items);
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        final Context context = parent.getContext();
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.main_list_element, null);
        }
        MainListElement p = items.get(position);
        if (p != null) {
            imageView = (ImageView) v.findViewById(R.id.imageView1);
            textView1 = (TextView) v.findViewById(R.id.textView1);
            textView2 = (TextView) v.findViewById(R.id.textView2);
            if (imageView != null){
                imageView.setImageResource(R.drawable.ic_launcher);
                //Log.e("ImageTitle",p.getTitle());
                //Log.e("ImageThumbnail",p.getThumbnail());
                final String image_URL = p.getThumbnail();
                ThumbnailTask thumbnailTask = new ThumbnailTask(context, imageView);
                thumbnailTask.execute(image_URL);
            }
            if (textView1 != null){
                textView1.setText(p.getTitle());
            }
            if(textView2 != null){
                textView2.setText(p.getPress());
            }
        }

        /*
        if(position%2 == 0){
            v.setBackgroundColor(0x00FFFFFF);
        }
        else{
            v.setBackgroundColor(0x00FFFFFF);
        }*/

        return v;
    }
}