package io.github.maignardi.maigui.forms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester

/**
 * DSL scope for defining validation rules on a single [MaigFormField].
 */
public class MaigFieldBuilder internal constructor(
    private val initialValue: String = "",
) {
    private val validators = mutableListOf<MaigValidator>()

    /** Adds a [MaigValidators.required] rule. */
    public fun required(message: String = "Campo obrigatório") {
        validators += MaigValidators.required(message)
    }

    /** Adds a [MaigValidators.email] rule. */
    public fun email(message: String = "E-mail inválido") {
        validators += MaigValidators.email(message)
    }

    /** Adds a [MaigValidators.minLength] rule. */
    public fun minLength(
        length: Int,
        message: String = "Deve ter pelo menos $length caracteres",
    ) {
        validators += MaigValidators.minLength(length, message)
    }

    /** Adds a [MaigValidators.maxLength] rule. */
    public fun maxLength(
        length: Int,
        message: String = "Deve ter no máximo $length caracteres",
    ) {
        validators += MaigValidators.maxLength(length, message)
    }

    /** Adds a [MaigValidators.pattern] rule. */
    public fun pattern(regex: Regex, message: String = "Formato inválido") {
        validators += MaigValidators.pattern(regex, message)
    }

    /** Adds a [MaigValidators.numeric] rule. */
    public fun numeric(message: String = "Informe apenas números") {
        validators += MaigValidators.numeric(message)
    }

    /** Adds a [MaigValidators.matches] cross-field equality rule. */
    public fun matches(
        otherFieldName: String,
        message: String = "Os campos não coincidem",
    ) {
        validators += MaigValidators.matches(otherFieldName, message)
    }

    /** Adds a custom predicate validator rule. */
    public fun custom(
        message: String = "Valor inválido",
        predicate: (String) -> Boolean,
    ) {
        validators += MaigValidators.custom(message, predicate)
    }

    /** Adds a Brazilian CPF checksum validator. */
    public fun cpf(message: String = "CPF inválido") {
        validators += MaigValidators.cpf(message)
    }

    /** Adds a Brazilian CNPJ checksum validator. */
    public fun cnpj(message: String = "CNPJ inválido") {
        validators += MaigValidators.cnpj(message)
    }

    /** Adds a Brazilian CEP postal code validator. */
    public fun cep(message: String = "CEP inválido") {
        validators += MaigValidators.cep(message)
    }

    /** Adds a Brazilian phone validator. */
    public fun phone(message: String = "Telefone inválido") {
        validators += MaigValidators.brazilianPhone(message)
    }

    /** Adds an arbitrary [MaigValidator] instance. */
    public fun validator(validator: MaigValidator) {
        validators += validator
    }

    internal fun build(name: String, owner: MaigFormState): MaigFormField = MaigFormField(
        name = name,
        initialValue = initialValue,
        validators = validators.toList(),
        owner = owner,
    )
}

/**
 * DSL scope for declaring fields in [rememberMaigForm].
 */
public class MaigFormBuilder internal constructor() {
    internal data class FieldSpec(
        val name: String,
        val initialValue: String,
        val builder: MaigFieldBuilder,
    )

    internal val specs = mutableListOf<FieldSpec>()

    /**
     * Registers a form field named [name] with optional [initialValue] and validation rules.
     */
    public fun field(
        name: String,
        initialValue: String = "",
        block: MaigFieldBuilder.() -> Unit = {},
    ) {
        val builder = MaigFieldBuilder(initialValue).apply(block)
        specs += FieldSpec(name = name, initialValue = initialValue, builder = builder)
    }
}

/**
 * Reactive state holder for a single field inside [MaigFormState].
 */
