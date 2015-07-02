package com.kentchiu.spring.base.domain;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class OptionValidatorTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testLicense_option_white() throws Exception {
        Car car = new Car();
        car.setColor("white");
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(0, constraintViolations.size());

    }

    @Test
    public void testLicense_option_black() throws Exception {
        Car car = new Car();
        car.setColor("black");
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testLicense_option_blue() throws Exception {
        Car car = new Car();
        car.setColor("blue");
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "must one of [white, black, red]",
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testLicense_option_blank() throws Exception {
        Car car = new Car();
        car.setColor("");
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testLicense_option_null() throws Exception {
        Car car = new Car();
        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);
        assertEquals(0, constraintViolations.size());
    }


}