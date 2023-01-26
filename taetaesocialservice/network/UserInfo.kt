package com.taetae.taetaesocialservice.network

object UserInfo {
    var accessToken : String = ""
    var userId : String = ""
    var userEmail : String = ""

    fun clearData(){
        this.accessToken = ""
        this.userId = ""
        this.userEmail = ""
    }
}