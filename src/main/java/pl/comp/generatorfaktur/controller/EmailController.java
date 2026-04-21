package pl.comp.generatorfaktur.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.comp.generatorfaktur.entities.EmailUser;
import pl.comp.generatorfaktur.repositories.EmailRepository;

import java.util.Map;

@RestController
public class EmailController {

    private final EmailRepository emailRepository;

    public EmailController(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @PostMapping("/save-email")
    public String saveEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        emailRepository.save(new EmailUser(email));

        return "ok";
    }
}
