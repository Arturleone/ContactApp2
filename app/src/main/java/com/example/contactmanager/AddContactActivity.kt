package com.example.contactmanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class AddContactActivity : AppCompatActivity() {

    private lateinit var contactImageView: ImageView
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        contactImageView = findViewById(R.id.contactImageView)
        val selectImageButton = findViewById<Button>(R.id.selectImageButton)

        // Clique para abrir a galeria
        selectImageButton.setOnClickListener {
            openGallery()
        }

        // Clique para adicionar contato
        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            val contactName = findViewById<EditText>(R.id.contactNameEditText).text.toString()
            val contactEmail = findViewById<EditText>(R.id.contactEmailEditText).text.toString()

            val data = Intent().apply {
                putExtra("contactName", contactName)
                putExtra("contactEmail", contactEmail)
                selectedImageUri?.let {
                    putExtra("contactPhotoUri", it.toString())
                }
            }
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    // Função para abrir a galeria
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    // Lida com o resultado da seleção de imagem
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            contactImageView.setImageURI(selectedImageUri) // Exibe a imagem selecionada
        }
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 2
    }
}
