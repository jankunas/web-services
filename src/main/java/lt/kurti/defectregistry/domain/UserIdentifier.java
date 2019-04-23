package lt.kurti.defectregistry.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class UserIdentifier {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

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
}
