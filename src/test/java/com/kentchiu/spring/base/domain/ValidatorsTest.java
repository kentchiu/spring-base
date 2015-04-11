package com.kentchiu.spring.base.domain;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.List;
import java.util.Map;

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

        assertThat(restError.getStatus(), is(404));
        assertThat(restError.getCode(), is("BindingException"));
        assertThat(restError.getMessage(), is("There are 2 validation errors, check content for detail"));
        Map<String, List<Map<String, Object>>> content = (Map<String, List<Map<String, Object>>>) restError.getContent();
        List<Map<String, Object>> fieldErrors = content.get("fieldErrors");
        assertThat(fieldErrors.get(0).get("field"), is("prop1"));
        assertThat(fieldErrors.get(0).get("code"), is("NotIn"));
        assertThat(fieldErrors.get(0).get("rejected"), is(""));
        assertThat(fieldErrors.get(0).get("message"), is("must one of {a,b,c}"));
    }
}