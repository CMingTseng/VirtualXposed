/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.android.launcher3.util;

import android.os.Process;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewTreeObserver.OnDrawListener;

import com.android.launcher3.Launcher;
import com.android.launcher3.LauncherModel;

import java.util.ArrayList;
import java.util.concurrent.Executor;

/**
 * An executor which runs all the tasks after the first onDraw is called on the target view.
 */
public class ViewOnDrawExecutor implements Executor, OnDrawListener, Runnable,
        OnAttachStateChangeListener {

    private final ArrayList<Runnable> mTasks = new ArrayList<>();
    private final Executor mExecutor;

    private Launcher mLauncher;
    private View mAttachedView;
    private boolean mCompleted;
    private boolean mIsExecuting;

    private boolean mLoadAnimationCompleted;
    private boolean mFirstDrawCompleted;

    public ViewOnDrawExecutor(Executor executor) {
        mExecutor = executor;
    }

    public void attachTo(Launcher launcher) {
        mLauncher = launcher;
        mAttachedView = launcher.getWorkspace();
        mAttachedView.addOnAttachStateChangeListener(this);

        attachObserver();
    }

    private void attachObserver() {
        if (!mCompleted) {
            mAttachedView.getViewTreeObserver().addOnDrawListener(this);
        }
    }

    @Override
    public void execute(Runnable command) {
        mTasks.add(command);
        LauncherModel.setWorkerPriority(Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        attachObserver();
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
    }

    @Override
    public void onDraw() {
        mFirstDrawCompleted = true;
        mAttachedView.post(this);
    }

    /**
     * Returns whether the executor is still queuing tasks and hasn't yet executed them.
     */
    public boolean canQueue() {
        return !mIsExecuting && !mCompleted;
    }

    public void onLoadAnimationCompleted() {
        mLoadAnimationCompleted = true;
        if (mAttachedView != null) {
            mAttachedView.post(this);
        }
    }

    @Override
    public void run() {
        // Post the pending tasks after both onDraw and onLoadAnimationCompleted have been called.
        if (mLoadAnimationCompleted && mFirstDrawCompleted && !mCompleted) {
            mIsExecuting = true;
            for (final Runnable r : mTasks) {
                mExecutor.execute(r);
            }
            markCompleted();
        }
    }

    public void markCompleted() {
        mTasks.clear();
        mCompleted = true;
        mIsExecuting = false;
        if (mAttachedView != null) {
            mAttachedView.getViewTreeObserver().removeOnDrawListener(this);
            mAttachedView.removeOnAttachStateChangeListener(this);
        }
        if (mLauncher != null) {
            mLauncher.clearPendingExecutor(this);
        }
        LauncherModel.setWorkerPriority(Process.THREAD_PRIORITY_DEFAULT);
    }
}
