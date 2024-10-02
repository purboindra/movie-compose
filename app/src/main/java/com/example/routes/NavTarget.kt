package com.example.routes

sealed class NavTarget {
    object Screen1 : NavTarget()
    object Screen2 : NavTarget()
}