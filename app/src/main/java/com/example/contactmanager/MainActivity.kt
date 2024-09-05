package com.example.contactmanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactAdapter
    private val contacts = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ContactAdapter(contacts, ::onContactClick, ::onFavoriteClick, ::onDeleteClick)
        recyclerView.adapter = adapter

        // Mock data para testar
        contacts.add(Contact(1, "John Doe", "john@example.com", false))
        contacts.add(Contact(2, "Jane Smith", "jane@example.com", true))
        adapter.notifyDataSetChanged()

        // FloatingActionButton para adicionar contatos
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivityForResult(intent, ADD_CONTACT_REQUEST_CODE)
        }
    }

    // Clique no contato
    private fun onContactClick(contact: Contact) {
        Toast.makeText(this, "Clicked on ${contact.name}", Toast.LENGTH_SHORT).show()
    }

    // Clique para favoritar/desfavoritar o contato
    private fun onFavoriteClick(contact: Contact) {
        contact.isFavorite = !contact.isFavorite
        sortContacts() // Reordena a lista com favoritos no topo
        adapter.notifyDataSetChanged()
    }

    // Clique para deletar o contato
    private fun onDeleteClick(contact: Contact) {
        contacts.remove(contact) // Remove o contato da lista
        sortContacts()
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "${contact.name} foi deletado", Toast.LENGTH_SHORT).show()
    }

    // Função para ordenar a lista com os favoritos no topo
    private fun sortContacts() {
        contacts.sortWith(compareByDescending<Contact> { it.isFavorite }.thenBy { it.name })
    }

    // Adiciona novo contato ao resultado da AddContactActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val contactName = data?.getStringExtra("contactName") ?: return
            val contactEmail = data.getStringExtra("contactEmail") ?: return
            val contactPhotoUri = data.getStringExtra("contactPhotoUri")?.let { Uri.parse(it) }

            val newContact = Contact(
                id = (contacts.size + 1).toLong(),
                name = contactName,
                email = contactEmail,
                isFavorite = false,
                photoUri = contactPhotoUri // Adiciona a imagem ao contato
            )
            contacts.add(newContact)
            sortContacts()
            adapter.notifyDataSetChanged()
        }
    }


    companion object {
        const val ADD_CONTACT_REQUEST_CODE = 1
    }
}
