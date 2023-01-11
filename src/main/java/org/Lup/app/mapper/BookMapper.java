package org.Lup.app.mapper;

import org.Lup.app.dto.AuthorDto;
import org.Lup.app.dto.BookDto;
import org.Lup.app.web.request.AuthorRequest;
import org.Lup.app.web.request.BookRequest;
import org.Lup.app.web.response.AuthorResponse;
import org.Lup.app.web.response.BookResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto bookRequestToBookDto(BookRequest bookRequest);

    BookDto bookResponseToBookDto(BookResponse bookResponse);

    BookRequest bookDtoToBookRequest(BookDto bookDto);

    BookRequest bookResponseToBookRequest(BookResponse bookResponse);

    BookResponse bookRequestToBookResponse(BookRequest bookRequest);

    BookResponse bookDtoToBookResponse(BookDto bookDto);

    AuthorDto authorRequestToAuthorDto(AuthorRequest authorRequest);

    AuthorDto authorResponseToAuthorDto(AuthorResponse authorResponse);

    AuthorRequest aAuthorDtoToAuthorRequest(AuthorDto authorDto);

    AuthorRequest authorResponseToAuthorRequest(AuthorResponse authorResponse);

    AuthorResponse authorRequestToAuthorResponse(AuthorRequest authorRequest);

    AuthorResponse authorDtoToAuthorResponse(AuthorDto authorDto);

}
