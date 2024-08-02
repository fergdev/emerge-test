package com.example.testemerge.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val apiToken =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiODEzNGY3Y2M4Yjk5Yjk3YWZhZjdmZmQzZjg0OTI2ZiIsIm5iZiI6MTcyMjU3MDUwNC43Njc2MTEsInN1YiI6IjY1MGJlNzI1M2Q3NDU0MDBhYzljNDhhMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.gSGz2oBVSTvy-Z8th8Usp5a1HkbQa3NR_j6o_mG_PeE"

interface LoginService {
    @GET("authentication/token/new")
    @Headers("Authorization: Bearer $apiToken")
    suspend fun getToken(): TokenDto

    @POST("authentication/token/validate_with_login")
    @Headers("Authorization: Bearer $apiToken")
    suspend fun login(@Body requestLoginDto: RequestLoginDto): LoginResponseDto

    @POST("authentication/session/new")
    @Headers("Authorization: Bearer $apiToken")
    suspend fun createSession(@Body requestSessionDto: RequestSessionDto): SessionDto

    @GET("account")
    @Headers("Authorization: Bearer $apiToken")
    suspend fun account(@Query("session_id") sessionId: String): AccountDto

    @GET("movie/popular?language=en-US&page=1")
    @Headers("Authorization: Bearer $apiToken")
    suspend fun popular(): MoviesDto

    @GET("account/{account_id}/favorite/movies")
    @Headers("Authorization: Bearer $apiToken")
    suspend fun favourite(@Path("account_id") accountId: String): MoviesDto

    @POST("account/{account_id}/favorite")
    @Headers("Authorization: Bearer $apiToken")
    suspend fun manageFavorite(
        @Path("account_id") accountId: String,
        @Query("session_id") sessionId: String,
        @Body favoriteDto: FavoriteDto
    )
}

@JsonClass(generateAdapter = true)
data class FavoriteDto(
    @Json(name = "media_type")
    val mediaType: String = "movie",
    @Json(name = "media_id")
    val mediaId: Int,
    @Json(name = "favorite")
    val favorite: Boolean
)

@JsonClass(generateAdapter = true)
data class LoginResponseDto(
    @Json(name = "request_token")
    val requestToken: String
)

@JsonClass(generateAdapter = true)
data class SessionDto(
    @Json(name = "session_id")
    val sessionId: String
)

@JsonClass(generateAdapter = true)
data class AccountRequestDto(
    @Json(name = "request_token")
    val requestToken: String
)

@JsonClass(generateAdapter = true)
data class TokenDto(
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "request_token")
    val requestToken: String
)

@JsonClass(generateAdapter = true)
data class RequestLoginDto(
    @Json(name = "username")
    val username: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "request_token")
    val requestToken: String
)

@JsonClass(generateAdapter = true)
data class RequestSessionDto(
    @Json(name = "request_token")
    val requestToken: String
)


@JsonClass(generateAdapter = true)
data class MoviesDto(
    @Json(name = "results")
    val results: List<MovieDto>
)

@JsonClass(generateAdapter = true)
data class MovieDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "original_title")
    val title: String,
    @Json(name = "overview")
    val details: String
)

@JsonClass(generateAdapter = true)
data class AccountDto(
    @Json(name = "id")
    val id: Int
)

