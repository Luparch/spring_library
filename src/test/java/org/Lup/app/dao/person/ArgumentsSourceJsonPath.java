package org.Lup.app.dao.person;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(PersonDtoTestDataProvider.class)
@interface ArgumentsSourceJsonPath {
    String value();
}
