package org.Lup.app.web.request;

import lombok.Data;
import org.Lup.app.dto.AuthorDto;

import javax.validation.constraints.NotNull;

@Data
public class BookRequest {

    @NotNull
    private String title;

    private AuthorDto[] authors;

}
