package io.github.maignardi.maigui.forms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MaigFormStateTest {

    @Test
    fun `errors stay hidden until focus is lost or validate is called`() {
        val state = MaigFormState.build {
            field("email") {
                required("E-mail obrigatório")
                email("E-mail inválido")
            }
            field("senha") {
                required()
                minLength(8)
            }
            field("confirmacao") {
                required()
                matches("senha", "Senhas não conferem")
            }
        }

        assertFalse(state.isValid)
        assertNull(state["email"].error)

        // Focus entered then left without typing -> error appears
        state["email"].onFocusChanged(true)
        state["email"].onFocusChanged(false)
        assertEquals("E-mail obrigatório", state["email"].error)
        assertTrue(state["email"].touched)

        // Typing fixes error dynamically once touched
        state["email"].onValueChange("dev@maigui.io")
        assertNull(state["email"].error)

        // Validate triggers remaining errors
        assertFalse(state.validate())
        assertNotNull(state["senha"].error)

        state["senha"].onValueChange("12345678")
        state["confirmacao"].onValueChange("12345678")
        assertTrue(state.validate())
        assertTrue(state.isValid)

        state.reset()
        assertEquals("", state["email"].value)
        assertFalse(state["email"].touched)
        assertNull(state["email"].error)
    }

    @Test
    fun `snapshotState and restoreState preserve values and touched state across rotation`() {
        val buildBlock: MaigFormBuilder.() -> Unit = {
            field("cpf") { required(); cpf() }
        }
        val original = MaigFormState.build(buildBlock)
        original["cpf"].onValueChange("123")
        original.validate()
        assertEquals("CPF inválido", original["cpf"].error)

        val saved = original.snapshotState()
        val restored = MaigFormState.build(buildBlock).apply { restoreState(saved) }

        assertEquals("123", restored["cpf"].value)
        assertTrue(restored["cpf"].touched)
        assertEquals("CPF inválido", restored["cpf"].error)
    }
}
