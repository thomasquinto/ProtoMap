package com.life360.android.protomap.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.life360.android.protomap.R;
import com.life360.android.protomap.model.Member;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thomas on 2/21/17.
 */

public class MemberAdapter extends RecyclerView.Adapter {

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        View view;

        @BindView(R.id.member_icon)
        ImageView memberIcon;
        @BindView(R.id.member_name)
        TextView memberName;
        @BindView(R.id.member_address)
        TextView memberAddress;

        public MemberViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    private Context context;
    private List<Member> members;
    public MemberAdapter(Context context, List<Member> members) {
        this.context = context;
        this.members = members;
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.member_card, viewGroup, false);
        MemberViewHolder viewHolder = new MemberViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Member member = members.get(i);

        MemberViewHolder memberViewHolder = (MemberViewHolder) viewHolder;
        memberViewHolder.memberName.setText(member.getName());
        memberViewHolder.memberAddress.setText(member.getAddress());
        memberViewHolder.memberIcon.setImageBitmap(member.getIcon((context)));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
