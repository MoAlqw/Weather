package com.example.dagger2.model.location.permission

interface LocationPermissionChecker {
    fun hasPermission(): Boolean
}