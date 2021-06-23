package com.example.notesappmvvm.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.notesappmvvm.room.Notes;
import com.example.notesappmvvm.room.NotesDao;
import com.example.notesappmvvm.room.NotesDatabase;

import java.util.List;

public class NotesRepository {

    public NotesDao notesDao;

    public LiveData<List<Notes>> getAllNotes;

    public NotesRepository(Application application)
    {
        NotesDatabase database=NotesDatabase.getDatabaseInstance(application);
        notesDao=database.notesDao();
        getAllNotes = notesDao.getAllNotes();
    }

    void insertNotes(Notes notes)
    {
        notesDao.insertNotes(notes);
    }

    void deleteNote(int id)
    {
        notesDao.deleteNote(id);
    }

    void updateNote(Notes notes)
    {
        notesDao.updateNote(notes);
    }


}
