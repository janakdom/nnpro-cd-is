package cz.janakdom.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {
    private String firstname;
    private String surname;
    private String username;
    private String password;
    private String email;
    private int areaId;
}
