package com.bandfitproject.board;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.bandfitproject.BandFitDataBase;
import com.bandfitproject.R;
import com.bandfitproject.data.BoardData;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.Unbinder;

public class BoardActivity extends Fragment {
    @BindView(R.id.board_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.board_sp_search) Spinner board_sp_search;

    BoardAdapter rAdapter = null;

    private Unbinder unbinder;

    @OnItemSelected(R.id.board_sp_search)
    void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(this.getClass().getName(), "스피너");
        String sp_type = parent.getItemAtPosition(position).toString();
        if(!sp_type.equals("종목을 선택하세요")) {
            rAdapter.filter(sp_type);
        }
    }

    public BoardActivity() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        //Log.i(this.getClass().getName(), "여기는 게시판 onResume 입니다.");
    }

    @Override
    public void onPause() {
       // Log.i(this.getClass().getName(), "여기는 게시판 onPause 입니다.");
        super.onPause();
    }

    @Override
    public void onStop() {
      //*  Log.i(this.getClass().getName(), "여기는 게시판 onStop 입니다.");
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BandFitDataBase.getInstance().exit();
        Log.i(getClass().getName(), FirebaseDatabase.getInstance().toString() + " onDestroy");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreateView");
        View view = inflater.inflate(R.layout.board_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);

        //items = BandFitDataBase.getInstance().getBoard_items();
        System.out.println("===================================================================");
        System.out.println();
        for(BoardData b : BandFitDataBase.getInstance().board_Items) {
            System.out.print(b.topic.toString());
            System.out.print(", ");
        }
        System.out.println();
        System.out.println("===================================================================");

        rAdapter = new BoardAdapter(getContext(), BandFitDataBase.getInstance().board_Items, R.layout.board_fragment);
        recyclerView.setAdapter(rAdapter);
        return view;
    }
}
