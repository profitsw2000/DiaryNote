package diarynote.data.mappers

import diarynote.data.model.CategoryModel
import diarynote.data.room.entity.CategoryEntity

class CategoryMapper {
    fun map(categoryModel: CategoryModel): CategoryEntity {
        return CategoryEntity(
            id = categoryModel.id,
            color = categoryModel.color,
            categoryName = categoryModel.categoryName,
            categoryImage = categoryModel.categoryImage,
            userId = categoryModel.userId
        )
    }

    fun map(categoryEntity: CategoryEntity): CategoryModel {
        return CategoryModel(
            id = categoryEntity.id,
            color = categoryEntity.color,
            categoryName = categoryEntity.categoryName,
            categoryImage = categoryEntity.categoryImage,
            userId = categoryEntity.userId
        )
    }

    fun map(categoryEntityList: List<CategoryEntity>): List<CategoryModel> {
        return categoryEntityList.map { map(it) }
    }

    fun map(categoryModelList: List<CategoryModel>): List<CategoryEntity> {
        return categoryModelList.map { map(it) }
    }
}