package diarynote.data.mappers

import diarynote.data.model.CategoryModel
import diarynote.data.room.entity.CategoryEntity

class CategoryMapper {
    private fun map(categoryModel: CategoryModel): CategoryEntity {
        return CategoryEntity(
            id = categoryModel.id,
            color = categoryModel.color,
            categoryName = categoryModel.categoryName,
            categoryImage = categoryModel.categoryImage,
            userId = categoryModel.userId
        )
    }

    private fun map(categoryEntity: CategoryEntity): CategoryModel {
        return CategoryModel(
            id = categoryEntity.id,
            color = categoryEntity.color,
            categoryName = categoryEntity.categoryName,
            categoryImage = categoryEntity.categoryImage,
            userId = categoryEntity.userId
        )
    }

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("mapEntityList")
    fun map(categoryEntityList: List<CategoryEntity>): List<CategoryModel> {
        return categoryEntityList.map { map(it) }
    }

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("mapModelList")
    fun map(categoryModelList: List<CategoryModel>): List<CategoryEntity> {
        return categoryModelList.map { map(it) }
    }
}