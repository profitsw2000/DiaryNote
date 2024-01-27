package diarynote.data.model.type

sealed class DataSourceType{
    object UserNotesDataSource : DataSourceType()
    object CategoryNotesDataSource : DataSourceType()
    object SearchNotesDataSource : DataSourceType()
    object DateNotesDataSource : DataSourceType()
}
