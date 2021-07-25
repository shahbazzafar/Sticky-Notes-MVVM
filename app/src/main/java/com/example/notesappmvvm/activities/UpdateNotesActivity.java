package com.example.notesappmvvm.activities;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.notesappmvvm.R;
import com.example.notesappmvvm.databinding.ActivityUpdateNotesBinding;
import com.example.notesappmvvm.room.Notes;
import com.example.notesappmvvm.viewmodel.NotesViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;

public class UpdateNotesActivity extends AppCompatActivity {

    ActivityUpdateNotesBinding binding;
    String priority = "1";
    String sTitle, sSubtitle, sDate, sPriority, sNoteData;
    int iid;
    NotesViewModel notesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        iid = getIntent().getIntExtra("id", 0);
        sTitle = getIntent().getStringExtra("title");
        sSubtitle = getIntent().getStringExtra("subtitle");
        sPriority = getIntent().getStringExtra("priority");
        sDate = getIntent().getStringExtra("date");
        sNoteData = getIntent().getStringExtra("notedata");

        binding.txtUpTitle.setText(sTitle);
        binding.txtUpSubtitle.setText(sSubtitle);
        binding.txtUpSubject.setText(sNoteData);
        switch (sPriority) {
            case "1":
                binding.greenPriority.setImageResource(R.drawable.ic_baseline_done_24);
                break;
            case "2":
                binding.yellowPriority.setImageResource(R.drawable.ic_baseline_done_24);
                break;
            case "3":
                binding.redPriority.setImageResource(R.drawable.ic_baseline_done_24);
                break;
        }
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        binding.greenPriority.setOnClickListener(v -> {
            binding.greenPriority.setImageResource(R.drawable.ic_baseline_done_24);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);
            priority = "1";
        });

        binding.yellowPriority.setOnClickListener(v -> {
            binding.yellowPriority.setImageResource(R.drawable.ic_baseline_done_24);
            binding.greenPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);
            priority = "2";
        });

        binding.redPriority.setOnClickListener(v -> {
            binding.redPriority.setImageResource(R.drawable.ic_baseline_done_24);
            binding.yellowPriority.setImageResource(0);
            binding.greenPriority.setImageResource(0);
            priority = "3";
        });

        binding.updateNotesBtn.setOnClickListener(v -> updateNotes(binding.txtUpTitleLayout.getEditText().getText().toString(), binding.txtUpSubtitleLayout.getEditText().getText().toString(), binding.txtUpSubjectLayout.getEditText().getText().toString()));

        binding.imgDelete.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(UpdateNotesActivity.this, R.style.BottomSheetStyle);
            View view = LayoutInflater.from(UpdateNotesActivity.this).inflate(R.layout.delete_bottom_sheet,
                    (LinearLayout) findViewById(R.id.bottomsheet));
            dialog.setContentView(view);
            dialog.show();

            TextView yes, no;
            yes = view.findViewById(R.id.delete_yes);
            no = view.findViewById(R.id.delete_no);

            yes.setOnClickListener(w -> {
                notesViewModel.deleteNote(iid);
                finish();
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        });
    }

    private void updateNotes(String title, String subtitle, String notes) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMMM d, yyyy", date.getTime());

        Notes updateNote = new Notes();
        updateNote.id = iid;
        updateNote.notesTitle = title;
        updateNote.notesSubtitle = subtitle;
        updateNote.notes = notes;
        updateNote.notesDate = sequence.toString();
        updateNote.notesPriority = priority;

        notesViewModel.updateNote(updateNote);

        Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            BottomSheetDialog dialog = new BottomSheetDialog(UpdateNotesActivity.this, R.style.BottomSheetStyle);
            View view = LayoutInflater.from(UpdateNotesActivity.this).inflate(R.layout.delete_bottom_sheet,
                    (LinearLayout) findViewById(R.id.bottomsheet));
            dialog.setContentView(view);
            dialog.show();

            TextView yes, no;
            yes = view.findViewById(R.id.delete_yes);
            no = view.findViewById(R.id.delete_no);

            yes.setOnClickListener(v -> {
                notesViewModel.deleteNote(iid);
                finish();
            });

            no.setOnClickListener(v -> dialog.dismiss());


        }
        return true;
    }


}