package com.kentchiu.spring.base.domain;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class OptionsValidatorTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void testLicense_options_red() throws Exception {
        Car car = new Car();
        car.setColors(new String[]{"red"});
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testLicense_options_red_white_black_are_all_validated() throws Exception {
        Car car = new Car();
        car.setColors(new String[]{"red", "white", "black"});
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testLicense_options_blue_and_red() throws Exception {
        Car car = new Car();
        car.setColors(new String[]{"blue", "red"});
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(1, constraintViolations.size());
        assertEquals("must in [white, black, red]", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testLicense_options_blue() throws Exception {
        Car car = new Car();
        car.setColors(new String[]{"blue"});
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(1, constraintViolations.size());
        assertEquals("must in [white, black, red]", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testLicense_options_null_is_acceptable() throws Exception {
        Car car = new Car();
        car.setColors(null);
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testLicense_options_empty_array_is_acceptable() throws Exception {
        Car car = new Car();
        car.setColors(new String[]{});
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(0, constraintViolations.size());
    }

}