@Stable
public class MaigFormField internal constructor(
    public val name: String,
    private val initialValue: String,
    private val validators: List<MaigValidator>,
    internal val owner: MaigFormState,
) {
    /** Current field value. */
    public var value: String by mutableStateOf(initialValue)
        private set

    /** Validation error message shown once the field has lost focus or [validate] was invoked. */
    public var error: String? by mutableStateOf(null)
        private set

    /** Whether the user has blurred this field or form validation has been triggered. */
    public var touched: Boolean by mutableStateOf(false)
        private set

    /** `true` if the current [value] passes all validators (independent of [touched]). */
    public val isValid: Boolean
        get() = computeError() == null

    /** FocusRequester used by MaigUI form fields for automatic `Next` navigation. */
    public val focusRequester: FocusRequester = FocusRequester()

    private var hadFocus = false

    /** Updates [value] and refreshes [error] if already [touched]. */
    public fun onValueChange(newValue: String) {
        value = newValue
        if (touched) {
            error = computeError()
        }
        owner.revalidateDependentFields(changedField = name)
    }

    /** Tracks focus transitions so validation error appears when focus leaves the field. */
    public fun onFocusChanged(isFocused: Boolean) {
        if (isFocused) {
            hadFocus = true
        } else if (hadFocus) {
            touched = true
            error = computeError()
        }
    }

    /** Marks the field as touched and returns `true` if valid. */
    public fun validate(): Boolean {
        touched = true
        val err = computeError()
        error = err
        return err == null
    }

    /** Resets [value], clears [error], and resets [touched] state. */
    public fun reset() {
        value = initialValue
        error = null
        touched = false
        hadFocus = false
    }

    internal fun restore(savedValue: String, savedTouched: Boolean) {
        value = savedValue
        touched = savedTouched
        error = if (savedTouched) computeError() else null
    }

    internal fun revalidateIfTouched() {
        if (touched) {
            error = computeError()
        }
    }

    internal fun moveFocusNext(): Boolean = owner.moveFocusAfter(name)

    internal fun isLastField(): Boolean = owner.isLastField(name)

    private fun computeError(): String? {
        val snapshot = owner.values
        for (validator in validators) {
            val message = validator.validate(value, snapshot)
            if (message != null) return message
        }
        return null
    }
}

/**
 * State holder managing all declared [MaigFormField]s, aggregate validity, validation,
 * reset, automatic focus ordering, and rotation state restoration.
 */
@Stable
public class MaigFormState internal constructor() {
    private val orderedFields = mutableListOf<MaigFormField>()
    private val fieldMap = linkedMapOf<String, MaigFormField>()

    internal fun addField(field: MaigFormField) {
        orderedFields += field
        fieldMap[field.name] = field
    }

    /** Returns the [MaigFormField] registered under [name]. */
    public operator fun get(name: String): MaigFormField =
        fieldMap[name] ?: throw IllegalArgumentException("Form field '$name' is not registered.")

    /** Snapshot map of all field names to their current values. */
    public val values: Map<String, String>
        get() = orderedFields.associate { it.name to it.value }

    /** `true` if every registered field currently passes all its validators. */
    public val isValid: Boolean
        get() = orderedFields.all { it.isValid }

    /** Validates every field, reveals all errors, and returns `true` iff the whole form is valid. */
    public fun validate(): Boolean {
        var allValid = true
        for (field in orderedFields) {
            if (!field.validate()) {
                allValid = false
            }
        }
        return allValid
    }

    /** Resets every field to its initial value and clears all errors. */
    public fun reset() {
        orderedFields.forEach { it.reset() }
    }

    internal fun revalidateDependentFields(changedField: String) {
        orderedFields.forEach { field ->
            if (field.name != changedField) {
                field.revalidateIfTouched()
            }
        }
    }

    internal fun isLastField(name: String): Boolean = orderedFields.lastOrNull()?.name == name

    internal fun moveFocusAfter(name: String): Boolean {
        val idx = orderedFields.indexOfFirst { it.name == name }
        if (idx >= 0 && idx + 1 < orderedFields.size) {
            orderedFields[idx + 1].focusRequester.requestFocus()
            return true
        }
        return false
    }

    internal fun snapshotState(): List<String> = buildList {
        orderedFields.forEach { field ->
            add(field.name)
            add(field.value)
            add(field.touched.toString())
        }
    }

    internal fun restoreState(saved: List<String>) {
        var i = 0
        while (i + 2 < saved.size) {
            val name = saved[i]
            val savedVal = saved[i + 1]
            val savedTouched = saved[i + 2].toBoolean()
            fieldMap[name]?.restore(savedVal, savedTouched)
            i += 3
        }
    }

    internal companion object {
        fun build(block: MaigFormBuilder.() -> Unit): MaigFormState {
            val builder = MaigFormBuilder().apply(block)
            val state = MaigFormState()
            builder.specs.forEach { spec ->
                state.addField(spec.builder.build(spec.name, state))
            }
            return state
        }

        fun saver(block: MaigFormBuilder.() -> Unit): Saver<MaigFormState, Any> = listSaver(
            save = { it.snapshotState() },
            restore = { saved ->
                build(block).apply {
                    restoreState(saved.filterIsInstance<String>())
                }
            },
        )
    }
}

/**
 * Creates and remembers a [MaigFormState] surviving configuration changes (screen rotation).
 */
@Composable
public fun rememberMaigForm(
    block: MaigFormBuilder.() -> Unit,
): MaigFormState {
    return rememberSaveable(saver = MaigFormState.saver(block)) {
        MaigFormState.build(block)
    }
}
