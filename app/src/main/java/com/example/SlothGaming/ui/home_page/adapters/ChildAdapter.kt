import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.databinding.ItemGameCardBinding

class ChildAdapter(private var items: List<GameItem>,
                   private var gameClick:(GameItem) -> Unit) :
    RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {

    inner class ChildViewHolder(val binding: ItemGameCardBinding) :
        RecyclerView.ViewHolder(binding.root){
        private lateinit var gameItem : GameItem

        fun bind (item : GameItem){
            this.gameItem = item
            binding.itemTitle.text = item.title
            Glide.with(binding.root)
                .load(item.imageUrl).override(300,450)
                .into(binding.gamePoster)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding = ItemGameCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bind(items[position])
        val item = items[position]
        Log.d("crash","from child")
        holder.binding.gamePoster.setOnClickListener {
            gameClick(item)
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newGames: List<GameItem>) {
        val diffCallBack = ChildDiffCallback(items,newGames)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
         // // For small list its ok, but better use diffUtil
        items = newGames
        diffResult.dispatchUpdatesTo(this)
    }
}


class ChildDiffCallback(
    private val oldList: List<GameItem>,
    private val newList: List<GameItem>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    //check if it's the same section
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    // check if the data inside the section has been changed
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]== newList[newItemPosition]
    }
}