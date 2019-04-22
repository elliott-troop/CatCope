package com.elliott.catcope.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elliott.catcope.data.repository.CatCopeRepository
import com.elliott.catcope.data.repository.CatCopeRepositoryImpl

class CatCopeViewModelFactory(
    private val catCopeRepository: CatCopeRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CatCopeViewModel(catCopeRepository) as T
    }
}