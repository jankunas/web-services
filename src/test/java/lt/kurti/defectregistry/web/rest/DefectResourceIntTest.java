package lt.kurti.defectregistry.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import javax.persistence.EntityManager;

import lt.kurti.defectregistry.DefectRegistryApplication;
import lt.kurti.defectregistry.domain.Defect;
import lt.kurti.defectregistry.domain.enumeration.DefectPriority;
import lt.kurti.defectregistry.domain.enumeration.DefectStatus;
import lt.kurti.defectregistry.repository.DefectRepository;
import lt.kurti.defectregistry.service.DefectService;
import lt.kurti.defectregistry.util.TestUtil;
import lt.kurti.defectregistry.web.rest.errors.CustomExceptionAdvice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DefectRegistryApplication.class)
public class DefectResourceIntTest {

	private static final String DEFECT_NAME = "Test name";
	private static final String DEFECT_DESCRIPTION = "Test description";
	private static final String UPDATED_DEFECT_NAME = "Updated Test name";
	private static final String UPDATED_DEFECT_DESCRIPTION = "Updated Test description";

	private MockMvc restDefectMockMvc;

	private Defect defect;

	@Autowired
	private DefectService defectService;

	@Autowired
	private CustomExceptionAdvice customExceptionAdvice;

	@Autowired
	private DefectRepository defectRepository;

	@Autowired
	private EntityManager em;

	@Before
	public void setup() {
		final DefectResource defectResource = new DefectResource(defectService);
		this.restDefectMockMvc = MockMvcBuilders.standaloneSetup(defectResource)
				.setControllerAdvice(customExceptionAdvice)
				.build();
	}

	@Before
	public void init() {
		defect = new Defect();
		defect.setStatus(DefectStatus.NEW);
		defect.setPriority(DefectPriority.HIGH);
		defect.setName(DEFECT_NAME);
		defect.setDescription(DEFECT_DESCRIPTION);
	}

	@Test
	@Transactional
	public void createDefectWithExistingId() throws Exception {
		final int databaseSizeBeforeCreate = defectRepository.findAll().size();

		defect.setId(1L);

		restDefectMockMvc.perform(post("/defects")
				.contentType(APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(defect)))
				.andExpect(status().isBadRequest());

		final List<Defect> cartOrderList = defectRepository.findAll();
		assertThat(cartOrderList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllDefects() throws Exception {
		defectRepository.saveAndFlush(defect);

		restDefectMockMvc.perform(get("/defects?sort=id").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[-1:].id").value(hasItem(defect.getId().intValue())))
				.andExpect(jsonPath("$.[-1:].name").value(hasItem(defect.getName())))
				.andExpect(jsonPath("$.[-1:].description").value(hasItem(defect.getDescription())))
				.andExpect(jsonPath("$.[-1:].priority").value(defect.getPriority().toString()))
				.andExpect(jsonPath("$.[-1:].status").value(defect.getStatus().toString()));
	}

	@Test
	@Transactional
	public void getDefect() throws Exception {
		defectRepository.saveAndFlush(defect);

		restDefectMockMvc.perform(get("/defects/{id}", defect.getId()).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(defect.getId().intValue()))
				.andExpect(jsonPath("$.name").value(defect.getName()))
				.andExpect(jsonPath("$.description").value(defect.getDescription()))
				.andExpect(jsonPath("$.priority").value(defect.getPriority().toString()))
				.andExpect(jsonPath("$.status").value(defect.getStatus().toString()));
	}

	@Test
	@Transactional
	public void updateDefect() throws Exception {
		defectRepository.saveAndFlush(defect);

		final int databaseSizeBeforeUpdate = defectRepository.findAll().size();
		final Long defectId = defect.getId();
		final Defect updatedDefect = defectRepository.findById(defectId).get();

		em.detach(updatedDefect);
		updatedDefect.setName(UPDATED_DEFECT_NAME);
		updatedDefect.setDescription(UPDATED_DEFECT_DESCRIPTION);
		defect.setId(null);
		defect.setDateCreated(null);

		restDefectMockMvc.perform(put("/defects/{id}/", defectId)
				.contentType(APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(defect)))
				.andExpect(status().isOk());

		final List<Defect> defectList = defectRepository.findAll();
		assertThat(defectList).hasSize(databaseSizeBeforeUpdate);
		final Defect testDefect = defectList.get(defectList.size() - 1);
		assertThat(testDefect.getName()).isEqualTo(UPDATED_DEFECT_NAME);
		assertThat(testDefect.getDescription()).isEqualTo(UPDATED_DEFECT_DESCRIPTION);
	}

	@Test
	@Transactional
	public void deleteDefect() throws Exception {
		defectRepository.saveAndFlush(defect);

		final int databaseSizeBeforeDelete = defectRepository.findAll().size();

		restDefectMockMvc.perform(delete("/defects/{id}", defect.getId()).contentType(APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNoContent());

		final List<Defect> cartOrderList = defectRepository.findAll();
		assertThat(cartOrderList).hasSize(databaseSizeBeforeDelete - 1);
	}

}
