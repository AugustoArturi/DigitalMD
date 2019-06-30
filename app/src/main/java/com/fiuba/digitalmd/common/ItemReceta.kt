package com.fiuba.digitalmd.common

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import com.fiuba.digitalmd.Models.Receta
import com.fiuba.digitalmd.Models.QRCodeHelper
import com.fiuba.digitalmd.R
import com.google.gson.Gson
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.receta_row.view.*

open class ItemReceta(val receta: Receta?) : Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.receta_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val itemView = viewHolder.itemView

        itemView.dniPacientebox.text = "DNI Paciente: " + receta!!.dniPaciente

        itemView.obrasocialbox.text ="Obra social: " + receta.obrasocial
        itemView.diagnosticobox.text = "Diagnostico paciente: " +receta.diagnostico

        itemView.farmacobox.text = "Farmaco dado: " +receta!!.farmaco
        itemView.cantidadbox.text = "Cantidad: " +receta.cantidadFarmaco

        itemView.consumobox.text = "Modo de consumo: " +receta!!.modoConsumo
        itemView.lugarbox.text = "Se receto en: " +receta.lugar
        itemView.fechabox.text = "Fecha: " +receta!!.fecha
        itemView.idrecetabox.text= "ID receta: " + receta!!.recetaID
        itemView.matriculabox.text = "Matricula medico: " + receta!!.matricula

        val context = itemView.context
        itemView.btnGenerateQr.setOnClickListener {
            showImage(context, generateQr(context, receta)!!)
        }
    }

    private fun generateQr(context: Context, content: Receta?): Bitmap? {
        return QRCodeHelper
            .newInstance(context)
            .setContent(Gson().toJson(content))
            .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
            .setMargin(2)
            .qrCode
    }

    private fun showImage(context: Context, bitmap: Bitmap) {
        val builder = Dialog(context)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window?.setBackgroundDrawable(
            ColorDrawable(Color.BLACK)
        )
        val imageView = ImageView(context)
        imageView.setImageBitmap(bitmap)
        builder.addContentView(
            imageView, RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        builder.show()
    }
}