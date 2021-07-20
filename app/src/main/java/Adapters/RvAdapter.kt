package Adapters

import Models.User
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilhomjon.h552royhatdanotish.databinding.ItemRvBinding
import kotlinx.android.synthetic.main.item_rv.view.*

class RvAdapter(val list: List<User>, var rvItemClick: RvItemClick)
    : RecyclerView.Adapter<RvAdapter.Vh>(){

inner class Vh(var itemView:ItemRvBinding):RecyclerView.ViewHolder(itemView.root){
    fun onBind(user: User, position:Int){
        itemView.txt_name_rv_item.text = user.name
        itemView.txt_number_rv_item.text = user.telNumber

        if (user.imageUri != null){

            val bitmap =BitmapFactory.decodeByteArray(user.imageUri, 0, user.imageUri?.size!!)

            itemView.image_rv_item.setImageBitmap(bitmap)
        }

        itemView.setOnClickListener {
            rvItemClick.itemClick(user, position)
        }
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

}
interface RvItemClick{
    fun itemClick(user: User, position: Int)
}