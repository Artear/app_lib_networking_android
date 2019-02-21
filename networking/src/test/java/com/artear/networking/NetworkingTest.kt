package com.artear.networking

import androidx.test.core.app.ApplicationProvider
import com.artear.networking.model.AndroidNetworking
import com.artear.networking.model.ApiModel
import com.artear.networking.model.Model
import com.artear.networking.model.Repository
import com.artear.tools.exception.BadResponseException
import com.artear.tools.exception.NoInternetConnectionException
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber


@RunWith(RobolectricTestRunner::class)
class NetworkingTest {

    companion object {
        private const val TEST_MESSAGE = "Test to validate this networking tools"
        private const val MODEL_JSON = "{\"param\":\"$TEST_MESSAGE\"}"
        private const val ERROR_RESPONSE = "{ \"error\":500, \"message\":\"Error test response\""
    }

    private lateinit var androidNetworking: AndroidNetworking
    private lateinit var repositoryImpl: Repository
    private lateinit var model: Model

    private val request = Request.Builder().url("http://test.url").build()
    private val mediaType = MediaType.parse("application/json")

    @Mock
    lateinit var api: ApiModel

    @Mock
    lateinit var call: Call<Model>


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        Timber.plant(mock(Timber.Tree::class.java))

        val responseBodyOk = ResponseBody.create(mediaType, MODEL_JSON)
        model = Gson().fromJson(responseBodyOk.string(), Model::class.java)

        androidNetworking = spy(AndroidNetworking(ApplicationProvider.getApplicationContext()))
        repositoryImpl = Repository(api, androidNetworking)

        `when`(api.getTestModelTest()).thenReturn(call)
        `when`(call.request()).thenReturn(request)
    }

    @Test
    fun recipesResponseOk() {
        `when`(call.execute()).thenReturn(Response.success(model))
        `when`(androidNetworking.isNetworkConnected()).thenReturn(true)
        val model = repositoryImpl.getModelTest()
        Assert.assertEquals(TEST_MESSAGE, model.param)
    }


    @Test(expected = BadResponseException::class)
    fun recipeInvalidResponse() {
        val responseBodyError = ResponseBody.create(mediaType, ERROR_RESPONSE)
        `when`(call.execute()).thenReturn(Response.error(500, responseBodyError))
        `when`(androidNetworking.isNetworkConnected()).thenReturn(true)
        repositoryImpl.getModelTest()
    }


    @Test(expected = IllegalStateException::class)
    fun recipesResponseOkNullBody() {
        `when`(call.execute()).thenReturn(Response.success(null))
        `when`(androidNetworking.isNetworkConnected()).thenReturn(true)
        repositoryImpl.getModelTest()
    }

    @Test(expected = NoInternetConnectionException::class)
    fun noInternetConnection() {
        `when`(androidNetworking.isNetworkConnected()).thenReturn(false)
        repositoryImpl.getModelTest()
    }

}