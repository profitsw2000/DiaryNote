package diarynote.data.data.web

import diarynote.data.domain.web.CategoryRepositoryRemote
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.related.UserWithCategories
import diarynote.data.room.related.UserWithCategoriesAndNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class CategoryRepositoryRemoteImpl : CategoryRepositoryRemote {
    override fun getAllUserCategories(userId: Int): Single<UserWithCategories> {
        TODO("Not yet implemented")
    }

    override fun getAllCategories(): Single<List<CategoryEntity>> {
        TODO("Not yet implemented")
    }

    override fun addCategory(categoryEntity: CategoryEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun addCategoryList(categoryEntityList: List<CategoryEntity>): Completable {
        TODO("Not yet implemented")
    }

    override fun updateCategory(categoryEntity: CategoryEntity): Completable {
        TODO("Not yet implemented")
    }
}