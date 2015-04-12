package com.kentchiu.spring.base.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Validators {

    public static final String NOT_NULL = "NotNull";
    public static final String NOT_BLANK = "NotBlank";
    public static final String MIN = "Min";
    public static final String NOT_IN = "NotIn";
    public static final String EMAIL = "Email";
    public static final String DATE_TIME_FORMAT = "DateTimeFormat";
    public static final String DUPLICATED = "Duplicated";
    public static final String ILLEGAL_API_USAGE = "IllegalApiUsage";
    public static final String RESOURCE_NOT_FOUND = "ResourceNotFound";
    public static final String UNKNOWN = "Unknown";


    private static final String NOT_BLANK_MESSAGE = "may not be empty";
    private static final String MIN_MESSAGE = "must be greater than or equal to %d";
    private static final String NOT_IN_MESSAGE = "must one of %s";
    private static final String EMAIL_MESSAGE = "not a well-formed email address";
    private static final String ASSERT_FALSE_MESSAGE = "must be false";
    private static final String ASSERT_TRUE_MESSAGE = "must be true";
    private static final String DECIMAL_MAX_MESSAGE = "must be less than ${inclusive == true ? 'or equal to ' : ''}{value}";
    private static final String DECIMAL_MIN_MESSAGE = "must be greater than ${inclusive == true ? 'or equal to ' : ''}{value}";
    private static final String DIGITS_MESSAGE = "numeric value out of bounds (<{integer} digits>.<{fraction} digits> expected)";
    private static final String FUTURE_MESSAGE = "must be in the future";
    private static final String MAX_MESSAGE = "must be less than or equal to {value}";
    private static final String NOT_NULL_MESSAGE = "may not be null";
    private static final String NULL_MESSAGE = "must be null";
    private static final String PAST_MESSAGE = "must be in the past";
    private static final String PATTERN_MESSAGE = "must match '%s'";
    private static final String DUPLICATED_MESSAGE = "the value of [%s] field is duplicated";
    private static final String SIZE_MESSAGE = "size must be between %d and %d";
    private static final String CREDIT_CARD_NUMBER_MESSAGE = "invalid credit card number";
    private static final String LENGTH_MESSAGE = "length must be between {min} and {max}";
    private static final String NOT_EMPTY_MESSAGE = "may not be empty";
    private static final String RANGE_MESSAGE = "must be between {min} and {max}";
    private static final String SAFE_HTML_MESSAGE = "may have unsafe html content";
    private static final String URL_MESSAGE = "must be a valid URL";
    private static ValidatorFactory validatorFactory;

    private Validators() {
    }


//    public static String error(String objectName, String code, String message, Object... arguments) {
//        ObjectMapper om = new ObjectMapper();
//        try {
//            ObjectError error = new ObjectError(objectName, new String[]{code}, arguments, message);
//            return om.writeValueAsString(error);
//        } catch (JsonProcessingException e) {
//            return "";
//        }
//    }

//    public static String bindErrors(BindingResult result) {
//        ObjectMapper om = new ObjectMapper();
//        try {
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            List<ObjectError> globalErrors = result.getGlobalErrors();
//            return om.writeValueAsString(ImmutableMap.of("globalErrors", globalErrors, "fieldErrors", fieldErrors));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return "convert bind error to json fail";
//        }
//    }


    public static RestError toRestError(BindingResult result) {
        ObjectMapper om = new ObjectMapper();
        List<LinkedHashMap<String, Object>> globals = result.getGlobalErrors().stream().map(e -> {
            LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
            map.put("code", e.getCode());
            map.put("message", e.getDefaultMessage());
            return map;
        }).collect(Collectors.toList());

        List<LinkedHashMap<String, Object>> fields = result.getFieldErrors().stream().map(e -> {
            LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
            map.put("field", e.getField());
            map.put("code", e.getCode());
            map.put("rejected", e.getRejectedValue());
            map.put("message", e.getDefaultMessage());
            return map;
        }).collect(Collectors.toList());
        ImmutableMap<String, List<LinkedHashMap<String, Object>>> content = ImmutableMap.of("globalErrors", globals, "fieldErrors", fields);
        String msg;
        if (result.getErrorCount() == 1) {
            msg = "There is a validation error, check content for detail";
        } else {
            msg = "There are " + result.getErrorCount() + " validation errors, check content for detail";
        }
        RestError restError = new RestError(400, "BindingException", msg);
        restError.setContent(content);
        return restError;
        //return om.writeValueAsString(restError);
    }

    public static void validateMin(Errors errors, String field, int min) {
        int value = 0;
        try {
            Object value1 = errors.getFieldValue(field);
            value = Integer.parseInt(value1.toString());
        } catch (Exception e) {
            value = 0;
        }
        if (value < min) {
            errors.rejectValue(field, MIN, new Object[]{min}, String.format(MIN_MESSAGE, min));
        }
    }

    public static void validateMin(Errors errors, String field, long min) {
        long value = 0;
        try {
            Object value1 = errors.getFieldValue(field);
            value = Long.parseLong(value1.toString());
        } catch (Exception e) {
            value = 0;
        }
        if (value < min) {
            errors.rejectValue(field, MIN, new Object[]{min}, String.format(MIN_MESSAGE, min));
        }
    }

    public static void validateMin(Errors errors, String field, float min) {
        float value = 0;
        try {
            Object value1 = errors.getFieldValue(field);
            value = Float.parseFloat(value1.toString());
        } catch (Exception e) {
            value = 0;
        }
        if (value < min) {
            errors.rejectValue(field, MIN, new Object[]{min}, String.format(MIN_MESSAGE, min));
        }
    }

    public static void validateMin(Errors errors, String field, double min) {
        double value = 0;
        try {
            Object value1 = errors.getFieldValue(field);
            value = Double.parseDouble(value1.toString());
        } catch (Exception e) {
            value = 0;
        }
        if (value < min) {
            errors.rejectValue(field, MIN, new Object[]{min}, String.format(MIN_MESSAGE, min));
        }
    }

    public static void validateEmail(Errors errors, String field) {
        Object value = errors.getFieldValue(field);
        EmailValidator emailValidator = new EmailValidator();
        if (value == null || !emailValidator.validate(value.toString())) {
            errors.rejectValue(field, EMAIL, new Object[]{}, String.format(EMAIL_MESSAGE));
        }
    }

    public static void validateBean(Errors errors, Object target) {
        if (validatorFactory == null) {
            validatorFactory = Validation.buildDefaultValidatorFactory();
        }
        SpringValidatorAdapter validator = new SpringValidatorAdapter(validatorFactory.getValidator());
        validator.validate(target, errors);
    }

    public static void validateBoolean(Errors errors, String field) {
        validateNotIn(errors, field, Boolean.TRUE.toString(), Boolean.FALSE.toString());
    }

    public static void validateNotIn(Errors errors, String field, String... values) {
        Object value = errors.getFieldValue(field);
        if (value == null || !ArrayUtils.contains(values, value.toString())) {
            errors.rejectValue(field, NOT_IN, values, String.format(NOT_IN_MESSAGE, ArrayUtils.toString(values)));
        }
    }

    public static void validateNotBlank(Errors errors, String field) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, NOT_BLANK, NOT_BLANK_MESSAGE);

    }

