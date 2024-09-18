package com.example.contactmanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.SearchView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactAdapter
    private val contacts = mutableListOf<Contact>()

    private val addContactLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val contactName = data?.getStringExtra("contactName") ?: return@registerForActivityResult
                val contactEmail = data.getStringExtra("contactEmail") ?: return@registerForActivityResult
                val contactPhotoUri = data.getStringExtra("contactPhotoUri")?.let { Uri.parse(it) }

                val newContact = Contact(
                    id = (contacts.size + 1).toLong(),
                    name = contactName,
                    email = contactEmail,
                    isFavorite = false,
                    photoUri = contactPhotoUri
                )
                adapter.addContact(newContact)
                adapter.sortContacts()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener {
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }

        adapter = ContactAdapter(
            contacts = contacts,
            onContactClick = ::onContactClick,
            onFavoriteClick = ::onFavoriteClick,
            onDeleteClick = ::onDeleteClick
        )
        recyclerView.adapter = adapter

        contacts.add(Contact(1, "John Doe", "john@example.com", false))
        contacts.add(Contact(2, "Jane Smith", "jane@example.com", true))
        adapter.notifyDataSetChanged()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            addContactLauncher.launch(intent)
        }

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { adapter.filter(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { adapter.filter(it) }
                return true
            }
        })
    }

    private fun onContactClick(contact: Contact) {
        Toast.makeText(this, "Clicked on ${contact.name}", Toast.LENGTH_SHORT).show()
    }

    private fun onFavoriteClick(contact: Contact) {
        contact.isFavorite = !contact.isFavorite
        adapter.updateContact(contact)
        adapter.sortContacts()
    }

    private fun onDeleteClick(contact: Contact) {
        adapter.removeContact(contact)
        Toast.makeText(this, "${contact.name} foi deletado", Toast.LENGTH_SHORT).show()
    }
}
