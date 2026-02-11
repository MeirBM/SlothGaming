import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.databinding.ItemGameCardBinding

class ChildAdapter(private var items: List<GameItem>) :
    RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {

    inner class ChildViewHolder(val binding: ItemGameCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding = ItemGameCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            itemTitle.text = item.title
            // Load image using Glide
            Glide.with(root.context)
                .load(item.imageUrl).override(300,450)
                .into(gamePoster)
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newGames: List<GameItem>) {
        val diffCallBack = ChildDiffCallback(items,newGames)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
         // בתוך רשימה אופקית קטנה זה בסדר, אבל תמיד עדיף DiffUtil בעתיד
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

    // האם זה אותו Section (לפי הכותרת למשל)
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    // האם תוכן הרשימה בתוך ה-Section השתנה
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]== newList[newItemPosition]
    }
}