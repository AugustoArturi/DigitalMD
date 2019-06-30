package com.fiuba.digitalmd.Paciente

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.fiuba.digitalmd.Models.InfoActual
import com.fiuba.digitalmd.Models.Paciente
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignedInActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_mis_diagnosticos.*
import kotlinx.android.synthetic.main.diagnostico_row.view.*

class MisDiagnosticosActivity : SignedInActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_diagnosticos)

        val toolbar: Toolbar = findViewById(R.id.toolbarProfile)
        toolbar.title = InfoActual.getUsuarioActual().nombre + " " +InfoActual.getUsuarioActual().apellido
        setSupportActionBar(toolbar)

        leerDiagnosticosDeFirebase()

        btnNuevaConsulta.setOnClickListener {
            startActivity(Intent(this, PacienteActivity::class.java))
        }
        btnRefresh.setOnClickListener {
            leerDiagnosticosDeFirebase()
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
    }


    private fun leerDiagnosticosDeFirebase() {
        val adapter = GroupAdapter<ViewHolder>()
        val ref = FirebaseDatabase.getInstance().getReference("/diagnosticos")
            .orderByChild("dniPaciente").equalTo(InfoActual.getUsuarioActual().dni)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(!p0.exists()){
                    adapter.add(Vac())
                    rvMisDiagnosticos.adapter = adapter
                }
                else {
                    p0.children.forEach {
                        val paciente = it.getValue(Paciente::class.java)!!
                        adapter.add(PacienteItem(paciente, it.ref, adapter))
                    }
                    rvMisDiagnosticos.adapter = adapter
                }
           }
        })
    }

}

class PacienteItem(
    val paciente: Paciente,
    val ref: DatabaseReference,
    val adapter: GroupAdapter<ViewHolder>
) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txtResultado.text = paciente.estadoDiagnostico
        if(paciente.estadoDiagnostico == "Ir al medico") {
            viewHolder.itemView.txtResultado.setTextColor(Color.RED)
        }
        if(paciente.estadoDiagnostico == "Ok") {
            viewHolder.itemView.txtResultado.setTextColor(Color.GREEN)
        }
        viewHolder.itemView.txtDesc.text = paciente.descripcion
        viewHolder.itemView.txtComentario.text = paciente.comentarioMedico

        Picasso.get().load(paciente.urlImage).into(viewHolder.itemView.iv_photo_diagnostico)
        viewHolder.itemView.btnDelete.setOnClickListener {
            ref.removeValue()
            adapter.removeGroup(position)
        }
    }

    override fun getLayout(): Int {
        return R.layout.diagnostico_row
    }

}


class Vac() : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txtResultado.text = "No tenes diagnosticos"
        viewHolder.itemView.txtDesc.text = ""
        viewHolder.itemView.txtComentario.text = ""
    }

    override fun getLayout(): Int {
        return R.layout.diagnostico_row
    }
}

