/*
 * Part of Phonk http://www.phonk.io
 * A prototyping platform for Android devices
 *
 * Copyright (C) 2013 - 2017 Victor Diaz Barrales @victordiaz (Protocoder)
 * Copyright (C) 2017 - Victor Diaz Barrales @victordiaz (Phonk)
 *
 * Phonk is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Phonk is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Phonk. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.phonk.gui.filemanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.phonk.events.Events;
import io.phonk.runner.base.utils.MLog;
import io.phonk.server.model.ProtoFile;

public class FileManagerAdapter extends RecyclerView.Adapter<FileManagerAdapter.ViewHolder> {

    private static final String TAG = FileManagerAdapter.class.getSimpleName();
    private final FileManagerFragment mFragment;
    private ArrayList<ProtoFile> mDataSet;

    public void setData(ArrayList<ProtoFile> currentFileList) {
        mDataSet = currentFileList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final FileManagerListItem mView;

        public ViewHolder(FileManagerListItem v) {
            super(v);
            mView = v;
        }
    }

    public FileManagerAdapter(FileManagerFragment fragment, ArrayList<ProtoFile> currentFileList) {
        mFragment = fragment;
        mDataSet = currentFileList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FileManagerListItem fileManagerListItem = new FileManagerListItem(mFragment.getContext());
        ViewHolder vh = new ViewHolder(fileManagerListItem);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ProtoFile protoFile = mDataSet.get(position);
        holder.mView.setProtoFile(protoFile);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLog.d(TAG, "" + protoFile.name + " " + protoFile.getFullPath());

                // if its a folder move to that level
                if (protoFile.type.equals("folder")) {
                    mFragment.setCurrentFolder(protoFile.getFullPath());
                } else {
                    EventBus.getDefault().post(new Events.EditorEvent(Events.EDITOR_FILE_INTENT_LOAD, protoFile));
                }
            }

        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mFragment.showMenuForItem(holder.mView, position);

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
