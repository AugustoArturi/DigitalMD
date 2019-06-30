package com.fiuba.digitalmd.Medico

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.fiuba.digitalmd.Models.InfoActual
import com.fiuba.digitalmd.Models.Receta
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignedInActivity
import com.fiuba.digitalmd.common.ItemReceta
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_ver_recetas_medico.*
import kotlinx.android.synthetic.main.receta_row.view.*

class VerRecetasMedicoActivity : SignedInActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_recetas_medico)
        val toolbar: Toolbar = findViewById(R.id.toolbarProfile)
        toolbar.title = toolbar.title.toString()
        setSupportActionBar(toolbar)
        cargarRecetasDeFirebase()
    }

    private fun cargarRecetasDeFirebase() {
        val adapter = GroupAdapter<ViewHolder>()
        val database = FirebaseDatabase.getInstance().getReference("/recetas")
            .orderByChild("matricula").equalTo(InfoActual.getMedicoActual().matricula)

        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (!p0.exists()) {
                    adapter.add(Vaci())
                    rvMisRecetasMedico.adapter = adapter

                } else {

                    p0.children.forEach {
                        val receta = it.getValue(Receta::class.java)
                        adapter.add(ItemReceta(receta))
                    }

                    rvMisRecetasMedico.adapter = adapter
                }
            }
        })
    }
}

class Vaci() : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val itemView = viewHolder.itemView
        itemView.dniPacientebox.text = "No tenes recetas"
        itemView.obrasocialbox.text = ""
        itemView.diagnosticobox.text = ""
        itemView.matriculabox.text = ""

        itemView.farmacobox.text = ""
        itemView.cantidadbox.text = ""

        itemView.consumobox.text = ""
        itemView.lugarbox.text = ""
        itemView.fechabox.text = ""
    }

    override fun getLayout(): Int {
        return R.layout.receta_row
    }

}

