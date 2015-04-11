package com.kentchiu.spring.base.domain;

import com.google.common.collect.ImmutableMap;
import com.jayway.jsonassert.JsonAssert;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;


public class ValidatorsTest {

    @Test
    public void testBindErrors2() throws Exception {
        BindingResult bindingResult = new MapBindingResult(ImmutableMap.of("prop1", "", "prop2", "3"), "domain");
        Validators.validateNotIn(bindingResult, "prop1", "a", "b", "c");
        Validators.validateNotBlank(bindingResult, "prop1");
        Validators.validateMin(bindingResult, "prop2", 1);
        String json = Validators.bindErrors2(bindingResult);

        JsonAssert.with(json).assertEquals("$.status", 404);
        JsonAssert.with(json).assertEquals("$.code", "BindingException");
        JsonAssert.with(json).assertEquals("$.message", "There are 2 validation errors, check content for detail");
        JsonAssert.with(json).assertEquals("$.content.fieldErrors[0].field", "prop1");
        JsonAssert.with(json).assertEquals("$.content.fieldErrors[0].code", "NotIn");
        JsonAssert.with(json).assertEquals("$.content.fieldErrors[0].rejected", "");
    }
}