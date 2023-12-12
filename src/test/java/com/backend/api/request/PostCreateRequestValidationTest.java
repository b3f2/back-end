package com.backend.api.request;

import com.backend.api.request.post.PostCreateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static com.backend.api.config.factory.PostCreateRequestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PostCreateRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        //given
        PostCreateRequest req = createPostCreateRequestWithUserId(null);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        //then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyTitleTest() {
        //given
        String invalidValue = null;
        PostCreateRequest req = createPostCreateRequestWithTitle(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByBlankTitleTest() {
        //given
        String invalidValue = "";
        PostCreateRequest req = createPostCreateRequestWithTitle(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyContentTest() {
        //given
        String invalidValue = null;
        PostCreateRequest req = createPostCreateRequestWithContent(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlackContentTest() {
        //given
        String invalidValue = "";
        PostCreateRequest req = createPostCreateRequestWithContent(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNotNullUserIdTest() {
        //given
        Long invalidValue = 1L;
        PostCreateRequest req = createPostCreateRequestWithUserId(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNullCategoryIdTest() {
        //given
        Long invalidValue = null;
        PostCreateRequest req = createPostCreateRequestWithCategoryId(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativeCategoryId() {
        //given
        Long invalidValue = -1L;
        PostCreateRequest req = createPostCreateRequestWithCategoryId(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }
}
