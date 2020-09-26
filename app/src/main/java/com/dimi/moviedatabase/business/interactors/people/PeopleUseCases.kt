package com.dimi.moviedatabase.business.interactors.people

import kotlinx.coroutines.FlowPreview

@FlowPreview
class PeopleUseCases(
    val peopleDetailsUseCase: PeopleDetailsUseCase,
    val searchPeopleUseCase: SearchPeopleUseCase,
    val personMovieCastUseCase: PersonMovieCastUseCase,
    val personTvShowCastUseCase: PersonTvShowCastUseCase,
    val personImagesUseCase: PersonImagesUseCase
)