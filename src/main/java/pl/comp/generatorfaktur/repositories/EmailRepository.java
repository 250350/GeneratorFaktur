package pl.comp.generatorfaktur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.comp.generatorfaktur.entities.EmailUser;

@Repository
public interface EmailRepository extends JpaRepository<EmailUser, Long> {
}
