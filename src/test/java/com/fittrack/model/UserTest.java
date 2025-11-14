package com.fittrack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the User model class.
 * Tests getter/setter functionality and data integrity.
 */
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserIdGetterSetter() {
        user.setUserId(1);
        assertEquals(1, user.getUserId(), "User ID should be 1");
    }

    @Test
    void testUsernameGetterSetter() {
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername(), "Username should be 'testuser'");
    }

    @Test
    void testEmailGetterSetter() {
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail(), "Email should be 'test@example.com'");
    }

    @Test
    void testAgeGetterSetter() {
        user.setAge(25);
        assertEquals(25, user.getAge(), "Age should be 25");
    }

    @Test
    void testAgeCanBeNull() {
        user.setAge(null);
        assertNull(user.getAge(), "Age should be able to be null");
    }

    @Test
    void testGenderGetterSetter() {
        user.setGender("Male");
        assertEquals("Male", user.getGender(), "Gender should be 'Male'");
    }

    @Test
    void testHeightGetterSetter() {
        user.setHeight(175.5);
        assertEquals(175.5, user.getHeight(), 0.01, "Height should be 175.5 cm");
    }

    @Test
    void testHeightCanBeNull() {
        user.setHeight(null);
        assertNull(user.getHeight(), "Height should be able to be null");
    }

    @Test
    void testWeightGetterSetter() {
        user.setWeight(70.5);
        assertEquals(70.5, user.getWeight(), 0.01, "Weight should be 70.5 kg");
    }

    @Test
    void testWeightCanBeNull() {
        user.setWeight(null);
        assertNull(user.getWeight(), "Weight should be able to be null");
    }

    @Test
    void testFitnessLevelGetterSetter() {
        user.setFitnessLevel("Intermediate");
        assertEquals("Intermediate", user.getFitnessLevel(), "Fitness level should be 'Intermediate'");
    }

    @Test
    void testCompleteUserProfile() {
        user.setUserId(1);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setAge(30);
        user.setGender("Male");
        user.setHeight(180.0);
        user.setWeight(75.0);
        user.setFitnessLevel("Advanced");

        assertEquals(1, user.getUserId());
        assertEquals("johndoe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals(30, user.getAge());
        assertEquals("Male", user.getGender());
        assertEquals(180.0, user.getHeight(), 0.01);
        assertEquals(75.0, user.getWeight(), 0.01);
        assertEquals("Advanced", user.getFitnessLevel());
    }

    @Test
    void testPartialUserProfile() {
        // Only required fields set
        user.setUserId(2);
        user.setUsername("janedoe");
        user.setEmail("jane@example.com");

        assertEquals(2, user.getUserId());
        assertEquals("janedoe", user.getUsername());
        assertEquals("jane@example.com", user.getEmail());
        assertNull(user.getAge());
        assertNull(user.getGender());
        assertNull(user.getHeight());
        assertNull(user.getWeight());
        assertNull(user.getFitnessLevel());
    }
}
