package com.example.contactmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ContactAdapter(
    private val contacts: MutableList<Contact>,
    private val onContactClick: (Contact) -> Unit,
    private val onFavoriteClick: (Contact) -> Unit,
    private val onDeleteClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var filteredContacts = contacts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = filteredContacts[position]
        holder.bind(contact, onContactClick, onFavoriteClick, onDeleteClick)
    }

    override fun getItemCount(): Int = filteredContacts.size

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactName: TextView = itemView.findViewById(R.id.contactName)
        private val contactEmail: TextView = itemView.findViewById(R.id.contactEmail)
        private val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        private val contactImage: ImageView = itemView.findViewById(R.id.contactImage)

        fun bind(
            contact: Contact,
            onContactClick: (Contact) -> Unit,
            onFavoriteClick: (Contact) -> Unit,
            onDeleteClick: (Contact) -> Unit
        ) {
            contactName.text = contact.name
            contactEmail.text = contact.email

            // Usando Glide para carregar a imagem
            if (contact.photoUri != null) {
                Glide.with(itemView.context)
                    .load(contact.photoUri)
                    .placeholder(R.drawable.ic_person_placeholder)
                    .error(R.drawable.ic_person_placeholder)
                    .into(contactImage)
            } else {
                contactImage.setImageResource(R.drawable.ic_person_placeholder)
            }

            favoriteButton.setImageResource(
                if (contact.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
            )

            itemView.setOnClickListener { onContactClick(contact) }
            favoriteButton.setOnClickListener {
                onFavoriteClick(contact)
                notifyItemChanged(adapterPosition) // Atualiza o item específico
            }
            deleteButton.setOnClickListener {
                onDeleteClick(contact)
                notifyItemRemoved(adapterPosition)
                notifyItemRangeChanged(adapterPosition, filteredContacts.size)
            }
        }
    }

    fun filter(query: String) {
        filteredContacts = contacts.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.email.contains(query, ignoreCase = true)
        }.toMutableList()
        notifyDataSetChanged()
    }

    fun addContact(contact: Contact) {
        contacts.add(contact)
        filter("") // Reaplica o filtro para incluir o novo contato
    }

    fun removeContact(contact: Contact) {
        contacts.remove(contact)
        filter("") // Reaplica o filtro para remover o contato da lista filtrada
    }

    fun updateContact(contact: Contact) {
        val index = contacts.indexOfFirst { it.id == contact.id }
        if (index != -1) {
            contacts[index] = contact
            filter("") // Reaplica o filtro para refletir as mudanças
        }
    }

    fun sortContacts() {
        filteredContacts = filteredContacts.sortedByDescending { it.isFavorite }.toMutableList()
        notifyDataSetChanged()
    }
}
