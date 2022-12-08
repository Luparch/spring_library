package org.Lup.app.web.response;

import lombok.Data;

@Data
public class PersonResponse {

    private String name;
    private String secondName;
    private String patronymic;
    private Long birthDay;

}
