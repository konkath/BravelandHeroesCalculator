package com.soltysdev.bravelandheroescalculator.army.rules.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.rules.Rule;

import androidx.annotation.Nullable;

final class RuleRow extends LinearLayout {
    private Button mRemoveRuleButton;
    private TextView mRuleText;

    private Rule mRule;
    private Rule.Type mType;
    private Rule.Operator mOperator;

    public RuleRow(Context context, Rule rule, Rule.Type type, Rule.Operator operator) {
        this(context, null);

        mRule = rule;
        mType = type;
        mOperator = operator;

        mRuleText.setText(mRule.getFullDescription(getContext(), type, operator));
    }

    public RuleRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.view_rule, this);

        mRemoveRuleButton = layout.findViewById(R.id.removeRuleButton);
        mRuleText = layout.findViewById(R.id.ruleText);
    }

    interface OnRuleRemovedCallback {
        void removeRule(View v);
    }

    void setOnRuleRemovedCallback(OnRuleRemovedCallback onRuleRemovedCallback) {
        mRemoveRuleButton.setOnClickListener((v) -> {
            mRule.removeSubRule(mType, mOperator);
            onRuleRemovedCallback.removeRule(v);
        });
    }
}
