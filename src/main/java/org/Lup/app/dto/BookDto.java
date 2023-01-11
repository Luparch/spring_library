package org.Lup.app.dto;

import lombok.Data;

@Data
public class BookDto {

    private Integer id;
    private String title;
    private AuthorDto[] authors;

}
