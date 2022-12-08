package org.Lup.app.mapper;

import org.Lup.app.dto.PersonDto;
import org.Lup.app.web.request.PersonRequest;
import org.Lup.app.web.response.PersonResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDto personRequestToPersonDto(PersonRequest personRequest);

    PersonDto personResponseToPersonDto(PersonResponse personResponse);

    PersonRequest personDtoToPersonRequest(PersonDto personDto);

    PersonRequest personResponseToPersonRequest(PersonResponse personResponse);

    PersonResponse personRequestToPersonResponse(PersonRequest personRequest);

    PersonResponse personDtoToPersonResponse(PersonDto personDto);

}
