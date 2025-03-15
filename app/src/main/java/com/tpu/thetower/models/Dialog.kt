package com.tpu.thetower.models

data class Dialog  (
    val messages : List<String>,
    val speakers : List<Character>,
    val onDialogEnd: () -> Unit
)