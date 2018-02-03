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

package org.dalol.chipsinputlayout.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dalol.chipsinputlayout.model.ChipInput;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 03/02/2018.
 */

public abstract class BaseChipsInputAdapter<C extends ChipInput> {

    public static final int DEFAULT_CHIP_REMOVER_VIEW_ID = -1;

    public View onCreateChipView(LayoutInflater layoutInflater, ViewGroup parent) {
        return createChipView(layoutInflater, parent);
    }

    public void onBindChipView(View chipView, ChipInput chipInput) {
        bindChipView(chipView, (C) chipInput);
    }
    public View getChipInputRemoverView(View chipView) {
        return chipView;
    }

    protected abstract View createChipView(LayoutInflater layoutInflater, ViewGroup parent);

    public abstract void bindChipView(View chipView, C chipInput);
}
