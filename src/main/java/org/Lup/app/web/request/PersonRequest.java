package org.Lup.app.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PersonRequest {

    @NotNull
    private String name;

    private String secondName;

    private String patronymic;

    @NotNull
    private LocalDate birthDay;

}
