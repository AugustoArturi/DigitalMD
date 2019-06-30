package com.fiuba.digitalmd.Models

class Receta(val matricula: String,val dniPaciente: String,val obrasocial: String,val diagnostico: String,val farmaco: String, val cantidadFarmaco: String,val modoConsumo: String,val  lugar: String,val fecha: String, val recetaID: Int) {
    constructor(): this ("","","","","","","","","",0)

}

