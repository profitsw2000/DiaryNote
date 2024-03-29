package diarynote.core.utils

private const val SEARCH_FIELDS_NUMBER = 3

class SearchQueryBuilder(
    searchString: String,
    private val userId: Int,
    private val loadSize: Int,
    private val offset: Int,
    private val priorityList: List<Int>
) {
    private val titleSearchPriority: Int = priorityList[0]
    private val textSearchPriority: Int = priorityList[1]
    private val tagSearchPriority: Int = priorityList[2]
    private val queryBegin = "SELECT DISTINCT " +
            "id,category,title,text,tags,image,date,edited,editDate,category_id,user_id FROM ("
    private val queryBase = "SELECT *, ? " +
            "AS PRIORITY " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id LIKE ? " +
            "AND NoteEntity.tags LIKE '%' || ? || '%' " +
            "UNION " +
            "SELECT *, ? " +
            "AS PRIORITY " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id LIKE ? " +
            "AND NoteEntity.title LIKE '%' || ? || '%' " +
            "UNION " +
            "SELECT *, ? " +
            "AS PRIORITY " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id LIKE ? " +
            "AND NoteEntity.text LIKE '%' || ? || '%' "
    private val queryEnd = "ORDER BY PRIORITY)"

    private val args = mutableListOf<Any>()
    private var queryString: String = queryBegin +
            getFullStringQueryWithArgs(searchString) +
            getQueriesForParticularWordsWithArgs(searchString) +
            queryEnd + getQueryEnd(loadSize, offset)
    private val queryPair: Pair<String, List<Any>> = Pair(queryString, args)

    private fun getQueryWordsList(searchQuery: String): List<String> {
        return searchQuery.trim().split(" ").toList()
    }

    private fun getFullStringQueryWithArgs(searchQuery: String) : String {
        args.addAll(listOf(tagSearchPriority, userId, searchQuery,
            titleSearchPriority, userId, searchQuery,
            textSearchPriority, userId, searchQuery)
        )
        return queryBase
    }

    private fun getQueriesForParticularWordsWithArgs(searchQuery: String) : String {
        val particularWordsList = getQueryWordsList(searchQuery)
        var resultString = ""

        if (particularWordsList.size > 1) {
            particularWordsList.forEachIndexed { index, s ->
                resultString += "UNION "
                resultString += getParticularWordQueryWithArgs(s, index, particularWordsList.size)
            }
        }

        return resultString
    }

    private fun getParticularWordQueryWithArgs(searchQuery: String,
                                                    wordNumber: Int,
                                               numberOfWords: Int) : String {
        args.addAll(listOf((wordNumber + numberOfWords*tagSearchPriority + SEARCH_FIELDS_NUMBER), userId, searchQuery,
            (wordNumber + numberOfWords*titleSearchPriority + SEARCH_FIELDS_NUMBER), userId, searchQuery,
            (wordNumber + numberOfWords*textSearchPriority + SEARCH_FIELDS_NUMBER), userId, searchQuery
            )
        )
        return queryBase
    }

    private fun getQueryEnd(loadSize: Int, offset: Int): String {
        args.addAll(listOf(loadSize, offset))
        return "LIMIT :loadSize OFFSET :offset"
    }

    fun getSearchQueryPair(): Pair<String, List<Any>> {
        return queryPair
    }

}