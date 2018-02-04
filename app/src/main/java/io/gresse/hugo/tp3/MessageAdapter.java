package io.gresse.hugo.tp3;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.List;

/**
 * Display chat messages
 * <p>
 * Created by Hugo Gresse on 26/11/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Listener mListener;
    private List<Message> mData;
    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECIEVED = -1;
    private User user;

    public MessageAdapter(Listener listener, List<Message> data, User user) {
        mListener = listener;
        mData = data;
        this.user = user;
    }

    public void setData(List<Message> data) {
        mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_messages_sent, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_messages, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView mUserImageView;
        TextView mUserTextView;
        TextView mContentTextView;
        RelativeTimeTextView v;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            // itemView.setOnLongClickListener(new View.OnLongClickListener());

            mUserImageView = itemView.findViewById(R.id.userImageView);
            mUserTextView = itemView.findViewById(R.id.userTextView);
            mContentTextView = itemView.findViewById(R.id.contentTextView);
            v = itemView.findViewById(R.id.timestamp); //Or just use Butterknife!
            cardView = itemView.findViewById(R.id.card_view);

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.onItemLongClick(getAdapterPosition(), mData.get(getAdapterPosition()));
                    return true;
                }
            });

        }


        void setData(Message message) {
            Long tsLong = (System.currentTimeMillis() - 1000 - message.timestamp);
            mUserTextView.setText(message.userName + ": ");
            mContentTextView.setText(message.content);
            v.setReferenceTime(message.timestamp);

            if (!TextUtils.isEmpty(message.userEmail)) {
                Glide
                        .with(mUserImageView.getContext())
                        .load(Constant.GRAVATAR_PREFIX + Utils.md5(message.userEmail))
                        .apply(RequestOptions.circleCropTransform())
                        .into(mUserImageView);
            } else {
                mUserImageView.setImageResource(R.color.colorAccent);
            }
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClick(getAdapterPosition(), mData.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            mListener.onItemLongClick(getAdapterPosition(), mData.get(getAdapterPosition()));
            return true;
        }
      /*  @Override
        public boolean onLongClick(View view) {
            return true;
        }*/

    }

    public interface Listener {
        void onItemClick(int position, Message message);

        void onItemLongClick(int position, Message message);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mData.get(position);
        if (message.userEmail.equals(user.email)) {
            return TYPE_SENT;
        } else {
            return TYPE_RECIEVED;
        }
    }
}
