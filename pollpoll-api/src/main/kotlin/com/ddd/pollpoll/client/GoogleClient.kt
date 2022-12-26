import com.ddd.pollpoll.domain.user.User

//package com.ddd.pollpoll.client
//
//import com.ddd.pollpoll.domain.user.User
//import org.springframework.cloud.openfeign.FeignClient
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestParam
//
//@FeignClient(name = "Google API Client", url = "https://oauth2.googleapis.com/tokeninfo")
//interface GoogleClient {
//
//    @GetMapping
//    fun getUser(@RequestParam("id_token") accessToken: String): GoogleUser
//}
//
class GoogleUser(
    private val name: String,
    val socialId: String
) {
    fun toUser(): User {
        return User(name, socialId)
    }
}
