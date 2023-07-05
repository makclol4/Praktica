package space.games.quiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import space.games.quiz.R;
import space.games.quiz.model.Level;

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onClick(int position, Level level);
    }

    private List<Level> data;
    private OnItemClickListener onItemClickListener;

    public LevelsAdapter() {

    }

    public LevelsAdapter(List<Level> data) {
        this.data = data;
    }

    public void setData(List<Level> data) {
        this.data = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameLevelText.setText(data.get(position).getName());
        if (!data.get(position).isOpen())
            holder.item.setEnabled(false);

    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        else
            return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView nameLevelText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView;
            nameLevelText = itemView.findViewById(R.id.level_name);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onClick(getLayoutPosition(), data.get(getLayoutPosition()));
                }
            });
        }
    }
}


