package com.kentchiu.spring.base.domain;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class ValidatorsTest {

    @Test
    public void testBindErrors2() throws Exception {
        BindingResult bindingResult = new MapBindingResult(ImmutableMap.of("prop1", "", "prop2", "3"), "domain");
        Validators.validateNotIn(bindingResult, "prop1", "a", "b", "c");
        Validators.validateNotBlank(bindingResult, "prop1");
        Validators.validateMin(bindingResult, "prop2", 1);
        RestError restError = Validators.toRestError(bindingResult);

        assertThat(restError.getStatus(), is(400));
        assertThat(restError.getCode(), is("BindingException"));
        assertThat(restError.getMessage(), is("There are 2 validation errors, check content for detail"));
        Map<String, List<Map<String, Object>>> content = (Map<String, List<Map<String, Object>>>) restError.getContent();
        List<Map<String, Object>> fieldErrors = content.get("fieldErrors");
        assertThat(fieldErrors.get(0).get("field"), is("prop1"));
        assertThat(fieldErrors.get(0).get("code"), is("NotIn"));
        assertThat(fieldErrors.get(0).get("rejected"), is(""));
        assertThat(fieldErrors.get(0).get("message"), is("must one of {a,b,c}"));
    }

    @Test
    public void testValidateUuid() throws Exception {
        BindingResult bindingResult = new MapBindingResult(ImmutableMap.of("uuid", UUID.randomUUID().toString(), "uuid2", "xxx", "uuid3", ""), "domain");
        Validators.validateUuid(bindingResult, "uuid");
        Validators.validateUuid(bindingResult, "uuid2");
        Validators.validateUuid(bindingResult, "uuid3");

        assertThat(bindingResult.getFieldErrorCount(), is(1));

        assertThat(bindingResult.getFieldError("uuid2").getField(), is("uuid2"));
        assertThat(bindingResult.getFieldError("uuid2").getCode(), is("UUID"));
        assertThat(bindingResult.getFieldError("uuid2").getRejectedValue(), is("xxx"));
        assertThat(bindingResult.getFieldError("uuid2").getDefaultMessage(), is("UUID format : 8-4-4-4-12"));
    }

    @Test
    public void testValidateMac() throws Exception {
        BindingResult bindingResult = new MapBindingResult(ImmutableMap.of("mac", "9e:40:44:4d:fe:cf", "mac2", "ff:00", "mac3", ""), "domain");
        Validators.validateMac(bindingResult, "mac");
        Validators.validateMac(bindingResult, "mac2");
        Validators.validateMac(bindingResult, "mac3");

        assertThat(bindingResult.getFieldErrorCount(), is(1));

        assertThat(bindingResult.getFieldError("mac2").getField(), is("mac2"));
        assertThat(bindingResult.getFieldError("mac2").getCode(), is(Validators.MAC));
        assertThat(bindingResult.getFieldError("mac2").getRejectedValue(), is("ff:00"));
        assertThat(bindingResult.getFieldError("mac2").getDefaultMessage(), is("MAC format : 00:00:00:00:00:00"));
    }
}