package com.example.blinknotes.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

val clickMutex = Mutex()

fun handleClick(
    coroutineScope: CoroutineScope,
    action: suspend () -> Unit

    ) {
        coroutineScope.launch {
            clickMutex.withLock {
                action()
            }
        }
    }
