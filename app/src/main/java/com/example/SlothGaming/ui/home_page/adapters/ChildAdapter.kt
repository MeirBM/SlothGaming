import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.databinding.ItemGameCardBinding

class ChildAdapter(private val items: List<GameItem>) :
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
                .load(item.imageUrl)
                .into(gamePoster)
        }
    }

    override fun getItemCount() = items.size
}