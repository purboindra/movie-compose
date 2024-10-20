package com.example.routes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class NavTarget : Parcelable {
    @Parcelize
    object Main : NavTarget()
    
    @Parcelize
    object Search : NavTarget()
    
    @Parcelize
    object Login : NavTarget()
    
    @Parcelize
    data class MovieDetail(val id: String) : NavTarget()
}