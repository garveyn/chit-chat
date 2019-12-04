package com.ngarvey.chitchat.api

enum class ResponseCode(val code: Int){
    OK(1),
    UNAUTHORIZED_USER(2),
    GENERIC_ERROR(3),
    MISSING_PARAMS(4),
    BAD_EMAIL(5),
    DATABASE_ERROR(6)
}