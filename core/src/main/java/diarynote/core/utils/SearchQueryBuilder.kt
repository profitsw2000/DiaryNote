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
            "SELECT *, ? " +
            "AS PRIORITY " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id LIKE ? " +
            "AND NoteEntity.title LIKE '%?%' " +
            "UNION " +
            "SELECT *, ? " +
            "AS PRIORITY " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id LIKE ? " +
            "AND NoteEntity.text LIKE '%?%' "
    private val queryEnd = "ORDER BY PRIORITY)"

    private val args = mutableListOf<Any>()
    private var queryString: String = queryBegin +
            getFullStringQueryWithArgs(searchString) +
            getQueriesForParticularWordsWithArgs(searchString) +
            queryEnd
    private val queryPair: Pair<String, List<Any>> = Pair(queryString, args)

    private fun getQueryWordsList(searchQuery: String): List<String> {
        return searchQuery.trim().split(" ").toList()
    }

    private fun getFullStringQueryWithArgs(searchQuery: String) : String {
        args.addAll(listOf(0, userId, searchQuery,
            1, userId, searchQuery,
            2, userId, searchQuery)
        )
        return queryBase
    }

    private fun getQueriesForParticularWordsWithArgs(searchQuery: String) : String {
        val particularWordsList = getQueryWordsList(searchQuery)
        var resultString = ""

        if (particularWordsList.size > 1) {
            particularWordsList.forEachIndexed { index, s ->
                resultString += "UNION "
                resultString += getParticularWordQueryWithArgs(s, index)
            }
        }

        return resultString
    }

    private fun getParticularWordQueryWithArgs(searchQuery: String,
                                                    wordNumber: Int) : String {
        args.addAll(listOf((wordNumber + 3), userId, searchQuery,
            (wordNumber + 6), userId, searchQuery,
            (wordNumber + 9), userId, searchQuery
            )
        )
        return queryBase
    }

    fun getSearchQueryPair(): Pair<String, List<Any>> {
        return queryPair
    }

}