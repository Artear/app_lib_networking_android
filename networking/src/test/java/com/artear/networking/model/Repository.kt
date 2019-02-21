package com.artear.networking.model

import com.artear.networking.contract.Networking
import com.artear.networking.model.retrofit.executeWith

class Repository(private val apiModel: ApiModel, private val networking: Networking) {

    fun getModelTest() = executeWith(networking) {
        apiModel.getTestModelTest()
    }
}
