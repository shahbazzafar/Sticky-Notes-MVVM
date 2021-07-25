package com.example.notesappmvvm.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.example.notesappmvvm.R;
import com.example.notesappmvvm.databinding.ActivityInsertNotesBinding;
import com.example.notesappmvvm.room.Notes;
import com.example.notesappmvvm.viewmodel.NotesViewModel;
import java.util.Date;
import java.util.Objects;

public class InsertNotesActivity extends AppCompatActivity {

    ActivityInsertNotesBinding binding;
    NotesViewModel notesViewModel;
    String priority = "1";
    int num1=0,num2=0;
    Boolean aBoolean = false;
    Boolean bBOOLEAN = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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

        binding.underline.setOnClickListener(v -> {
            num1=1;
            if (aBoolean==false)
            {
                String x = binding.txtSubjectLayout.getEditText().getText().toString();
                binding.txtSubject.setText(Html.fromHtml("<u>"+x+"</u>"));
                aBoolean = true;
            }
            else if (aBoolean == true)
            {
                String x = binding.txtSubjectLayout.getEditText().getText().toString();
                binding.txtSubject.setText(Html.fromHtml(x));
                aBoolean = false;
            }
//            if (num2==1)
//            {
//                String x = binding.txtSubjectLayout.getEditText().getText().toString();
//                binding.txtSubject.setText(Html.fromHtml("<b><u>"+x+"</u></b>"));
//            }
//            else
//            {
//                aBoolean = true;
//                String x = binding.txtSubjectLayout.getEditText().getText().toString();
//                binding.txtSubject.setText(Html.fromHtml("<u>"+x+"</u>"));
//            }

        });

        binding.bold.setOnClickListener(v -> {
            num2=1;
            if (bBOOLEAN==false)
            {
                String x = binding.txtSubjectLayout.getEditText().getText().toString();
                binding.txtSubject.setText(Html.fromHtml("<b>"+x+"</b>"));
                bBOOLEAN = true;
            }
            else if (bBOOLEAN==true)
            {
                String x = binding.txtSubjectLayout.getEditText().getText().toString();
                binding.txtSubject.setText(Html.fromHtml(x));
                bBOOLEAN = false;
            }
//            if (num1==1)
//            {
//                String x = binding.txtSubjectLayout.getEditText().getText().toString();
//                binding.txtSubject.setText(Html.fromHtml("<u><b>"+x+"</b></u>"));
//            }
//            else
//            {
//                String x = binding.txtSubjectLayout.getEditText().getText().toString();
//                binding.txtSubject.setText(Html.fromHtml("<b>"+x+"</b>"));
//            }

        });
        binding.copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", binding.txtSubjectLayout.getEditText().getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied Successfully", Toast.LENGTH_SHORT).show();
        });

        binding.doneNotesBtn.setOnClickListener(v -> {
            if (Objects.requireNonNull(binding.txtTitleLayout.getEditText()).getText().toString().isEmpty()) {
                binding.txtTitleLayout.setError("Title is required.Can't be empty.");
            }
//                if (binding.txtSubjectLayout.getEditText().getText().toString().isEmpty())
//                {
//                    binding.txtSubjectLayout.setError("Subject is required.Can't be empty.");
//                }
//                else
//                {
//                    binding.txtSubjectLayout.setError(null);
//                }
            if (Objects.requireNonNull(binding.txtSubtitleLayout.getEditText()).getText().toString().isEmpty()) {
                binding.txtSubtitleLayout.setError("Subtitle is required.Can't be empty.");
            } else {
                binding.txtSubtitleLayout.setError(null);
                binding.txtTitleLayout.setError(null);
                createNotes(binding.txtTitleLayout.getEditText().getText().toString(),
                        binding.txtSubtitleLayout.getEditText().getText().toString(),
                        Objects.requireNonNull(binding.txtSubjectLayout.getEditText()).getText().toString());
                finish();
            }



        });
    }

    private void createNotes(String title, String subtitle, String subject) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMMM d, yyyy", date.getTime());
        Notes notes = new Notes();
        notes.notesTitle = title;
        notes.notesSubtitle = subtitle;
        notes.notes = subject;
        notes.notesDate = sequence.toString();
        notes.notesPriority = priority;
        notesViewModel.insertNote(notes);
        Toast.makeText(this, "Note created successfully", Toast.LENGTH_SHORT).show();
    }
}