package com.example.mvi_noteapp.business.domain.util

interface EntityMapper<Entity, DomainModel> {

    fun mapFromEntity(entity: Entity):DomainModel

    fun mapToEntity(domainModel: DomainModel):Entity


}