package cz.janakdom.backend.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "damage")
public class Damage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private float financeValue;

    @Column(nullable = false, length = 100)
    private String attackedObject;

    @JsonIgnore
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "security_incident_id", nullable = false)
    private SecurityIncident securityIncident;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "fire_incident_id", nullable = false)
    private FireIncident fireIncident;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "damage_type_id", nullable = false)
    private DamageType damageType;

    public void setSecurityIncident(SecurityIncident securityIncident) throws Exception {
        if (this.securityIncident != null)
            throw new Exception("The security incident cannot be set because the premise incident is assigned!");
        this.securityIncident = securityIncident;
    }

    public void setFireIncident(FireIncident fireIncident) throws Exception {
        if (this.securityIncident != null)
            throw new Exception("The premise incident cannot be set because the security incident is assigned!");
        this.fireIncident = fireIncident;
    }
}
