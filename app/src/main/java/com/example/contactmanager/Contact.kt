package com.example.contactmanager

data class Contact(
    val id: Long,
    val name: String,
    val email: String,
    var isFavorite: Boolean
)
