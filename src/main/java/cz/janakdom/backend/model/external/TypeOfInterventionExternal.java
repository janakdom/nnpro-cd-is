package cz.janakdom.backend.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TypeOfInterventionExternal implements Serializable {
    private String name;
}
