package com.professorapps.cvmaker.models


class ResumeSampleModel {
     var frm1 = 0
     var frm2 = 0

    constructor()

    constructor(i: Int, i2: Int) {
        this.frm1 = i
        this.frm2 = i2
    }

    fun getfrm1(): Int {
        return this.frm1
    }

    fun getfrm2(): Int {
        return this.frm2
    }

    fun setfrm1(i: Int) {
        this.frm1 = i
    }

    fun setfrm2(i: Int) {
        this.frm2 = i
    }
}
