package diarynote.data.room.entity

import androidx.room.*

@Entity(foreignKeys = [ForeignKey(
    entity = UserEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("user_id"),
    onDelete = ForeignKey.CASCADE)],
    indices = [Index("user_id")])
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val color: Int,
    val categoryName: String,
    val categoryImage: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int
)
