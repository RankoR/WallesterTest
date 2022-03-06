package page.smirnov.wallestertest.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import page.smirnov.wallester.core_network.data.model.Beer
import page.smirnov.wallester.core_ui.util.setOnSingleClickListener
import page.smirnov.wallestertest.R
import page.smirnov.wallestertest.databinding.ViewItemBeerListBinding

class BeersListAdapter : RecyclerView.Adapter<BeersListAdapter.ViewHolder>() {

    var onItemClick: (beer: Beer) -> Unit = {}
    var onFavoriteClick: (beer: Beer) -> Unit = {}

    var beers: List<Beer> = emptyList()
        set(value) {
            if (field != value) {
                val diffCallback = BeersListDiffUtilCallback(field, value)
                val diffResult = DiffUtil.calculateDiff(diffCallback)

                field = value
                diffResult.dispatchUpdatesTo(this)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewItemBeerListBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            .let(::ViewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beers[position])
    }

    override fun getItemCount(): Int {
        return beers.size
    }

    inner class ViewHolder(
        private val binding: ViewItemBeerListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnSingleClickListener {
                (itemView.tag as Beer?)?.let { beer ->
                    onItemClick(beer)
                }
            }

            binding.favoriteIv.setOnSingleClickListener {
                (itemView.tag as Beer?)?.let { beer ->
                    onFavoriteClick(beer)
                }
            }
        }

        fun bind(beer: Beer) {
            itemView.tag = beer

            binding.nameTv.text = beer.name
            binding.abvTv.text = binding.abvTv.resources.getString(R.string.format_beer_list_abc, beer.abv)

            val favoriteResId = if (beer.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
            binding.favoriteIv.setImageResource(favoriteResId)
        }
    }
}
