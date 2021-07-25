package com.example.notesappmvvm.room;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface NotesDao {

    @Query("select * from Notes_Database")
    LiveData<List<Notes>> getAllNotes();

    @Query("select * from Notes_Database ORDER BY notes_priority DESC")
    LiveData<List<Notes>> highToLow();

    @Query("select * from Notes_Database ORDER BY notes_priority ASC")
    LiveData<List<Notes>> lowToHigh();

    @Insert
    void insertNotes(Notes... notes);

    @Query("delete from Notes_Database where id=:id")
    void deleteNote(int id);

    @Update
    void updateNote(Notes notes);
}
