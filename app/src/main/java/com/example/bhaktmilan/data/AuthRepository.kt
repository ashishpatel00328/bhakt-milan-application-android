package com.example.bhaktmilan.data

object AuthRepository {

    fun checkUserExists(mobile: String): Boolean {
        // 🔴 DEMO LOGIC ONLY
        // last digit even → existing user
        // last digit odd → new user
        return mobile.last().digitToInt() % 2 == 0
    }

}
