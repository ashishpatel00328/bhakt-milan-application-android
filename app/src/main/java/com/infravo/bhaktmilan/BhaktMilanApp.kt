//package com.infravo.bhaktmilan
//
//import android.app.Application
//
//class BhaktMilanApp : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        // App level initialization
//    }
//}
package com.infravo.bhaktmilan

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BhaktMilanApp : Application()