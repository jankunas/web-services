package lt.kurti.defectregistry.web.rest;

import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.DEFECT_NOT_FOUND_BY_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import lt.kurti.defectregistry.domain.Defect;
import lt.kurti.defectregistry.domain.userservice.Data;
import lt.kurti.defectregistry.domain.userservice.User;
import lt.kurti.defectregistry.service.DefectService;
import lt.kurti.defectregistry.web.rest.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefectResource {

	private final DefectService defectService;

	@Autowired
	public DefectResource(final DefectService defectService) {
		this.defectService = defectService;
	}

	@GetMapping(value = "/defects", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Defect>> getDefects() {
		final List<Defect> result = defectService.getDefects();

		return ResponseEntity.ok(result);
	}

	@PostMapping(value = "/defects", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<Defect> addDefect(@RequestBody Defect defect) throws URISyntaxException {
		final Defect result = defectService.createDefect(defect);

		return ResponseEntity.created(new URI("/defects/" + result.getId()))
				.body(result);
	}

	@PutMapping(value = "/defects/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<Defect> updateDefect(@RequestBody Defect defect, @PathVariable Long id) {
		final Defect result = defectService.updateDefect(defect, id);

		return ResponseEntity.ok()
				.body(result);
	}

	@DeleteMapping(value = "/defects/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteDefect(@PathVariable Long id) {
		defectService.deleteDefectById(id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/defects/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<Defect> getDefectById(@PathVariable Long id, @RequestParam(value = "embedded", required=false) final String embedded) {
		final Optional<Defect> result = defectService.getDefectById(id, embedded);

		return result
				.map((response) -> ResponseEntity.ok().body(response))
				.orElseThrow(() -> new ResourceNotFoundException(DEFECT_NOT_FOUND_BY_ID));
	}

	@PatchMapping(value = "/defects/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<Defect> patchDefect(@RequestBody Defect defect, @PathVariable Long id) {
		final Defect result = defectService.patchDefect(defect, id);

		return ResponseEntity.ok()
				.body(result);
	}
}
