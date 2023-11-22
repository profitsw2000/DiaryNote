package diarynote.template.model

import diarynote.data.model.HelpItemModel

sealed class HelpState {

    data class Success(val helpItemModelList: List<HelpItemModel>) : HelpState()
    data class Error(val message: String) : HelpState()
    object Loading : HelpState()
}
