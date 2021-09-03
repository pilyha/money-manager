package com.project.moneymanager.services;

import com.project.moneymanager.models.Note;
import com.project.moneymanager.models.Plan;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void addNote(User u, Note note) {
        note.setUser(u);
        noteRepository.save(note);
    }

    public Note findNoteByID(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public void updateNote(Long id, Note note) {
        Note newNote = findNoteByID(id);
        newNote.setDescription(note.getDescription());
        noteRepository.save(newNote);
    }

    public List<Note> findAllNotes() {
        return (List<Note>) noteRepository.findAll();
    }

}
