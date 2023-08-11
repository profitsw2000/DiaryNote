package diarynote.data.interactor

import diarynote.data.domain.local.CategoryRepositoryLocal
import diarynote.data.domain.web.CategoryRepositoryRemote
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.related.UserWithCategories
import diarynote.data.room.related.UserWithCategoriesAndNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class CategoryInteractor(
    private val categoryRepositoryLocal: CategoryRepositoryLocal,
    private val categoryRepositoryRemote: CategoryRepositoryRemote
) {

    fun getAllUserCategories(userId: Int, remote: Boolean): Single<UserWithCategories> {
        return if (remote) {
            categoryRepositoryRemote.getAllUserCategories(userId)
        } else {
            categoryRepositoryLocal.getAllUserCategories(userId)
        }
    }

    fun getUserNotesByCategory(userId: Int, categoryId: Int, remote: Boolean): Single<UserWithCategoriesAndNotes> {
        return if (remote) {
            categoryRepositoryRemote.getUserNotesByCategory(userId, categoryId)
        } else {
            categoryRepositoryLocal.getUserNotesByCategory(userId, categoryId)
        }
    }

    fun getAllCategories(remote: Boolean): Single<List<CategoryEntity>> {
        return if (remote) {
            categoryRepositoryRemote.getAllCategories()
        } else {
            categoryRepositoryLocal.getAllCategories()
        }
    }

    fun addCategory(categoryEntity: CategoryEntity, remote: Boolean): Completable {
        return if (remote) {
            categoryRepositoryRemote.addCategory(categoryEntity)
        } else {
            categoryRepositoryLocal.addCategory(categoryEntity)
        }
    }

    fun addCategoryList(categoryEntityList: List<CategoryEntity>, remote: Boolean): Completable {
        return if (remote) {
            categoryRepositoryRemote.addCategoryList(categoryEntityList)
        } else {
            categoryRepositoryLocal.addCategoryList(categoryEntityList)
        }
    }
}