package com.fiuba.digitalmd.Farmacia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
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
import kotlinx.android.synthetic.main.activity_farmacia_landing.*


class FarmaciaLandingActivity : SignedInActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmacia_landing)
        cargarRecetasDeFirebase()
        val toolbar: Toolbar = findViewById(R.id.toolbarProfile)
        setSupportActionBar(toolbar)
        btnVenderReceta.setOnClickListener {
            val intent = Intent(this, VenderRecetaActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onBackPressed() {
        startActivity(Intent(baseContext, SignInActivity::class.java))
    }

    private fun cargarRecetasDeFirebase() {
        val adapter = GroupAdapter<ViewHolder>()
        val database = FirebaseDatabase.getInstance().getReference("/recetas/${InfoActual.getFarmaciaActual().cuit}")
            //.orderByChild("cuitFarmacia").equalTo(InfoActual.getFarmaciaActual().cuit)
        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (!p0.exists()) {
                   /* adapter.add(Vaci())
                    rvMisRecetasVendidas.adapter = adapter*/

                } else {

                    p0.children.forEach {
                        val receta = it.getValue(Receta::class.java)
                        adapter.add(ItemReceta(receta))
                    }

                    rvMisRecetasVendidas.adapter = adapter
                }
            }
        })
    }
}
