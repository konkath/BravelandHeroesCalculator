package com.soltysdev.bravelandheroescalculator.unit;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.soltysdev.bravelandheroescalculator.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class UnitViewHolder extends RecyclerView.ViewHolder {
    private static final String ASSETS_PATH = "file:///android_asset/";
    private ConstraintLayout mConstraintLayout;

    private Unit mUnit;
    private EditText mEditQuantity;

    public UnitViewHolder(ConstraintLayout constraintLayout, boolean quantityEditable) {
        super(constraintLayout);
        mConstraintLayout = constraintLayout;

        mEditQuantity = mConstraintLayout.findViewById(R.id.unit_quantity);
        if (quantityEditable) {
            mEditQuantity.addTextChangedListener(new QuantityTextWatcher());
        } else {
            mEditQuantity.setEnabled(false);
        }
    }

    public void populateUnitHolder(Unit unit) {
        mUnit = unit;

        populateUnitImage(mUnit.getImg());
        populateUnitType(mUnit.getType());
        populateUnitKingdom(mUnit.getClan());

        populateTextView(R.id.unit_name, mUnit.getTranslated_name());
        populateTextView(R.id.unit_star_value, String.valueOf(mUnit.getStars()));
        populateTextView(R.id.unit_leadership_value, String.valueOf(mUnit.getLeadership()));
        populateTextView(R.id.unit_defence_value, String.valueOf(mUnit.getDefence()));
        populateTextView(R.id.unit_magic_defence_value, String.valueOf(mUnit.getMagic_defence()));
        populateTextView(R.id.unit_movement_value, String.valueOf(mUnit.getMovement()));
        populateTextView(R.id.unit_initiative_value, String.valueOf(mUnit.getInitiative()));

        populateTextView(R.id.unit_min_damage_value, String.valueOf(mUnit.getNormalizedMinAttack()));
        populateTextView(R.id.unit_max_damage_value, String.valueOf(mUnit.getNormalizedMaxAttack()));
        populateTextView(R.id.unit_health_value, String.valueOf(mUnit.getNormalizedHealth()));
        mEditQuantity.setText(String.valueOf(mUnit.getQuantity()));
    }

    private void recalculateAttributesToQuantity(int unitQuantity) {
        mUnit.updateQuantity(unitQuantity);
        populateTextView(R.id.unit_min_damage_value, String.valueOf(mUnit.getNormalizedMinAttack()));
        populateTextView(R.id.unit_max_damage_value, String.valueOf(mUnit.getNormalizedMaxAttack()));
        populateTextView(R.id.unit_health_value, String.valueOf(mUnit.getNormalizedHealth()));
    }

    private void populateTextView(int id, String value) {
        TextView textView = mConstraintLayout.findViewById(id);
        textView.setText(value);
    }

    private void populateUnitImage(String path) {
        ImageView imageView = mConstraintLayout.findViewById(R.id.unit_image);
        Glide.with(mConstraintLayout).load(ASSETS_PATH + path).into(imageView);
    }

    private void populateUnitType(UnitType type) {
        ImageView imageView = mConstraintLayout.findViewById(R.id.unit_type);
        int drawableId = -1;
        switch (type) {
            case Warrior:
                drawableId = R.drawable.warrior;
                break;
            case Defender:
                drawableId = R.drawable.defender;
                break;
            case Marksman:
                drawableId = R.drawable.marksman;
                break;
            case Mage:
                drawableId = R.drawable.mage;
                break;
        }
        Drawable drawable = mConstraintLayout.getResources().getDrawable(drawableId, null);
        imageView.setImageDrawable(drawable);
    }

    private void populateUnitKingdom(UnitClan unitClan) {
        ImageView imageView = mConstraintLayout.findViewById(R.id.unit_kingdom);
        int drawableId = -1;
        switch (unitClan) {
            case Bandit:
                drawableId = R.drawable.bandit_leader;
                break;
            case Commander:
                drawableId = R.drawable.commander_of_the_kingdom;
                break;
            case Demon:
                drawableId = R.drawable.demon_lord;
                break;
            case Sorcerer:
                drawableId = R.drawable.fire_sorcerer;
                break;
            case Orc:
                drawableId = R.drawable.orc_chieftain;
                break;
            case Dead:
                drawableId = R.drawable.lord_of_the_dead;
                break;
            case Elves:
                drawableId = R.drawable.prince_of_the_elves;
                break;
            case Pirate:
                drawableId = R.drawable.pirate_captain;
                break;
            case IceMage:
                drawableId = R.drawable.ice_mage;
                break;
            case Neutral:
                drawableId = R.drawable.neutral;
                break;
        }
        Drawable drawable = mConstraintLayout.getResources().getDrawable(drawableId, null);
        imageView.setImageDrawable(drawable);
    }

    class QuantityTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            final int unitQuantity = charSequence.length() < 1 ? 0 :
                    Integer.parseInt(charSequence.toString());
            recalculateAttributesToQuantity(unitQuantity);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
