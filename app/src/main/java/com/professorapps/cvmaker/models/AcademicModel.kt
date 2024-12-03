package com.professorapps.cvmaker.models


class AcademicModel {
    var degree: String? = null
    var institute: String? = null
    var percgpa: String? = null
    var perradio: String? = null
    var year: String? = null
    var yearradio: String? = null



    constructor(
        str: String?,
        str2: String?,
        str3: String?,
        str4: String?,
        str5: String?,
        str6: String?
    ) {
        this.degree = str
        this.institute = str2
        this.percgpa = str3
        this.year = str4
        this.perradio = str5
        this.yearradio = str6
    }
}
