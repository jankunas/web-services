package lt.kurti.defectregistry.service.impl;

import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.DEFECT_NOT_FOUND_BY_ID;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import lt.kurti.defectregistry.domain.Defect;
import lt.kurti.defectregistry.repository.DefectRepository;
import lt.kurti.defectregistry.service.DefectService;
import lt.kurti.defectregistry.validation.DefectValidator;
import lt.kurti.defectregistry.web.rest.errors.ResourceNotFoundException;
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
		defectValidator.validateRequest(defect);

		return defectRepository.save(defect);
	}

	@Override
	public Defect updateDefect(final Defect defect, final Long id) {
		defectValidator.validateRequest(defect);

		final Date existingDefectCreationDate = defectRepository.findById(id)
				.map(Defect::getDateCreated)
				.orElseThrow(() -> new ResourceNotFoundException(DEFECT_NOT_FOUND_BY_ID));

		defect.setId(id);
		final Defect updatedDefect = defectRepository.save(defect);
		updatedDefect.setDateCreated(existingDefectCreationDate);

		return updatedDefect;
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
		if (defectRepository.findById(id).isPresent()) {
			defectRepository.deleteById(id);
		} else {
			throw new ResourceNotFoundException(DEFECT_NOT_FOUND_BY_ID);
		}
	}
}
