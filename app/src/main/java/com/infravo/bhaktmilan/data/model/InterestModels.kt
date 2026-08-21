package com.infravo.bhaktmilan.data.model

data class InterestRequest(
    val receiver: Int
)

data class Interest(
    val id: Int,
    val sender: Int,
    val receiver: Int,
    val status: String
)