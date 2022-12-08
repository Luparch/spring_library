package org.Lup.app.web.request;

import lombok.Data;

@Data
public class PersonRequest {

    private String name;
    private String secondName;
    private String patronymic;
    private Long birthDay;

}
