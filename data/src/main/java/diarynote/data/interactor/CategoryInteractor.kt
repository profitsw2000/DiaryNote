package diarynote.data.interactor

import diarynote.data.domain.local.CategoryRepositoryLocal
import diarynote.data.domain.local.UserRepositoryLocal
import diarynote.data.domain.web.CategoryRepositoryRemote
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.related.UserWithCategories
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class CategoryInteractor(
    private val categoryRepositoryLocal: CategoryRepositoryLocal,
    private val categoryRepositoryRemote: CategoryRepositoryRemote
) {

    fun getAllUserCategories(userId: Int): Single<UserWithCategories> {
        return categoryRepositoryLocal.getAllUserCategories(userId)
    }

    fun addCategory(categoryEntity: CategoryEntity): Completable {
        return categoryRepositoryLocal.addCategory(categoryEntity)
    }
}