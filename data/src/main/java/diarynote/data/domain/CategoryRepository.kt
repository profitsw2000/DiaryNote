package diarynote.data.domain

import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.related.UserWithCategories
import diarynote.data.room.related.UserWithCategoriesAndNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface CategoryRepository {

    fun getAllUserCategories(userId: Int): Single<UserWithCategories>

    fun getAllCategories(): Single<List<CategoryEntity>>

    fun addCategory(categoryEntity: CategoryEntity): Completable

    fun addCategoryList(categoryEntityList: List<CategoryEntity>): Completable
}