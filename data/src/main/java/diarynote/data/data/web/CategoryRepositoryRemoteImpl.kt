package diarynote.data.data.web

import diarynote.data.domain.web.CategoryRepositoryRemote
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.related.UserWithCategories
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class CategoryRepositoryRemoteImpl : CategoryRepositoryRemote {
    override fun getAllUserCategories(userId: Int): Single<UserWithCategories> {
        TODO("Not yet implemented")
    }

    override fun addCategory(categoryEntity: CategoryEntity): Completable {
        TODO("Not yet implemented")
    }
}