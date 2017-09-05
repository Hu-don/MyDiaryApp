package olivia.dimsun.com.mydiaryapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


/**
 * Created by hudon on 10/08/2017.
 */


class MainAdapter extends RecyclerView.Adapter <MainAdapter.RecyclerViewHolders> {

    TextView dateTV, titleTV, idTV;
    private List<Entry> itemList;
    private Context context;
    String sid;
    RecyclerViewHolders holder;
    SQLiteOpenHelper mHelper;
    RelativeLayout relativeLayout;

    public MainAdapter(Context context, List<Entry> itemList) {

        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list, null);
        return new RecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, int position) {
        holder.titleTV.setText(itemList.get(position).getEntryText());
        holder.dateTV.setText(itemList.get(position).getEntryDate());
        int id = (int) itemList.get(position).getitemId();
        sid = String.valueOf(id);
        holder.idTV.setText(sid);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String intentTitle = "Envoi de MyDiaryApp";
                String strText = String.valueOf(holder.titleTV.getText());

                Intent onLongClick = new Intent(Intent.ACTION_SEND);
                onLongClick.setType("plain/text");
                onLongClick.putExtra(Intent.EXTRA_SUBJECT, intentTitle);
                onLongClick.putExtra(Intent.EXTRA_TEXT, strText);
                holder.itemView.getContext().startActivity(onLongClick);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateTV, titleTV, idTV;

        private RecyclerViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            dateTV = (TextView) itemView.findViewById(R.id.entry_date);
            titleTV = (TextView) itemView.findViewById(R.id.entry_title);
            idTV = (TextView) itemView.findViewById(R.id.idTV);
        }

        @Override
        public void onClick(View view) {

            String text = String.valueOf(titleTV.getText());
            String id = String.valueOf(idTV.getText());

            Intent i = new Intent(itemView.getContext(), AddEntry.class);
            i.putExtra(Constantes.ENTRY, text);
            i.putExtra(Constantes.ID, id);
            itemView.getContext().startActivity(i);
            ((MainActivity) context).finish();
        }
    }
}
