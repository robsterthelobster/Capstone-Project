/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.robsterthelobster.ucibustracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robsterthelobster.ucibustracker.data.PredictionAdapter;
import com.robsterthelobster.ucibustracker.data.db.BusDbHelper;

public class ArrivalsFragment extends Fragment {

    private static final String TAG = ArrivalsFragment.class.getSimpleName();
    private static final int DATASET_COUNT = 60;

    protected RecyclerView mRecyclerView;
    protected PredictionAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BusDbHelper mOpenHelper = new BusDbHelper(getContext());
        Log.d(TAG, mOpenHelper.getDatabaseName());
        Log.d(TAG, "test1 " + mOpenHelper.testDb());
        Log.d(TAG, "test2 " + mOpenHelper.testDb2());
        Log.d(TAG, "test3 " + mOpenHelper.testDb3());

        Cursor cursor = mOpenHelper.testDb4();

        try {
            while (cursor.moveToNext()) {
                Log.d(TAG, "id: " + cursor.getInt(0));
                Log.d(TAG, "name: " + cursor.getString(1));
                Log.d(TAG, "color: " + cursor.getString(2));
            }
        } finally {
            cursor.close();
        }

        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_arrivals, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PredictionAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }
}
