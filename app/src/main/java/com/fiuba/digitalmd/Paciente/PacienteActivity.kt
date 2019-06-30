package com.fiuba.digitalmd.Paciente

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.fiuba.digitalmd.Models.InfoActual
import com.fiuba.digitalmd.Models.Paciente
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignInActivity
import com.fiuba.digitalmd.SignedInActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_paciente.*
import java.util.*

class PacienteActivity : SignedInActivity() {
    var uri: Uri? = null
    var photoUri: Uri? = null
    var image_uri: Uri? = null
    private val Image_CODE: Int = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente)

        btnSacarFoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    //permission was not enable
                    val permission =
                        arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, 1000)
                } else {
                    openCamera()
                }
            } else {
                openCamera()
            }

        }
        btnSubirFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        btnCConfirmarConsulta.setOnClickListener {
            if (validarDatos())
                subirDatosAFirebase()

        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, MisDiagnosticosActivity::class.java)
        startActivity(intent)
    }


    private fun validarDatos(): Boolean {
        if (etComentario.text.toString().isEmpty()) {
            etComentario.error = "Por favor ingresa un comentario"
            etComentario.requestFocus()
            return false
        }
        if (photoUri == null && image_uri == null ) {
            btnCConfirmarConsulta.error = "Por favor elija o suba una foto"
            btnCConfirmarConsulta.requestFocus()
            return false
        }

        return true
    }

    private fun subirDatosAFirebase() {


        val uid = UUID.randomUUID()

        val storage = FirebaseStorage.getInstance().getReference("/images/dermatologia/$uid")

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Estamos generando su consulta, por favor espere")
        progressDialog.show()
        storage.putFile(uri!!)
            .addOnSuccessListener { taskSnapshot ->
                storage.downloadUrl.addOnCompleteListener { taskSnapshot ->
                    var url = taskSnapshot.result
                    createUser(url.toString())
                    Log.d("ProfileAcitivity", "Image added to firebase: ${url.toString()}")
                    val intent = Intent(this, MisDiagnosticosActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            .addOnProgressListener { taskSnapShot ->
                val progress = 100 * taskSnapShot.bytesTransferred / taskSnapShot.totalByteCount
                progressDialog.setMessage("% ${progress}")
            }
    }

    private fun createUser(url: String) {
        //val fotosDisp = InfoActual.getUsuarioActual().fotosDisp
        val database = FirebaseDatabase.getInstance().getReference("/diagnosticos")
        val nombre = InfoActual.getUsuarioActual().nombre
        val apellido = InfoActual.getUsuarioActual().apellido
        val desc = etComentario.text.toString()
        val dni = InfoActual.getUsuarioActual().dni

        val key = database.push().key!!
        val paciente = Paciente(nombre, apellido,dni, url, desc, "En espera","","", key)
        database.child(key).setValue(paciente)
            .addOnSuccessListener {
                Log.d("PacienteActivity", "Paciente added to database")
            }
    }


    private fun openCamera() {
        val values = ContentValues()
        values.put (MediaStore.Images.Media.TITLE, "Nueva Foto")
        values.put (MediaStore.Images.Media.DESCRIPTION, "foto de la camara")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent,Image_CODE)

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            }
                else {
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }

        }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            photoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            CircleImageView.setImageBitmap(bitmap)
            uri = photoUri

        }

        if (resultCode == Activity.RESULT_OK && requestCode == 1001 ) {
            //image_uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image_uri)
            uri = image_uri
            CircleImageView.setImageBitmap(bitmap)
        }
    }


}
