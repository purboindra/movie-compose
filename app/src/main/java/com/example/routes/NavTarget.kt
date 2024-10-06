package com.example.routes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class NavTarget : Parcelable {
 
    @Parcelize
    object Main : NavTarget()
    
//    @Parcelize
//    object Home : NavTarget()
//
//
//    @Parcelize
//    object Cinemas : NavTarget()
//
//    @Parcelize
//    object Ticket : NavTarget()
//
//    @Parcelize
//    object Profile : NavTarget()
}