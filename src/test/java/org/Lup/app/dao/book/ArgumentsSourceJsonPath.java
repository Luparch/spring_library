package org.Lup.app.dao.book;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(BookDtoTestDataProvider.class)
@interface ArgumentsSourceJsonPath {
    String value();
}
