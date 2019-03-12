package lt.kurti.defectregistry.service.impl;

import java.util.List;
import java.util.Optional;

import lt.kurti.defectregistry.domain.Defect;
import lt.kurti.defectregistry.repository.DefectRepository;
import lt.kurti.defectregistry.service.DefectService;
import lt.kurti.defectregistry.validation.DefectValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefectServiceImpl implements DefectService {

	private final DefectRepository defectRepository;

	private final DefectValidator defectValidator;

	@Autowired
	public DefectServiceImpl(final DefectRepository defectRepository, final DefectValidator defectValidator) {
		this.defectRepository = defectRepository;
		this.defectValidator = defectValidator;
	}

	@Override
	public Defect createDefect(final Defect defect) {
		defectValidator.validatePostRequest(defect);

		return defectRepository.save(defect);
	}

	@Override
	public Defect updateDefect(final Defect defect) {
		defectValidator.validateUpdateRequest(defect);

		return defectRepository.save(defect);
	}

	@Override
	public List<Defect> getDefects() {
		return defectRepository.findAll();
	}

	@Override
	public Optional<Defect> getDefectById(final Long id) {
		return defectRepository.findById(id);
	}

	@Override
	public void deleteDefectById(final Long id) {
		defectRepository.deleteById(id);
	}
}
