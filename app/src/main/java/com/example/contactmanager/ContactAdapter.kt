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

class ContactAdapter(
    private val contacts: MutableList<Contact>,
    private val onContactClick: (Contact) -> Unit,
    private val onFavoriteClick: (Contact) -> Unit,
    private val onDeleteClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact, onContactClick, onFavoriteClick, onDeleteClick)
    }

    override fun getItemCount(): Int = contacts.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

            // Carrega a imagem do contato (se disponível)
            if (contact.photoUri != null) {
                contactImage.setImageURI(contact.photoUri)
            } else {
                contactImage.setImageResource(R.drawable.ic_person_placeholder) // Placeholder padrão
            }

            itemView.setOnClickListener { onContactClick(contact) }
            favoriteButton.setImageResource(
                if (contact.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
            )
            favoriteButton.setOnClickListener { onFavoriteClick(contact) }
            deleteButton.setOnClickListener { onDeleteClick(contact) }
        }
    }
}
