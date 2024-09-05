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

            // Verifica se a URI da foto não é nula antes de carregar a imagem
            if (contact.photoUri != null) {
                try {
                    contactImage.setImageURI(contact.photoUri)
                } catch (e: Exception) {
                    // Caso ocorra algum erro ao carregar a URI da imagem, usar o placeholder
                    contactImage.setImageResource(R.drawable.ic_person_placeholder)
                }
            } else {
                contactImage.setImageResource(R.drawable.ic_person_placeholder) // Placeholder padrão
            }

            favoriteButton.setImageResource(
                if (contact.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
            )

            itemView.setOnClickListener { onContactClick(contact) }
            favoriteButton.setOnClickListener {
                onFavoriteClick(contact)
                notifyItemChanged(adapterPosition) // Notifica a mudança apenas para o item atual
            }
            deleteButton.setOnClickListener {
                onDeleteClick(contact)
                notifyItemRemoved(adapterPosition) // Notifica a remoção do item atual
            }
        }
    }
}
