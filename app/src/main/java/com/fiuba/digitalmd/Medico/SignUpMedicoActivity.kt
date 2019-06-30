package com.fiuba.digitalmd.Medico

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
import com.fiuba.digitalmd.Models.Medico
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignInActivity
import com.fiuba.digitalmd.Models.ValidacionUtils.validarNoVacio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up_medico.*
import kotlinx.android.synthetic.main.activity_sign_up_paciente.apellidobox
import kotlinx.android.synthetic.main.activity_sign_up_paciente.emailbox
import kotlinx.android.synthetic.main.activity_sign_up_paciente.nombrebox
import kotlinx.android.synthetic.main.activity_sign_up_paciente.obrasocialbox
import kotlinx.android.synthetic.main.activity_sign_up_paciente.passwordbox
import java.util.*

class SignUpMedicoActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    var photoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_medico)
        mAuth = FirebaseAuth.getInstance()
        btnSignUpDoctor.setOnClickListener {
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
        val apellido = apellidobox.text.toString()
        val matricula =matriculabox.text.toString()
        val email=emailbox.text.toString()
        val password =  passwordbox.text.toString()
        val obrasocial =  obrasocialbox.text.toString()

        val medico = Medico(name, apellido, matricula, email, obrasocial, "medico", url)
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    subirloAFirebase(medico)
                } else {
                    Toast.makeText(baseContext, "Sign up failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun subirImagenAFirebase(){
        val uid = UUID.randomUUID()
        val ref = FirebaseStorage.getInstance().getReference("/images/Medicos/$uid")
        val progressDialog = ProgressDialog(this)

        progressDialog.setTitle("Creando nuevo perfil de medico ")
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

    private fun subirloAFirebase(medico: Medico) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/signup/$uid")
        ref.setValue(medico)
            .addOnSuccessListener {
                Log.d("SignUpActivity", "User added to database")
                startActivity(Intent(this, SignInActivity::class.java))
            }

    }


    private fun validarCampos(): Boolean {
        if (validarNoVacio(nombrebox, "Por favor ingresa tu nombre")) return false
        if (validarNoVacio(apellidobox, "Por favor ingresa tu apellido")) return false
        if (validarNoVacio(matriculabox, "Por favor ingresa tu matricula")) return false
        if (validarNoVacio(emailbox, "Por favor ingresa tu email")) return false
        if (validarNoVacio(passwordbox, "Por favor ingresa una contraseña")) return false
        if (validarNoVacio(obrasocialbox, "Por favor ingresa tu obra social")) return false

        if (!Patterns.EMAIL_ADDRESS.matcher(emailbox.text.toString()).matches()) {
            emailbox.error = "Por favor ingresa un email valido"
            emailbox.requestFocus()
            return false
        }
        if (passwordbox.text.toString().length < 6) {
            passwordbox.error = "El password debe tener al menos 6 caracteres"
            passwordbox.requestFocus()
            return false
        }
        if (photoUri == null) {
            btnSubirFotoMedico.error = "Por favor elija o suba una foto"
            btnSubirFotoMedico.requestFocus()
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
