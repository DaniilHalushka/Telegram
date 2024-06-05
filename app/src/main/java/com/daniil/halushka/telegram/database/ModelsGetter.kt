package com.daniil.halushka.telegram.database

import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.data.models.UserModel
import com.google.firebase.database.DataSnapshot

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()