package diarynote.data.data.local

import diarynote.data.domain.local.CategoryRepositoryLocal
import diarynote.data.room.database.AppDatabase
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.related.UserWithCategories
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class CategoryRepositoryLocalImpl(
    private val database: AppDatabase
) : CategoryRepositoryLocal {
    override fun getAllUserCategories(userId: Int): Single<UserWithCategories> {
        return database.categoryDao.getUserWithCategories(userId)
    }

    override fun getAllCategories(): Single<List<CategoryEntity>> {
        return database.categoryDao.all()
    }

    override fun addCategory(categoryEntity: CategoryEntity): Completable {
        return database.categoryDao.insert(categoryEntity)
    }

    override fun addCategoryList(categoryEntityList: List<CategoryEntity>): Completable {
        return database.categoryDao.insert(categoryEntityList)
    }
}