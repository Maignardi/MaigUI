package io.github.maignardi.maigui.forms

/**
 * Functional validator returning `null` when [value] is valid, or an error message otherwise.
 */
public fun interface MaigValidator {
    /**
     * Validates [value] with access to the full map of current [allValues] in the form.
     */
    public fun validate(value: String, allValues: Map<String, String>): String?
}

/**
 * Built-in validation rules and Brazilian document/address/phone checksum algorithms.
 */
public object MaigValidators {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    /** Ensures the trimmed value is not empty. */
    public fun required(message: String = "Campo obrigatório"): MaigValidator =
        MaigValidator { value, _ ->
            if (value.isBlank()) message else null
        }

    /** Validates standard email format when non-blank. */
    public fun email(message: String = "E-mail inválido"): MaigValidator =
        MaigValidator { value, _ ->
            if (value.isNotEmpty() && !emailRegex.matches(value.trim())) message else null
        }

    /** Ensures length is at least [length] characters when non-empty. */
    public fun minLength(
        length: Int,
        message: String = "Deve ter pelo menos $length caracteres",
    ): MaigValidator = MaigValidator { value, _ ->
        if (value.isNotEmpty() && value.length < length) message else null
    }

    /** Ensures length does not exceed [length] characters. */
    public fun maxLength(
        length: Int,
        message: String = "Deve ter no máximo $length caracteres",
    ): MaigValidator = MaigValidator { value, _ ->
        if (value.length > length) message else null
    }

    /** Validates value against a custom [regex] when non-empty. */
    public fun pattern(
        regex: Regex,
        message: String = "Formato inválido",
    ): MaigValidator = MaigValidator { value, _ ->
        if (value.isNotEmpty() && !regex.matches(value)) message else null
    }

    /** Ensures value contains only digits (`0-9`) when non-empty. */
    public fun numeric(message: String = "Informe apenas números"): MaigValidator =
        MaigValidator { value, _ ->
            if (value.isNotEmpty() && !value.all { it.isDigit() }) message else null
        }

    /** Ensures value matches the value of [otherFieldName] in the same form. */
    public fun matches(
        otherFieldName: String,
        message: String = "Os campos não coincidem",
    ): MaigValidator = MaigValidator { value, allValues ->
        val other = allValues[otherFieldName].orEmpty()
        if (value.isNotEmpty() && value != other) message else null
    }

    /** Validates using an arbitrary [predicate] returning `true` when valid. */
    public fun custom(
        message: String = "Valor inválido",
        predicate: (String) -> Boolean,
    ): MaigValidator = MaigValidator { value, _ ->
        if (value.isNotEmpty() && !predicate(value)) message else null
    }

    /** Validates a Brazilian CPF number including both mod-11 check digits. */
    public fun cpf(message: String = "CPF inválido"): MaigValidator =
        MaigValidator { value, _ ->
            if (value.isNotEmpty() && !isValidCpf(value)) message else null
        }

    /** Validates a Brazilian CNPJ number including both mod-11 check digits. */
    public fun cnpj(message: String = "CNPJ inválido"): MaigValidator =
        MaigValidator { value, _ ->
            if (value.isNotEmpty() && !isValidCnpj(value)) message else null
        }

    /** Validates a Brazilian 8-digit CEP postal code. */
    public fun cep(message: String = "CEP inválido"): MaigValidator =
        MaigValidator { value, _ ->
            if (value.isNotEmpty() && !isValidCep(value)) message else null
        }

    /** Validates a Brazilian 10 or 11 digit phone number (with DDD). */
    public fun brazilianPhone(message: String = "Telefone inválido"): MaigValidator =
        MaigValidator { value, _ ->
            if (value.isNotEmpty() && !isValidBrazilianPhone(value)) message else null
        }

    /** Returns `true` if [raw] represents a mathematically valid CPF. */
    public fun isValidCpf(raw: String): Boolean {
        val digits = raw.filter { it.isDigit() }
        if (digits.length != 11) return false
        if (digits.all { it == digits[0] }) return false

        val nums = digits.map { it - '0' }
        val firstSum = (0 until 9).sumOf { i -> nums[i] * (10 - i) }
        val firstDigit = ((firstSum * 10) % 11).let { if (it == 10) 0 else it }
        if (nums[9] != firstDigit) return false

        val secondSum = (0 until 10).sumOf { i -> nums[i] * (11 - i) }
        val secondDigit = ((secondSum * 10) % 11).let { if (it == 10) 0 else it }
        return nums[10] == secondDigit
    }

    /** Returns `true` if [raw] represents a mathematically valid CNPJ. */
    public fun isValidCnpj(raw: String): Boolean {
        val digits = raw.filter { it.isDigit() }
        if (digits.length != 14) return false
        if (digits.all { it == digits[0] }) return false

        val nums = digits.map { it - '0' }
        val weights1 = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
        val weights2 = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

        val sum1 = (0 until 12).sumOf { i -> nums[i] * weights1[i] }
        val digit1 = (sum1 % 11).let { if (it < 2) 0 else 11 - it }
        if (nums[12] != digit1) return false

        val sum2 = (0 until 13).sumOf { i -> nums[i] * weights2[i] }
        val digit2 = (sum2 % 11).let { if (it < 2) 0 else 11 - it }
        return nums[13] == digit2
    }

    /** Returns `true` if [raw] contains exactly 8 CEP digits. */
    public fun isValidCep(raw: String): Boolean {
        val digits = raw.filter { it.isDigit() }
        return digits.length == 8
    }

    /** Returns `true` if [raw] is a valid 10 or 11 digit Brazilian phone number. */
    public fun isValidBrazilianPhone(raw: String): Boolean {
        val digits = raw.filter { it.isDigit() }
        if (digits.length !in 10..11) return false
        val ddd = digits.substring(0, 2).toIntOrNull() ?: return false
        if (ddd < 11 || ddd > 99) return false
        if (digits.all { it == digits[0] }) return false
        if (digits.length == 11 && digits[2] != '9') return false
        return true
    }
}
