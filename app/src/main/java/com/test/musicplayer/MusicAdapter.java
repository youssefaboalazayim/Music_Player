package com.test.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<Song> {


    public MusicAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        TextView songTitle = convertView.findViewById(R.id.songTitle);
        TextView songArtist = convertView.findViewById(R.id.songArtist);
        Song song = getItem(position);

        if (song != null){
            songTitle.setText(song.getName());
        }

        if (song != null){
            songArtist.setText(song.getArtist());
        }


        return convertView;
    }
}
