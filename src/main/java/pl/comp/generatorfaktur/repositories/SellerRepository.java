package pl.comp.generatorfaktur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.comp.generatorfaktur.entities.SellerEntity;

public interface SellerRepository extends JpaRepository<SellerEntity, String> {
}
