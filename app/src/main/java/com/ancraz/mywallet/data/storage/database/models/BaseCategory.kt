package com.ancraz.mywallet.data.storage.database.models

abstract class BaseCategory(
    open val id: Long = 0L,
    open val name: String
)