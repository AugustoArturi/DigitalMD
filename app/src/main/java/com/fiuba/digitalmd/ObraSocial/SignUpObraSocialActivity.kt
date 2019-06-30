package com.fiuba.digitalmd.ObraSocial

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.fiuba.digitalmd.Models.ObraSocial
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up_farmacia.CircleImageViewMedico
import kotlinx.android.synthetic.main.activity_sign_up_farmacia.btnSubirFotoMedico
import kotlinx.android.synthetic.main.activity_sign_up_farmacia.emailbox
import kotlinx.android.synthetic.main.activity_sign_up_farmacia.nombrebox
import kotlinx.android.synthetic.main.activity_sign_up_farmacia.passwordbox
import kotlinx.android.synthetic.main.activity_sign_up_obra_social.*
import java.util.*


class SignUpObraSocialActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    var photoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_obra_social)
        mAuth = FirebaseAuth.getInstance()
        btnSignUpObraSocial.setOnClickListener {
            if(validarCampos())
                subirImagenAFirebase()
        }

        btnSubirFotoMedico.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }


    private fun subirDoctorAFirebase(url: String) {

        val name = nombrebox.text.toString()
        val email=emailbox.text.toString()
        val password =  passwordbox.text.toString()



        val obrasocial = ObraSocial(name, email, "obra social", url)
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    subirloAFirebase(obrasocial)
                } else {
                    Toast.makeText(baseContext, "Sign up failed", Toast.LENGTH_SHORT).show()
                }
            }


    }

    private fun subirImagenAFirebase(){
        val uid = UUID.randomUUID()
        val ref = FirebaseStorage.getInstance().getReference("/images/obra_socal/$uid")
        val progressDialog = ProgressDialog(this)

        progressDialog.setTitle("Creando nuevo perfil, espere por favor ")
        progressDialog.show()
        ref.putFile(photoUri!!)
            .addOnSuccessListener { taskSnapshot ->
                ref.downloadUrl.addOnCompleteListener { taskSnapshot ->
                    var url = taskSnapshot.result
                    Log.d("SignUp Medico", "Image added to firebase: ${url.toString()}")
                    subirDoctorAFirebase(url.toString())
                }
            }
            .addOnProgressListener { taskSnapShot ->
                val progress = 100 * taskSnapShot.bytesTransferred / taskSnapShot.totalByteCount
                progressDialog.setMessage("% ${progress}")
            }


    }

    private fun subirloAFirebase(obrasocial: ObraSocial) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/signup/$uid")
        ref.setValue(obrasocial)
            .addOnSuccessListener {
                Log.d("SignUpActivity", "User added to database")
                startActivity(Intent(this, SignInActivity::class.java))
            }

        val ref2 = FirebaseDatabase.getInstance().getReference("/listaObraSociales/$uid")
        ref2.setValue(obrasocial.nombre.toUpperCase())
            .addOnSuccessListener {
                Log.d("SignUpActivity", "Obra social added to database")
                startActivity(Intent(this, SignInActivity::class.java))
            }

    }


    private fun validarCampos(): Boolean {

        if (photoUri == null) {
            btnSubirFotoMedico.error = "Por favor elija o suba una foto"
            btnSubirFotoMedico.requestFocus()
            return false
        }
        if (nombrebox.text.toString().isEmpty()) {
            nombrebox.error = "Por favor ingresa el nombre de la farmacia"
            nombrebox.requestFocus()
            return false
        }


        if (emailbox.text.toString().isEmpty()) {
            emailbox.error = "Por favor ingresa tu email"
            emailbox.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailbox.text.toString()).matches()) {
            emailbox.error = "Por favor ingresa un email valido"
            emailbox.requestFocus()
            return false
        }

        if (passwordbox.text.toString().isEmpty()) {
            passwordbox.error = "Por favor ingresa una contraseña"
            passwordbox.requestFocus()
            return false
        }



        return true
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            photoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            CircleImageViewMedico.setImageBitmap(bitmap)
            btnSubirFotoMedico.background = null
            btnSubirFotoMedico.text = null


        }
    }
}
