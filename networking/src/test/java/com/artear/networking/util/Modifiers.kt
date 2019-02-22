package com.artear.networking.util

import java.lang.reflect.Field
import java.lang.reflect.Modifier

@Throws(Exception::class)
fun setFinalStatic(field: Field, newValue: Any) {
    field.isAccessible = true
    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true
    modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
    field.set(null, newValue)
}