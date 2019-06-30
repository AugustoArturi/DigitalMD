package com.fiuba.digitalmd.Models

object InfoActual {

    private var usuario_actual : User = User("","","","","","","")
    private var medico_actual : Medico = Medico("","","","","","","")
    private var obra_social_actual : ObraSocial = ObraSocial("","","","")
    private var farmacia_actual : Farmacia = Farmacia("","","","","","")
    private var receta_actual : Receta = Receta("","","","","","","","","",0)
    private var uidActual : String = ""

    fun setRecetaActual(receta: Receta){
        receta_actual = receta
    }

    fun getRecetaActual(): Receta{
        return receta_actual
    }
    fun setUsuarioActual(user : User){
        usuario_actual = user
    }

    fun getUsuarioActual():User{
        return usuario_actual
    }

    fun setMedicoActual(medico : Medico){
        medico_actual = medico
    }

    fun getMedicoActual():Medico{
        return medico_actual
    }

    fun setUidActual(uid: String) {
        uidActual = uid
    }

    fun getUidActual():String{
        return uidActual
    }

    fun setObraSocialActual(obraSocial: ObraSocial) {
        obra_social_actual = obraSocial
    }

    fun getObraSocialActual(): ObraSocial{
        return obra_social_actual
    }

    fun setFarmaciaActual(farmacia: Farmacia) {
        farmacia_actual = farmacia
    }

    fun getFarmaciaActual(): Farmacia {
        return farmacia_actual
    }

    fun logout() {
        usuario_actual = User("","","","","","","")
        medico_actual = Medico("","","","","","","")
        obra_social_actual = ObraSocial("","","","")
        farmacia_actual = Farmacia("","","","","","")
    }
}