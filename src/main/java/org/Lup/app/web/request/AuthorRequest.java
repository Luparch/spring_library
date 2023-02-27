package org.Lup.app.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthorRequest {
    @NotNull
    private String name;

    private String secondName;

    private String patronymic;
}
