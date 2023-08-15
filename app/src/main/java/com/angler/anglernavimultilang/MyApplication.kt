package com.angler.anglernavimultilang

import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import java.util.*

class MyApplication : LocalizationApplication(){
    override fun getDefaultLanguage(context: Context): Locale {
        return Locale.ENGLISH
    }

}