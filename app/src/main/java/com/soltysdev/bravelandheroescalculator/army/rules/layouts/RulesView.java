package com.soltysdev.bravelandheroescalculator.army.rules.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.rules.Rule;
import com.soltysdev.bravelandheroescalculator.army.rules.RuleManager;

import androidx.annotation.Nullable;

public final class RulesView extends LinearLayout {
    private LinearLayout mRulesLayout;
    private Button mAddRuleButton;

    private RuleManager mRuleManager;
    private OnRuleChangedCallback mOnRuleChangedCallback;

    public RulesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.view_rules, this);

        mRuleManager = RuleManager.getInstance();
        mRulesLayout = layout.findViewById(R.id.rulesLayout);
        mAddRuleButton = layout.findViewById(R.id.addRuleButton);
        mAddRuleButton.setOnClickListener(this::onAddRuleClick);
    }

    public void onAddRuleClick(View v) {
        RuleDialog dialog = new RuleDialog(getContext());
        dialog.setRuleAddedCallback((rule, type, operator) -> {
            addRuleRow(rule, type, operator);
            mOnRuleChangedCallback.onRuleChanged();

            if (mRuleManager.getEditableRules().isEmpty()) {
                mAddRuleButton.setEnabled(false);
            }
        });
        dialog.show();
    }

    private void addRuleRow(Rule rule, Rule.Type type, Rule.Operator operator) {
        RuleRow ruleRow = new RuleRow(getContext(), rule, type, operator);
        ruleRow.setOnRuleRemovedCallback((v) -> {
            mRulesLayout.removeView(ruleRow);
            mOnRuleChangedCallback.onRuleChanged();
            mAddRuleButton.setEnabled(true);
        });
        mRulesLayout.addView(ruleRow);
    }

    public interface OnRuleChangedCallback {
        void onRuleChanged();
    }

    public void setOnRuleChangedCallback(OnRuleChangedCallback onRuleChangedCallback) {
        mOnRuleChangedCallback = onRuleChangedCallback;
    }
}
