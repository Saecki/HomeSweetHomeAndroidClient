package bedbrains.homesweethomeandroidclient.rest

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object Controller {

    fun buildClient(baseUrl: String): APIService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            JacksonConverterFactory.create(
                jacksonObjectMapper().disable(
                    MapperFeature.AUTO_DETECT_FIELDS,
                    MapperFeature.AUTO_DETECT_GETTERS,
                    MapperFeature.AUTO_DETECT_IS_GETTERS,
                    MapperFeature.AUTO_DETECT_SETTERS,
                    MapperFeature.AUTO_DETECT_CREATORS
                ).disable(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES,
                    DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES
                )
            )
        )
        .build()
        .create(APIService::class.java)
}
