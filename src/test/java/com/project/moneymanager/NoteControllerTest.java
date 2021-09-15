package com.project.moneymanager;

import com.project.moneymanager.controllers.NoteController;
import com.project.moneymanager.models.Note;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.NoteRepository;
import com.project.moneymanager.services.NoteService;
import com.project.moneymanager.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    NoteController noteController;

    @Autowired
    NoteService noteService;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        assertThat(noteController).isNotNull();
        assertThat(noteController).isNotNull();
    }

    @Test
    void createNote() throws Exception {
        User user = userService.findByUsername("Illia");
        Note note = new Note(user, "Test");
        this.mockMvc.perform(post("/notes/new")
                        .with(user("Illia"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("description", note.getDescription()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));

        Assertions.assertTrue(noteRepository.existsById(noteService.findAllNotes().get(0).getId()));
        noteService.deleteNote(noteService.findAllNotes().get(0).getId());
    }

    @Test
    void deleteNote() throws Exception {
        User user = userService.findByUsername("Illia");
        Note note = new Note(user, "Test");
        noteService.addNote(note);
        Long id = noteService.findAllNotes().get(0).getId();
        this.mockMvc.perform(delete("/notes/" + id)
                        .with(user("Illia"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));

        Assertions.assertFalse(noteRepository.existsById(id));
    }
}
