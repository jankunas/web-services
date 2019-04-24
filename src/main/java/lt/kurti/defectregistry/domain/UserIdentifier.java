package lt.kurti.defectregistry.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
public class UserIdentifier {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String firstName;
    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String lastName;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="defect_id")
    private Defect defect;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Defect getDefect() {
        return defect;
    }

    public void setDefect(final Defect defect) {
        this.defect = defect;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
}
