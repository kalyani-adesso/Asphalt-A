package com.asphalt.commonui.util

import org.junit.Assert.*
import org.junit.Test

class EmailValidatorTest {
    @Test
    fun valid_email_should_return_true(){
        assertTrue(EmailValidator.isValid("test@example.com"))
        assertTrue(EmailValidator.isValid("user.name+tag@gmail.com"))
        assertTrue(EmailValidator.isValid("user123@sub.domain.com"))
    }

    @Test
    fun `invalid email should return false`() {
        assertFalse(EmailValidator.isValid("testexample.com"))   // missing @
        assertFalse(EmailValidator.isValid("test@"))             // missing domain
        assertFalse(EmailValidator.isValid("@example.com"))      // missing local part
    }
    @Test
    fun `null or blank email should return false`() {
        assertFalse(EmailValidator.isValid(null))
        assertFalse(EmailValidator.isValid(""))
        assertFalse(EmailValidator.isValid("   "))
    }
}