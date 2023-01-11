package org.Lup.app.web.response;

import lombok.Data;
import org.Lup.app.dto.AuthorDto;

@Data
public class BookResponse {

    private String title;
    private AuthorResponse[] authors;

}
