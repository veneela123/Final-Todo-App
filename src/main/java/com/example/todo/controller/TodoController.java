package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    // List all todos with optional filter
    @GetMapping
    public String list(@RequestParam(value = "filter", required = false) String filter, Model model) {
        model.addAttribute("todos", service.findAll(filter));
        model.addAttribute("filter", filter == null ? "ALL" : filter.toUpperCase());
        return "list"; // directly under templates/
    }

    // Show form to create new todo
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("todo", new Todo());
        return "form"; // directly under templates/
    }

    // Save or update todo
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("todo") Todo todo,
                       BindingResult br,
                       RedirectAttributes ra) {
        if (br.hasErrors()) {
            return "form"; // directly under templates/
        }
        service.save(todo);
        ra.addFlashAttribute("success", "Todo saved successfully");
        return "redirect:/todos";
    }

    // Show form to edit existing todo
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes ra) {
        var opt = service.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("error", "Todo not found");
            return "redirect:/todos";
        }
        model.addAttribute("todo", opt.get());
        return "form"; // directly under templates/
    }

    // Delete a todo
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.deleteById(id);
        ra.addFlashAttribute("success", "Todo deleted");
        return "redirect:/todos";
    }

    // Mark a todo as done
    @PostMapping("/done/{id}")
    public String markDone(@PathVariable Long id, RedirectAttributes ra,
                           @RequestHeader(value = "referer", required = false) String referer) {
        service.markDone(id);
        ra.addFlashAttribute("success", "Marked as done");
        return "redirect:" + (referer != null ? referer : "/todos");
    }
}
