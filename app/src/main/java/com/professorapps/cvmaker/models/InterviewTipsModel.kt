package com.professorapps.cvmaker.models


class InterviewTipsModel {
    var desc: String? = null
    var title: String? = null

    constructor(str: String?, str2: String?) {
        this.title = str
        this.desc = str2
    }
}
