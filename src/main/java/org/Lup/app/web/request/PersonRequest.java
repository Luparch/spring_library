package org.Lup.app.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PersonRequest {

    @NotNull
    private String name;

    @NotNull
    private String secondName;

    private String patronymic;

    @NotNull
    private Long birthDay;

}
