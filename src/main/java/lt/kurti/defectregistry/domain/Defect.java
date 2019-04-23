package lt.kurti.defectregistry.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import lt.kurti.defectregistry.domain.enumeration.DefectPriority;
import lt.kurti.defectregistry.domain.enumeration.DefectStatus;
import lt.kurti.defectregistry.domain.userservice.User;

@Entity
@Table(name = "defect")
public class Defect implements Serializable {

	private static final long serialVersionUID = 1871977717947082854L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	@Enumerated(EnumType.STRING)
	private DefectPriority priority;

	@Enumerated(EnumType.STRING)
	private DefectStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false)
	private Date dateUpdated;

	@OneToMany(mappedBy = "defect")
	private List<UserIdentifier> users;

	@PrePersist
	protected void onCreate() {
		dateCreated = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		dateUpdated = new Date();
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

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(final Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public List<UserIdentifier> getUsers() {
		return users;
	}

	public void setUser(final UserIdentifier user) {
		this.users.add(user);
	}
}
