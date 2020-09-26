package com.dimi.moviedatabase.business.interactors.tv_show

import kotlinx.coroutines.FlowPreview

@FlowPreview
class TvShowUseCases(
    val tvShowTrailersUseCase: TvShowTrailersUseCase,
    val searchTvShowsUseCase: SearchTvShowsUseCase,
    val tvShowDetailsUseCase: TvShowDetailsUseCase,
    val tvShowImagesUseCase: TvShowImagesUseCase,
    val tvShowRecommendationsUseCase: TvShowRecommendationsUseCase,
    val tvSimilarTvShowsUseCase: SimilarTvShowsUseCase,
    val tvShowEpisodesUseCase: TvShowEpisodesUseCase
)