package com.soltysdev.bravelandheroescalculator.unit;

import android.content.Context;
import android.icu.text.Collator;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class Unit implements Parcelable {
    private int type;
    private int clan;
    private int min_attack;
    private int max_attack;
    private int health;

    @Getter
    private String name;
    @Getter
    private String translated_name;
    @Getter
    private String img;
    @Getter
    private int stars;
    @Getter
    private int leadership;
    @Getter
    private int defence;
    @Getter
    private int magic_defence;
    @Getter
    private int movement;
    @Getter
    private int initiative;
    @Getter
    @Setter
    private int quantity;

    private static final float EMPTY_SORTING_MODIFIER = 0.001f;
    private OnDatabaseUpdateCallback onDatabaseUpdateCallback;

    public void updateLanguage(Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(name, "string", packageName);
        translated_name = context.getResources().getString(resId);
    }

    public interface OnDatabaseUpdateCallback {
        void UpdateQuantity(int quantity);
    }

    public void setOnDatabaseUpdateCallback(OnDatabaseUpdateCallback callback) {
        onDatabaseUpdateCallback = callback;
    }

    public UnitType getType() {
        return UnitType.get(type);
    }

    public UnitClan getClan() {
        return UnitClan.get(clan);
    }

    public int getMinAttack() {
        return min_attack * quantity;
    }

    public int getMaxAttack() {
        return max_attack * quantity;
    }

    public int getHealth() {
        return health * quantity;
    }

    void updateQuantity(int quantity) {
        this.quantity = quantity;

        // Callback will be null for Units in ArmyActivity
        if (onDatabaseUpdateCallback != null) {
            onDatabaseUpdateCallback.UpdateQuantity(quantity);
        }
    }

    int getNormalizedMinAttack() {
        return quantity == 0 ? min_attack : min_attack * quantity;
    }

    int getNormalizedMaxAttack() {
        return quantity == 0 ? max_attack : max_attack * quantity;
    }

    int getNormalizedHealth() {
        return quantity == 0 ? health : health * quantity;
    }

    private float getSortingAverageAttack() {
        float avgAttack = (float) (getMaxAttack() + getMinAttack()) / 2;
        return quantity > 0 ? avgAttack : avgAttack * EMPTY_SORTING_MODIFIER;
    }

    private float getSortingHealth() {
        return quantity > 0 ? health * quantity : health * EMPTY_SORTING_MODIFIER;
    }

    public static Comparator<Unit> ByAttack = (lhs, rhs) -> Float.compare(rhs.getSortingAverageAttack(), lhs.getSortingAverageAttack());
    public static Comparator<Unit> ByHealth = (lhs, rhs) -> Float.compare(rhs.getSortingHealth(), lhs.getSortingHealth());
    public static Comparator<Unit> ByName = (lhs, rhs) -> Collator.getInstance().compare(lhs.translated_name, rhs.translated_name);
    public static Comparator<Unit> ByClan = (lhs, rhs) -> Integer.compare(lhs.clan, rhs.clan);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return Objects.equals(name, unit.name);
    }

    // Parcelable implementation
    private Unit(Parcel in) {
        type = in.readInt();
        clan = in.readInt();
        min_attack = in.readInt();
        max_attack = in.readInt();
        health = in.readInt();
        name = in.readString();
        translated_name = in.readString();
        img = in.readString();
        stars = in.readInt();
        leadership = in.readInt();
        defence = in.readInt();
        magic_defence = in.readInt();
        movement = in.readInt();
        initiative = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<Unit> CREATOR = new Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeInt(clan);
        parcel.writeInt(min_attack);
        parcel.writeInt(max_attack);
        parcel.writeInt(health);
        parcel.writeString(name);
        parcel.writeString(translated_name);
        parcel.writeString(img);
        parcel.writeInt(stars);
        parcel.writeInt(leadership);
        parcel.writeInt(defence);
        parcel.writeInt(magic_defence);
        parcel.writeInt(movement);
        parcel.writeInt(initiative);
        parcel.writeInt(quantity);
    }
}
