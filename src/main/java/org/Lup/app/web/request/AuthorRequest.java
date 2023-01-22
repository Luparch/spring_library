package org.Lup.app.web.request;

import lombok.Data;

@Data
public class AuthorRequest {
    private String name;

    private String secondName;

    private String patronymic;
}
