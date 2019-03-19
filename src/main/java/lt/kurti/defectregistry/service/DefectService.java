package lt.kurti.defectregistry.service;

import java.util.List;
import java.util.Optional;

import lt.kurti.defectregistry.domain.Defect;

public interface DefectService {

	Defect createDefect(Defect defect);

	Defect updateDefect(Defect defect, Long id);

	List<Defect> getDefects();

	Optional<Defect> getDefectById(Long id);

	void deleteDefectById(Long id);
}
