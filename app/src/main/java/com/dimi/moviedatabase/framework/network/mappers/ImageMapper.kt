package com.dimi.moviedatabase.framework.network.mappers

import com.dimi.moviedatabase.business.domain.model.Image
import com.dimi.moviedatabase.business.domain.util.EntityMapper
import com.dimi.moviedatabase.framework.network.responses.common.ImageResponse
import javax.inject.Inject

class ImageMapper
@Inject
constructor(
) : EntityMapper<ImageResponse, Image> {

    fun mapFromEntityList(entities: List<ImageResponse>): List<Image> {
        return entities.map { mapFromEntity(it) }
    }

    override fun mapFromEntity(entity: ImageResponse): Image {
        return Image(
            aspectRatio = entity.aspectRatio,
            height = entity.height,
            filePath = entity.filePath,
            isPoster = (entity.aspectRatio < 1)
        )
    }

    override fun mapToEntity(domainModel: Image): ImageResponse {
        return ImageResponse(
            aspectRatio = domainModel.aspectRatio,
            height = domainModel.height,
            filePath = domainModel.filePath
        )
    }
}