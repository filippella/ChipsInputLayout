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
package org.dalol.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.dalol.chipsinputlayout.ChipsInputLayout
import org.dalol.chipsinputlayout.adapter.BaseChipsHintAdapter
import org.dalol.chipsinputlayout.adapter.BaseChipsInputAdapter
import org.dalol.chipsinputlayout.holders.ChipsInputViewHolder

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 03/02/2018.
 */

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chipsInputLayout = findViewById<ChipsInputLayout>(R.id.chips_input_layout)

        val chipInputs = mutableListOf<MyChipInput>()
        chipInputs.add(MyChipInput("Apple"))
        chipInputs.add(MyChipInput("Banana"))
        chipInputs.add(MyChipInput("Cat"))
        chipInputs.add(MyChipInput("Dog"))
        chipInputs.add(MyChipInput("Elephant"))
        chipInputs.add(MyChipInput("Fox"))
        chipInputs.add(MyChipInput("Giraffe"))
        chipInputs.add(MyChipInput("Horse"))
        chipInputs.add(MyChipInput("Imapla"))
        chipInputs.add(MyChipInput("Jaguar"))
        chipInputs.add(MyChipInput("kiwi"))
        chipInputs.add(MyChipInput("lion"))
        chipInputs.add(MyChipInput("mouse"))
        chipInputs.add(MyChipInput("Orange"))

        val pinnedChipsInput = mutableListOf<MyChipInput>()
        pinnedChipsInput.add(MyChipInput("Apple"))
        pinnedChipsInput.add(MyChipInput("Banana"))
        pinnedChipsInput.add(MyChipInput("Orange"))

        chipsInputLayout.setChipsAdapter(object : BaseChipsInputAdapter<MyChipInput>() {

            override fun getChipInputRemoverView(chipView: View?): View {
                return chipView!!.findViewById(R.id.button_close_tag)
            }

            override fun bindChipView(chipView: View?, chipInput: MyChipInput?) {
                val tagTextView = chipView?.findViewById(R.id.text_tag) as TextView
                tagTextView.text = chipInput.toString()
            }

            override fun createChipView(layoutInflater: LayoutInflater?, parent: ViewGroup?): View {
                return layoutInflater!!.inflate(R.layout.item_chip_input_layout, parent, false)
            }
        })

        chipsInputLayout.setFilterableAdapter(object : BaseChipsHintAdapter<MyChipInput, ChipsInputViewHolder>(this, chipInputs) {

            override fun onCreateViewHolder(inflater: LayoutInflater?, parent: ViewGroup?, position: Int): ChipsInputViewHolder {
                return ChipsInputViewHolder(inflater!!.inflate(R.layout.item_chip_hint_layout, parent, false))
            }

            override fun bindViewHolder(viewHolder: ChipsInputViewHolder?, position: Int) {
                val tagHintTextView = viewHolder!!.itemView?.findViewById(R.id.text_hint) as TextView
                tagHintTextView.text = getItem(position).toString()
            }
        })

        chipsInputLayout.setPinnedChipsInput(pinnedChipsInput)
    }
}
