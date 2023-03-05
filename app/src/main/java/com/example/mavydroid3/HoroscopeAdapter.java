package com.example.mavydroid3;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HoroscopeAdapter extends ArrayAdapter<Horoscope> {
    public HoroscopeAdapter(@NonNull Context context, int resource, ArrayList<Horoscope> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.horoscope, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textView = convertView.findViewById(R.id.textView);

        imageView.setClipToOutline(true);

        Horoscope horoscope = (Horoscope) getItem(position);

        if (horoscope.hasUriAvailable()) {
            imageView.setImageURI(Uri.parse(horoscope.getUriString()));
        } else {
            imageView.setImageResource(horoscope.getResource());
        }

        textView.setText(horoscope.getName());

        return convertView;
    }
}
