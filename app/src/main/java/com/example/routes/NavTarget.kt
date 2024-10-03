package com.example.routes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class NavTarget : Parcelable {
    @Parcelize
    object Child1 : NavTarget()
    
    @Parcelize
    object Child2 : NavTarget()
    
    @Parcelize
    object Child3 : NavTarget()
}