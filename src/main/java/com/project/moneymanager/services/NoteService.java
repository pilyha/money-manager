package com.project.moneymanager.services;

import com.project.moneymanager.models.Note;

import java.util.List;

public interface NoteService {

    void addNote(Note note);

    Note findNoteById(Long id);

    void updateNote(Long id, Note note);

    void deleteNote(Long id);

    List<Note> findAllNotes();
}
