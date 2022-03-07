package page.smirnov.wallestertest.presentation.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_ui.util.setOnSingleClickListener
import page.smirnov.wallestertest.databinding.ViewItemFavoriteBinding
import page.smirnov.wallestertest.presentation.util.BeersListDiffUtilCallback

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    var onItemClick: (beer: Beer) -> Unit = {}

    var beers: List<Beer> = emptyList()
        set(value) {
            if (field != value) {
                val diffCallback = BeersListDiffUtilCallback(field, value)
                val diffResult = DiffUtil.calculateDiff(diffCallback)

                field = value
                diffResult.dispatchUpdatesTo(this)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.ViewHolder {
        return ViewItemFavoriteBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            .let(::ViewHolder)
    }

    override fun onBindViewHolder(holder: FavoritesAdapter.ViewHolder, position: Int) {
        holder.bind(beers[position])
    }

    override fun getItemCount(): Int {
        return beers.size
    }

    inner class ViewHolder(
        private val binding: ViewItemFavoriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnSingleClickListener {
                (itemView.tag as Beer?)?.let { beer ->
                    onItemClick(beer)
                }
            }
        }

        fun bind(beer: Beer) {
            itemView.tag = beer

            binding.nameTv.text = beer.name
        }
    }
}
