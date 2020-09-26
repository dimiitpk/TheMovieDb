package com.dimi.moviedatabase.business.interactors.movie

import kotlinx.coroutines.FlowPreview

@FlowPreview
class MovieUseCases(
    val movieTrailersUseCase: MovieTrailersUseCase,
    val searchMoviesUseCase: SearchMoviesUseCase,
    val movieDetailsUseCase: MovieDetailsUseCase,
    val movieImagesUseCase: MovieImagesUseCase,
    val similarMoviesUseCase: SimilarMoviesUseCase,
    val movieRecommendationsUseCase: MovieRecommendationsUseCase
)