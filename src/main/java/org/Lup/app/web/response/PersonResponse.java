package org.Lup.app.web.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonResponse {

    private String name;
    private String secondName;
    private String patronymic;
    private LocalDate birthDay;

}
