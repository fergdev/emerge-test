package com.example.testemerge.api

class MoviesApiManager(private val service: LoginService) {

    private var token: String? = null
    private var session: String? = null
    private var account: AccountDto? = null
    private var cachedPopular: List<MovieDto>? = null

    suspend fun login(
        username: String,
        password: String
    ) {
        val tokenResult = service.getToken()
        this.token = tokenResult.requestToken
        val login = service.login(RequestLoginDto(username, password, this.token!!))
        val sessionDto = service.createSession(RequestSessionDto(login.requestToken))
        this.session = sessionDto.sessionId
        this.account = service.account(sessionDto.sessionId)
    }

    suspend fun popular(): List<MovieDto> {
        val cachedPopular = this.cachedPopular
        if (cachedPopular != null) {
            return cachedPopular
        }
        val results = service.popular().results
        this.cachedPopular = results
        return results
    }

    suspend fun favourite(): List<MovieDto> {
        return service.favourite(session!!).results
    }

    suspend fun addFavorite(toAdd: MovieDto) {
        service.manageFavorite(
            accountId = account!!.id.toString(),
            sessionId = session!!,
            favoriteDto = FavoriteDto(mediaId = toAdd.id, favorite = true)
        )
    }

    suspend fun removeFavorite(movieDto: MovieDto) {
        service.manageFavorite(
            accountId = account!!.id.toString(),
            sessionId = session!!,
            favoriteDto = FavoriteDto(mediaId = movieDto.id, favorite = false)
        )
    }

    fun logout() {
        token = null
        session = null
        account = null
        cachedPopular = null
    }
}