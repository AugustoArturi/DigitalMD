package com.fiuba.digitalmd.Paciente

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
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_mis_recetas.*


class MisRecetasActivity : SignedInActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_recetas)
        val toolbar: Toolbar = findViewById(R.id.toolbarProfile)
        toolbar.title = toolbar.title.toString() + " - Recetas"
        setSupportActionBar(toolbar)
        cargarRecetasDeFirebase()
    }

    private fun cargarRecetasDeFirebase() {
        val adapter = GroupAdapter<ViewHolder>()
        val database = FirebaseDatabase.getInstance().getReference("/recetas")
            .orderByChild("dniPaciente").equalTo(InfoActual.getUsuarioActual().dni)

        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (!p0.exists()) {
                } else {

                    p0.children.forEach {
                        val receta = it.getValue(Receta::class.java)
                        adapter.add(ItemReceta(receta))
                    }

                    rvMisRecetasPaciente.adapter = adapter
                }
            }
        })
    }
}
