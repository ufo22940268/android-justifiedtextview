/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.biubiubiu.justifytext;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextActivity extends Activity {

    public static final String KEY_FILE_NAME = "KEY_FILE_NAME";
    private TextView mJustifiedText;
    private TextView mText;
    private MyScrollView mScroller;
    private MyScrollView mJustifiedScroller;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mText = (TextView)findViewById(R.id.text);
        mJustifiedText = (TextView)findViewById(R.id.justified_text);
        String text = "";

        BufferedReader br = null;
        try {
            String fileName = getIntent().getStringExtra(KEY_FILE_NAME);
            br = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            text = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mJustifiedText.setText(text);
        mText.setText(text);

        mScroller = (MyScrollView)findViewById(R.id.scroller);
        mJustifiedScroller = (MyScrollView)findViewById(R.id.justified_scroller);
        mScroller.setAlternativeScrollView(mJustifiedScroller);
        mJustifiedScroller.setAlternativeScrollView(mScroller);
    }
}
