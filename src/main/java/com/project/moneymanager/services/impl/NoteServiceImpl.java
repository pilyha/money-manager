package com.project.moneymanager.services.impl;

import com.project.moneymanager.models.Note;
import com.project.moneymanager.repositories.NoteRepository;
import com.project.moneymanager.services.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger LOGGER_INFO = LoggerFactory.getLogger("info");
    private static final Logger LOGGER_WARN = LoggerFactory.getLogger("warn");
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger("error");

    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void addNote(Note note) {
        if (note != null) {
            LOGGER_INFO.info("Start create note: " + note.getDescription());
            noteRepository.save(note);
            LOGGER_INFO.info("End create note: " + note.getDescription());
        } else {
            LOGGER_ERROR.error("Note is null!");
        }
    }

    public Note findNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public void deleteNote(Long id) {
        if (noteRepository.existsById(id)) {
            LOGGER_WARN.warn("Start delete note: " + id);
            noteRepository.deleteById(id);
            LOGGER_WARN.warn("End delete note: " + id);
        } else {
            LOGGER_ERROR.error("Note doesn't exists");
        }
    }

    public void updateNote(Long id, Note note) {
        if (noteRepository.existsById(id)) {
            LOGGER_WARN.warn("Start update note: " + id);
            Note newNote = findNoteById(id);
            newNote.setDescription(note.getDescription());
            noteRepository.save(newNote);
            LOGGER_WARN.warn("End update note: " + id);
        } else {
            LOGGER_ERROR.error("Note doesn't exists");
        }
    }

    public List<Note> findAllNotes() {
        LOGGER_INFO.info("Read all notes");
        return (List<Note>) noteRepository.findAll();
    }

}
