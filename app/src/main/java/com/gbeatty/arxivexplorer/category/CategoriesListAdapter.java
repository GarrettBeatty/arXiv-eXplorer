package com.gbeatty.arxivexplorer.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gbeatty.arxivexplorer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.CategoryViewHolder> {

    private final CategoriesPresenter presenter;

    CategoriesListAdapter(CategoriesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        CategoryViewHolder viewHolder = new CategoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        presenter.onBindCategoryRowViewAtPosition(position, holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return presenter.getCategoriesRowsCount();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements CategoryRowView {

        @BindView(R.id.category_name)
        TextView categoryName;

        CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> presenter.categoryClicked(getLayoutPosition()));
        }

        public void setCategoryName(String name) {
            categoryName.setText(name);
        }

    }
}
