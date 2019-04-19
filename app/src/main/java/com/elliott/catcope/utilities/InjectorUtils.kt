package com.elliott.catcope.utilities

import com.elliott.catcope.data.repository.CatCopeRepository
import com.elliott.catcope.ui.CatCopeViewModelFactory

object InjectorUtils {

    fun provideCatCopeViewModelFactory() : CatCopeViewModelFactory {
        val catCopeRepository = CatCopeRepository.getInstance()
        return CatCopeViewModelFactory(catCopeRepository)
    }
}