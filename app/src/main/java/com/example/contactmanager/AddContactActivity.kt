package com.example.contactmanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class AddContactActivity : AppCompatActivity() {

    private lateinit var contactImageView: ImageView
    private var selectedImageUri: Uri? = null

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                contactImageView.setImageURI(selectedImageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        contactImageView = findViewById(R.id.contactImageView)
        val selectImageButton = findViewById<Button>(R.id.selectImageButton)

        selectImageButton.setOnClickListener {
            openGallery()
        }

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            val contactName = findViewById<EditText>(R.id.contactNameEditText).text.toString()
            val contactEmail = findViewById<EditText>(R.id.contactEmailEditText).text.toString()

            if (contactName.isEmpty() || contactEmail.isEmpty()) {
                Toast.makeText(this, "Nome e Email são obrigatórios!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageLauncher.launch(intent)
    }
}
