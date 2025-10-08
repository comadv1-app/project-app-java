package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class MajorAdapter extends RecyclerView.Adapter<MajorAdapter.VH> implements Filterable {

    public interface OnStartQuiz {
        void onStart(String trackId);
    }

    private final List<MajorTrack> original = new ArrayList<>();
    private final List<MajorTrack> filtered = new ArrayList<>();
    private String keyword = "";
    private String categoryFilter = "ทั้งหมด";
    private final OnStartQuiz callback;

    public MajorAdapter(List<MajorTrack> data, OnStartQuiz cb) {
        original.addAll(data);
        filtered.addAll(data);
        callback = cb;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        MajorTrack m = filtered.get(pos);
        h.tvName.setText(m.name);
        h.tvBrief.setText(m.brief);
        h.tvSkills.setText("ทักษะเด่น: " + m.skills);
        h.tvTopics.setText("หัวข้อสำคัญ: " + m.topics);
        h.tvJobs.setText("งานที่เจอ: " + m.jobs);

        h.ivToggle.setOnClickListener(v -> {
            h.boxDetail.setVisibility(h.boxDetail.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE);
            h.ivToggle.setRotation(h.boxDetail.getVisibility()==View.VISIBLE ? 180f : 0f);
        });
        h.itemView.setOnClickListener(v -> h.ivToggle.performClick());
        h.btnStart.setOnClickListener(v -> {
            if (callback != null) callback.onStart(m.id);
        });
    }

    @Override public int getItemCount() { return filtered.size(); }

    @Override public Filter getFilter() {
        return new Filter() {
            @Override protected FilterResults performFiltering(CharSequence cs) {
                keyword = cs==null ? "" : cs.toString().trim().toLowerCase();
                List<MajorTrack> out = new ArrayList<>();
                for (MajorTrack m : original) {
                    boolean matchCat = categoryFilter.equals("ทั้งหมด")
                            || m.category.equalsIgnoreCase(categoryFilter);
                    boolean matchText = keyword.isEmpty()
                            || m.name.toLowerCase().contains(keyword)
                            || m.brief.toLowerCase().contains(keyword)
                            || m.topics.toLowerCase().contains(keyword)
                            || m.jobs.toLowerCase().contains(keyword);
                    if (matchCat && matchText) out.add(m);
                }
                FilterResults fr = new FilterResults();
                fr.values = out; fr.count = out.size();
                return fr;
            }
            @Override protected void publishResults(CharSequence cs, FilterResults fr) {
                filtered.clear();
                if (fr.values != null) filtered.addAll((List<MajorTrack>) fr.values);
                notifyDataSetChanged();
            }
        };
    }

    public void setCategoryFilter(String cat) {
        this.categoryFilter = cat;
        getFilter().filter(keyword);
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvBrief, tvSkills, tvTopics, tvJobs;
        View boxDetail;
        ImageView ivToggle;
        MaterialButton btnStart;
        VH(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvBrief = v.findViewById(R.id.tvBrief);
            tvSkills = v.findViewById(R.id.tvSkills);
            tvTopics = v.findViewById(R.id.tvTopics);
            tvJobs = v.findViewById(R.id.tvJobs);
            boxDetail = v.findViewById(R.id.boxDetail);
            ivToggle = v.findViewById(R.id.ivToggle);
            btnStart = v.findViewById(R.id.btnStart);
        }
    }
}
