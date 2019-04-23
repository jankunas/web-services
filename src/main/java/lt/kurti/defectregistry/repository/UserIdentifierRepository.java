package lt.kurti.defectregistry.repository;

import lt.kurti.defectregistry.domain.UserIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserIdentifierRepository extends JpaRepository<UserIdentifier, Long> {
}
