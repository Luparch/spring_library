package org.Lup.app.web.request;

import lombok.Data;
import org.Lup.app.dto.AuthorDto;

@Data
public class BookRequest {

    private String title;
    private AuthorDto[] authors;

}
