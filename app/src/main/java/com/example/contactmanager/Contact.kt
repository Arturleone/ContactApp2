package com.example.contactmanager

import android.net.Uri

data class Contact(
    val id: Long,
    val name: String,
    val email: String,
    var isFavorite: Boolean,
    var photoUri: Uri? = null // Adiciona campo para armazenar a URI da imagem
)
