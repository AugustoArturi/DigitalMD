package com.fiuba.digitalmd.Medico


import android.os.Bundle
import com.fiuba.digitalmd.Models.DepthPageTransformer
import com.fiuba.digitalmd.Models.Paciente
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignedInActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_swipe.*


class SwipeActivity : SignedInActivity() {
    lateinit var adapter: SwipeAdapter
    lateinit var users : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)
        fetchDataFromFirebase()
        viewpager_images.setPageTransformer(true, DepthPageTransformer())

    }

    private fun fetchDataFromFirebase() {
        users = FirebaseDatabase.getInstance().getReference("/diagnosticos")
        users.addListenerForSingleValueEvent(object : ValueEventListener {
            var listPacientes:MutableList<Paciente> = ArrayList()
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val user = it.getValue(Paciente::class.java)
                    if (deberiaMostrarDiagnostico(user))
                        listPacientes.add(user!!)
                }
                loadAdapter(listPacientes)
            }

        })
    }

    private fun deberiaMostrarDiagnostico(paciente: Paciente?): Boolean {
        return if (paciente == null) false
            else paciente.estadoDiagnostico == "En espera"
    }

    private fun loadAdapter(listPacientes: MutableList<Paciente>) {
        adapter = SwipeAdapter(this, listPacientes)
        viewpager_images.adapter = adapter

    }
}


