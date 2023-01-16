package org.Lup.app.dao.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Lup.app.dto.BookDto;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class BookDtoTestDataProvider implements ArgumentsProvider {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
            throws IOException {
        String path = extensionContext.getElement().get().getAnnotation(ArgumentsSourceJsonPath.class).value();
        FileReader fr = new FileReader(path);
        BookDto[] arr = mapper.readValue(fr, BookDto[].class);
        return Arrays.stream(arr).map(Arguments::of);
    }
}