//    public static void validateDateStringFormat(Errors errors, String field, String pattern) {
//        Object value = errors.getFieldValue(field);
//        if (value != null) {
//            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
//            try {
//                fmt.parseDateTime(value.toString());
//            } catch (IllegalArgumentException e) {
//                errors.rejectValue(field, DATE_TIME_FORMAT, String.format(PATTERN_MESSAGE, pattern));
//            }
//        }
//
//    }

    public static void validateNestedBean(Errors errors, String nestedPath, Object nestedTarget) {
        try {
            errors.pushNestedPath(nestedPath);
            Validators.validateBean(errors, nestedTarget);
        } finally {
            errors.popNestedPath();
        }
    }

    public static void rejectDuplicated(Errors errors, String field) {
        Object value = errors.getFieldValue(field);
        errors.rejectValue(field, DUPLICATED, new Object[]{value}, String.format(DUPLICATED_MESSAGE, field));

    }

    public static void validateKey(Errors errors, String key, String... keys) {
        if (!ArrayUtils.contains(keys, key)) {
            errors.rejectValue(key, Validators.NOT_IN, keys, String.format(NOT_IN_MESSAGE, ArrayUtils.toString(keys)));
        }
    }
}

class EmailValidator {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;
    private Matcher matcher;

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validate(final String hex) {
        matcher = pattern.matcher(hex);
        return matcher.matches();

    }
}