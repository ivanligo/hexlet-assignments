package exercise.dto;

// BEGIN
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
public class GuestCreateDTO {
    @NotBlank
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^\\+[0-9]{11,13}$")
    private String phoneNumber;

    @Size(min = 4, max = 4)
    private String clubCard;

    @Future
    private LocalDate cardValidUntil;
}
// END
