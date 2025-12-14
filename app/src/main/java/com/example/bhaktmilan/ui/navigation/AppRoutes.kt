package com.example.bhaktmilan.navigation

object AppRoutes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val PROFILE = "profile/{name}"

    fun profileRoute(name: String): String {
        return "profile/$name"
    }
}
