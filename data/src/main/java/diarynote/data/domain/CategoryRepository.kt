package diarynote.data.domain

import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.related.UserWithCategories
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface CategoryRepository {

    fun getAllUserCategories(userId: Int): Single<UserWithCategories>

    fun addCategory(categoryEntity: CategoryEntity): Completable
}