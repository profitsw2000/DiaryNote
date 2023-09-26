package diarynote.core.utils

class SearchQueryBuilder(
    private val searchString: String,
    private val userId: Int
) {
    private val queryBegin = "SELECT DISTINCT " +
            "id,category,title,text,tags,image,date,edited,editDate,category_id,user_id FROM ("
    private val queryBase = "SELECT *, ? " +
            "AS PRIORITY " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id LIKE ? " +
            "AND NoteEntity.tags LIKE '%?%' " +
            "UNION " +
            "SELECT *, 1 " +
            "AS PRIORITY " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id LIKE ? " +
            "AND NoteEntity.title LIKE '%?%' " +
            "UNION " +
            "SELECT *, 2 " +
            "AS PRIORITY " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id LIKE ? " +
            "AND NoteEntity.text LIKE '%?%' "
    private val queryEnd = "ORDER BY PRIORITY)"

    private var queryString: String = ""
    private var args: MutableList<Any> = mutableListOf()

    private fun getQueryWordsList(searchQuery: String): List<String> {
        val wordsList: MutableList<String> = mutableListOf()
        if (searchQuery.trim().split(" ").toList().size < 2) {
            wordsList.addAll(searchQuery.trim().split(" ").toList())
        }
        return wordsList
    }

    private fun generateFullStringQueryWithArgs(searchQuery: String) {
        queryString += queryBase
        args.addAll(listOf(0, userId, searchQuery,
            1, userId, searchQuery,
            2, userId, searchQuery)
        )
    }

    private fun generateParticularWordQueryWithArgs(searchQuery: String,
                                                    wordNumber: Int) {
        queryString += queryBase
        args.addAll(listOf((wordNumber + 3), userId, searchQuery,
            (wordNumber + 6), userId, searchQuery,
            (wordNumber + 9), userId, searchQuery
            )
        )
    }

    fun getSearchQueryString(): String {
        return queryString
    }

    fun getSearchQueryArgs(): List<Any>{
        return args
    }

}