package com.fiuba.digitalmd.ObraSocial

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.fiuba.digitalmd.Medico.Vaci
import com.fiuba.digitalmd.Models.InfoActual
import com.fiuba.digitalmd.Models.Receta
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignInActivity
import com.fiuba.digitalmd.SignedInActivity
import com.fiuba.digitalmd.common.ItemReceta
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_obra_social_landing.*


class ObraSocialLandingActivity : SignedInActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obra_social_landing)
        val toolbar: Toolbar = findViewById(R.id.toolbarProfile)
        setSupportActionBar(toolbar)
        cargarRecetasDeFirebase()
    }

    override fun onBackPressed() {
        startActivity(Intent(baseContext, SignInActivity::class.java))
    }

    private fun cargarRecetasDeFirebase() {
        val adapter = GroupAdapter<ViewHolder>()
        val database = FirebaseDatabase.getInstance().getReference("/recetas/${InfoActual.getObraSocialActual().nombre}")
            //.orderByChild("obrasocial").equalTo(InfoActual.getObraSocialActual().nombre)

        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (!p0.exists()) {
                    adapter.add(Vaci())
                    rvMisRecetasObraSocial.adapter = adapter

                } else {

                    p0.children.forEach {
                        val receta = it.getValue(Receta::class.java)
                        adapter.add(ItemReceta(receta))
                    }

                    rvMisRecetasObraSocial.adapter = adapter
                }
            }
        })
    }




}