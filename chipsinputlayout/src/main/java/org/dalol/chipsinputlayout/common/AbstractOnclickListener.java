/*
 * Copyright (c) 2018 Filippo Engidashet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dalol.chipsinputlayout.common;

import android.view.View;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 03/02/2018.
 */

public abstract class AbstractOnclickListener<V extends View> implements View.OnClickListener {

    private final View parentView;

    protected AbstractOnclickListener(View parentView) {
        this.parentView = parentView;
    }

    @Override
    public void onClick(View childView) {
        onClick(parentView, childView);
    }

    protected abstract void onClick(View parentView, View childView);
}
