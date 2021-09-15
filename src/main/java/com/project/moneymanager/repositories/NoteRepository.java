package com.project.moneymanager.repositories;

import com.project.moneymanager.models.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note,Long> {
}
