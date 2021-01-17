package com.soltysdev.bravelandheroescalculator.army.rules.layouts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.rules.Rule;
import com.soltysdev.bravelandheroescalculator.army.rules.RuleManager;

import androidx.annotation.NonNull;

public class RuleDialog extends Dialog {
    private static final String TAG = RuleDialog.class.getSimpleName();

    private Spinner mRuleSpinner;
    private Spinner mTypeSpinner;
    private Spinner mOperatorSpinner;
    private EditText mRuleValue;
    private Button mAddRuleButton;

    private Rule mSelectedRule;
    private Rule.Type mSelectedType;
    private Rule.Operator mSelectedOperator;

    private RuleManager mRuleManager;
    private OnRuleAddedCallback mRuleAddedCallback;

    RuleDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rule);

        mRuleManager = RuleManager.getInstance();
        mRuleSpinner = findViewById(R.id.ruleSpinner);
        mTypeSpinner = findViewById(R.id.typeSpinner);
        mOperatorSpinner = findViewById(R.id.operationSpinner);

        mRuleValue = findViewById(R.id.ruleValue);
        mRuleValue.addTextChangedListener(new RuleValueTextWatcher());
        mRuleValue.getBackground().setColorFilter(getContext().getResources().getColor(
                R.color.colorSpinnerBackground, null), PorterDuff.Mode.SRC_ATOP);

        mAddRuleButton = findViewById(R.id.addRuleButton);
        mAddRuleButton.setOnClickListener(this::onAddRuleClick);

        initRuleSpinner();
    }

    abstract class AdapterItemSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Log.e(TAG, "Operation not permitted - nothing cannot be selected!");
            dismiss();
        }
    }

    private void initRuleSpinner() {
        mRuleSpinner.setAdapter(new SpinnerAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, mRuleManager.getEditableRules()));
        mRuleSpinner.setOnItemSelectedListener(new AdapterItemSelected() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onRuleSpinnerSelected(i);
            }
        });
        onRuleSpinnerSelected(mRuleSpinner.getSelectedItemPosition());
    }

    private void onRuleSpinnerSelected(int position) {
        mSelectedRule = (Rule) mRuleSpinner.getItemAtPosition(position);
        initTypeSpinner();
    }

    private void initTypeSpinner() {
        mTypeSpinner.setAdapter(new SpinnerAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, mSelectedRule.getAvailableTypes()));
        mTypeSpinner.setOnItemSelectedListener(new AdapterItemSelected() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onTypeSpinnerSelected(i);
            }
        });
        onTypeSpinnerSelected(mTypeSpinner.getSelectedItemPosition());
    }

    private void onTypeSpinnerSelected(int position) {
        mSelectedType = (Rule.Type) mTypeSpinner.getItemAtPosition(position);
        initOperatorSpinner();
    }

    private void initOperatorSpinner() {
        mOperatorSpinner.setAdapter(new SpinnerAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                mSelectedRule.getAvailableOperators(mSelectedType)));
        mOperatorSpinner.setOnItemSelectedListener(new AdapterItemSelected() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onOperationSpinnerSelected(i);
            }
        });
        onOperationSpinnerSelected(mOperatorSpinner.getSelectedItemPosition());
    }

    private void onOperationSpinnerSelected(int position) {
        mSelectedOperator = (Rule.Operator) mOperatorSpinner.getItemAtPosition(position);
        initRuleEditText();
    }

    private void initRuleEditText() {
        final int minValue = mSelectedRule.getMinEditableValue(mSelectedType);
        final int maxValue = mSelectedRule.getMaxEditableValue(mSelectedType);

        mRuleValue.setText("");
        mRuleValue.setHint(minValue + " - " + maxValue);
        mRuleValue.setFilters(new InputFilter[]{(source, start, end, dest, dStart, dEnd) -> {
            try {
                String replacement = source.subSequence(start, end).toString();
                String newVal = dest.toString().substring(0, dStart) +
                        replacement + dest.toString().substring(dEnd);

                int input = Integer.parseInt(newVal);
                if (input <= maxValue && input >= minValue) {
                    return null;
                }

                // Special case where minValue has two digits and we are starting to type
                // TODO: Handle more than two digit numbers
                if (minValue > 9 && input > 0 && input <= (minValue / 10)) {
                    mAddRuleButton.setEnabled(false);  // button shouldn't be enabled yet
                    return null;
                }
            } catch (NumberFormatException ignored) {
            }
            return "";
        }});
    }

    private int getRuleValue() {
        String text = mRuleValue.getText().toString();
        return Integer.parseInt(text);
    }

    class RuleValueTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            final String text = editable.toString();
            if (text.isEmpty()) {
                mAddRuleButton.setEnabled(false);
            } else if (!mAddRuleButton.isEnabled()) {
                final int value = Integer.parseInt(text);
                final int minValue = mSelectedRule.getMinEditableValue(mSelectedType);

                if (value >= minValue) {
                    mAddRuleButton.setEnabled(true);
                }
            }
        }
    }

    public interface OnRuleAddedCallback {
        void ruleAdded(Rule rule, Rule.Type type, Rule.Operator operator);
    }

    void setRuleAddedCallback(OnRuleAddedCallback callback) {
        mRuleAddedCallback = callback;
    }

    private void onAddRuleClick(View v) {
        mRuleManager.getArmyLimitRule().addSubRule(
                mSelectedType, mSelectedOperator, getRuleValue());
        mRuleAddedCallback.ruleAdded(mSelectedRule, mSelectedType, mSelectedOperator);
        dismiss();
    }
}
