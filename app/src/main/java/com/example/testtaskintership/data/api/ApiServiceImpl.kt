package com.example.testtaskintership.data.api

import com.example.testtaskintership.data.api.ApiRoutes.CAMERAS
import com.example.testtaskintership.data.api.ApiRoutes.DOORS
import com.example.testtaskintership.domain.model.Data
import com.example.testtaskintership.domain.model.MainDataModel
import com.example.testtaskintership.domain.model.SecondaryDataModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {
    override suspend fun getCameras(): MainDataModel {
        return try {
            client.get { url(CAMERAS) }.body()
        }
        catch (ex: RedirectResponseException) {
            println("Error: ${ex.response.status.description}")
            MainDataModel(Data(emptyList(), emptyList()), false)
        }
        catch (ex: ClientRequestException) {
            println("Error: ${ex.response.status.description}")
            MainDataModel(Data(emptyList(), emptyList()), false)
        }
        catch (ex: ServerResponseException) {
            println("Error: ${ex.response.status.description}")
            MainDataModel(Data(emptyList(), emptyList()), false)
        }
        catch (ex: Exception) {
            println("Error: ${ex.message}")
            MainDataModel(Data(emptyList(), emptyList()), false)
        }
    }

    override suspend fun getDoors(): SecondaryDataModel {
        return try {
            client.get { url(DOORS) }.body()
        }
        catch (ex: RedirectResponseException) {
            println("Error: ${ex.response.status.description}")
            SecondaryDataModel(emptyList(), false)
        }
        catch (ex: ClientRequestException) {
            println("Error: ${ex.response.status.description}")
            SecondaryDataModel(emptyList(), false)
        }
        catch (ex: ServerResponseException) {
            println("Error: ${ex.response.status.description}")
            SecondaryDataModel(emptyList(), false)
        }
        catch (ex: Exception) {
            println("Error: ${ex.message}")
            SecondaryDataModel(emptyList(), false)
        }
    }
}
