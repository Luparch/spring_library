package org.Lup.app.dao.person;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.Lup.app.dto.PersonDto;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class PersonDtoTestDataProvider implements ArgumentsProvider {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
            throws IOException {
        String path = extensionContext.getElement().get().getAnnotation(ArgumentsSourceJsonPath.class).value();
        FileReader fr = new FileReader(path);
        PersonDto[] arr = mapper.readValue(fr, PersonDto[].class);
        return Arrays.stream(arr).map(Arguments::of);
    }
}
