package com.example.rho_eojin1.a409_prototype13;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

public class ContentListAdapter extends ArrayAdapter<ContentListElement> {
    ArrayList<ContentListElement> contents;
    public ContentListAdapter(Context context, int textViewResourceId, ArrayList<ContentListElement> contents){
        super(context, textViewResourceId, contents);
        this.contents = contents;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        final Context context = parent.getContext();
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.content_list_element, null);
        }

        ContentListElement p = contents.get(position);

        if (p != null) {
            //textView = (TextView) v.findViewById(R.id.textView3);
            //imageView = (ImageView) v.findViewById(R.id.imageView2);
            //videoView = (VideoView) v.findViewById(R.id.videoView);
            LinearLayout ll = (LinearLayout)v.findViewById(R.id.linearLayout);

            if (p.getType().equals("text")) {
                TextView textView = new TextView(getContext());
                ll.addView(textView);
                Log.e("Debug_Sev",p.getContent());
                textView.setText(p.getContent());
                //imageView.setEnabled(false);
                //videoView.setEnabled(false);
            } else if (p.getType().equals("image")) {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ll.addView(imageView);
                final String image_URL = p.getContent();
                ThumbnailTask thumbnailTask = new ThumbnailTask(context, imageView);
                thumbnailTask.execute(image_URL);
            } else {
                //textView.setEnabled(false);
                //imageView.setEnabled(false);
                //videoView = (VideoView) v.findViewById(R.id.videoView);
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
