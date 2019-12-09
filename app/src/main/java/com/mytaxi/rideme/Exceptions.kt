package com.mytaxi.rideme

@Suppress("UNUSED")
class UnauthorizedException : Exception()

class IncorrectParameterException(rawObject: Any) :
    RuntimeException("Incorrect parameter found at: $rawObject")
