package page.smirnov.wallestertest.presentation.util

import androidx.recyclerview.widget.DiffUtil
import page.smirnov.wallester.core_persistence.data.model.Beer

internal class BeersListDiffUtilCallback(
    private val oldList: List<Beer>,
    private val newList: List<Beer>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition] // Data classes override «equals» by default, so we can use «==»
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }
}
