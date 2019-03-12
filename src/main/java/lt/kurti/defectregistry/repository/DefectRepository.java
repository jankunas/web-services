package lt.kurti.defectregistry.repository;

import lt.kurti.defectregistry.domain.Defect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefectRepository extends JpaRepository<Defect, Long> {
}
