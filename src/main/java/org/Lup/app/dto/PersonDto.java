package org.Lup.app.dto;

import lombok.Data;

@Data
public class PersonDto {

    private Integer id;
    private String name;
    private String secondName;
    private String patronymic;
    private Long birthDay;

}
