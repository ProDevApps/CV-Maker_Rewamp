package com.professorapps.cvmaker.models


class ExperienceModel {
    var designation: String? = null
    var empradio: String? = null
    var fromtime: String? = null
    var organization: String? = null
    var role: String? = null
    var totime: String? = null

    constructor()

    constructor(
        str: String?,
        str2: String?,
        str3: String?,
        str4: String?,
        str5: String?,
        str6: String?
    ) {
        this.organization = str
        this.designation = str2
        this.fromtime = str3
        this.totime = str4
        this.empradio = str5
        this.role = str6
    }
}
