package com.zibro.fooddeliveryapp.util.event

import com.zibro.fooddeliveryapp.view.main.MainTabMenu
import kotlinx.coroutines.flow.MutableSharedFlow

class MenuChangeEventBus {
    val mainTabMenuFlow = MutableSharedFlow<MainTabMenu>()

    suspend fun changeMenu(menu : MainTabMenu) {
        mainTabMenuFlow.emit(menu)
    }
}