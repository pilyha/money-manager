package com.project.moneymanager.controllers;

import com.project.moneymanager.models.Note;
import com.project.moneymanager.models.User;
import com.project.moneymanager.services.NoteService;
import com.project.moneymanager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping(value = "/new")
    public String newNote(Principal principal, @Valid @ModelAttribute("note") Note note, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/dashboard";
        }
        User user = userService.findUserByUsername(principal.getName());
        noteService.addNote(user, note);
        return "redirect:/dashboard";
    }

    @DeleteMapping(value = "/{id}")
    public String deletePlan(Principal principal, @PathVariable("id") Long id) {
        noteService.deleteNote(id);
        return "redirect:/dashboard";
    }

}
