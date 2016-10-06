package com.example.vincent.complexrecyclerview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vincent.complexrecyclerview.adapter.MultiGalleryAdapter;
import com.example.vincent.complexrecyclerview.adapter.MultiTypeViewAdapter;
import com.example.vincent.complexrecyclerview.classes.GridItemDecoration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mMultiBtn, mGalleryBtn;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMultiBtn = (Button)findViewById(R.id.button_multi);
        mGalleryBtn = (Button)findViewById(R.id.button_gallery);

        mMultiBtn.setOnClickListener(this);
        mGalleryBtn.setOnClickListener(this);

        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_multi:
                mFragmentManager.beginTransaction().replace(R.id.main_content, new MultiTypeFragment(), MultiTypeFragment.class.getSimpleName()).commit();
                break;
            case R.id.button_gallery:
                mFragmentManager.beginTransaction().replace(R.id.main_content, new MultiGalleryFragment(), MultiGalleryFragment.class.getSimpleName()).commit();
                break;
        }
    }

    public static class MultiTypeFragment extends Fragment{

        private RecyclerView mRecyclerView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_multitype, container, false);
            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(new MultiTypeViewAdapter(getActivity()));

            return rootView;
        }
    }

    public static class MultiGalleryFragment extends Fragment{

        private RecyclerView mRecyclerView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_multigallery, container, false);
            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview);
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(new MultiGalleryAdapter(getActivity()));
            mRecyclerView.addItemDecoration(new GridItemDecoration(getActivity(), mRecyclerView.getAdapter()));

            return rootView;
        }
    }
}
