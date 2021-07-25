package com.example.notesappmvvm.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesappmvvm.R;
import com.example.notesappmvvm.activities.MainActivity;
import com.example.notesappmvvm.activities.UpdateNotesActivity;
import com.example.notesappmvvm.room.Notes;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.notesViewHolder> {

    MainActivity mainActivity;
    List<Notes> notes;
    List<Notes> allNotesItem;
    String titleShare,subTitleShare,notesShare,dateShare;
    public NotesAdapter(MainActivity mainActivity, List<Notes> notes) {
         this.mainActivity = mainActivity;
         this.notes = notes;
         allNotesItem = new ArrayList<>(notes);
    }

    public void searchNotes(List<Notes> filteredName)
    {
            this.notes = filteredName;
            notifyDataSetChanged();
    }

    @NonNull
    @Override
    public notesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new notesViewHolder(LayoutInflater.from(mainActivity).inflate(R.layout.item_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(NotesAdapter.notesViewHolder holder, int position) {

        Notes note = notes.get(position);


        holder.title.setText(note.notesTitle);
        holder.subtitle.setText(note.notesSubtitle);
        holder.notesDate.setText(note.notesDate);
        if (note.notesPriority.equals("1"))
        {
            holder.notePriority.setBackgroundResource(R.drawable.green_circle);
        }
        if (note.notesPriority.equals("2"))
        {
            holder.notePriority.setBackgroundResource(R.drawable.yellow_circle);
        }
        if (note.notesPriority.equals("3"))
        {
            holder.notePriority.setBackgroundResource(R.drawable.red_circle);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, UpdateNotesActivity.class);
            intent.putExtra("id",note.id);
            intent.putExtra("title",note.notesTitle);
            intent.putExtra("subtitle",note.notesSubtitle);
            intent.putExtra("date",note.notesDate);
            intent.putExtra("priority",note.notesPriority);
            intent.putExtra("notedata",note.notes);
            mainActivity.startActivity(intent);
            //titleShare = note.notesTitle;
        });


        holder.share.setOnClickListener(view -> {

            //creating a popup menu
            PopupMenu popup = new PopupMenu(mainActivity, holder.share);
            //inflating menu from xml resource
            popup.inflate(R.menu.share);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.share:
                        titleShare=note.notesTitle+" \n"+note.notesSubtitle+" \n"+note.notesDate+" \n"+note.notes;
                        //Toast.makeText(mainActivity, "Title: "+titleShare, Toast.LENGTH_SHORT).show();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,titleShare);
                        sendIntent.setType("text/plain");
                        mainActivity.startActivity(sendIntent);
                        return true;
                    default:
                        return false;
                }
            });
            //displaying the popup
            popup.show();

        });

    }

    @Override
    public int getItemCount() {

        return notes.size();
    }

    static class notesViewHolder extends RecyclerView.ViewHolder {

        TextView title,subtitle,notesDate,share;
        View notePriority;

        public notesViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notesTitle);
            subtitle = itemView.findViewById(R.id.notesSubtitle);
            notesDate = itemView.findViewById(R.id.notesDate);
            notePriority = itemView.findViewById(R.id.notesPriority);
            share = itemView.findViewById(R.id.textViewOptions);
        }
    }
}
