package lt.kurti.defectregistry.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lt.kurti.defectregistry.domain.enumeration.DefectPriority;
import lt.kurti.defectregistry.domain.enumeration.DefectStatus;

@Entity
@Table(name = "defect")
public class Defect {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	@Enumerated(EnumType.STRING)
	private DefectPriority priority;

	@Enumerated(EnumType.STRING)
	private DefectStatus status;

	@Temporal(TemporalType.DATE)
	private Date dateCreated;

	@PrePersist
	protected void onCreate() {
		dateCreated = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public DefectPriority getPriority() {
		return priority;
	}

	public void setPriority(final DefectPriority priority) {
		this.priority = priority;
	}

	public DefectStatus getStatus() {
		return status;
	}

	public void setStatus(final DefectStatus status) {
		this.status = status;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(final Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
