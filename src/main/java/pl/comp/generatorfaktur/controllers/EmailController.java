package pl.comp.generatorfaktur.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.comp.generatorfaktur.entities.EmailUserEntity;
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
        System.out.println("Saving email...");
        String email = body.get("email");
        emailRepository.save(new EmailUserEntity(email));
        System.out.println("Saving email: " + email);

        return "ok";
    }
}
