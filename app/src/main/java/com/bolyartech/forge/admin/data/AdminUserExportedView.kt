package com.bolyartech.forge.admin.data

data class AdminUserExportedView(
    val id: Int,
    val username: String,
    val isDisabled: Boolean,
    val isSuperUser: Boolean,
    val name: String
)
