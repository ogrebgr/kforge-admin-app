package com.bolyartech.forge.admin.data

data class AdminUserExportedView(
    val id: Int,
    val username: String,
    val isDisabled: Boolean,
    val isSuperUser: Boolean,
    val name: String
) {
    companion object {
        const val PASSWORD_MIN_LENGTH = 6

        fun isValidUsername(username: String): Boolean {
            return username.matches(Regex("^[\\p{L}][\\p{L}\\p{N} _]{1,48}[\\p{L}\\p{N}]$"))
        }

        fun isValidPasswordLength(len: Int): Boolean {
            return len >= PASSWORD_MIN_LENGTH
        }
    }
}


