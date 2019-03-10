package com.unacademyclone.utility;
import android.content.Context;
import android.graphics.Typeface;

public class TypefaceUtility {
    final Typeface opensans_thin,opensans_light,opensans_regular,opensans_semibold,opensans_bold;
    final Typeface caviar_dreams_thin,caviar_dreams_light,caviar_dreams_regular,caviar_dreams_semibold,caviar_dreams_bold;
    final Typeface wellmet_bold,helvetica_regular;

    public TypefaceUtility(Context context){

        opensans_thin = Typeface.createFromAsset(context.getAssets(), "opensans_thin.ttf");
        opensans_light = Typeface.createFromAsset(context.getAssets(), "opensans_light.ttf");
        opensans_regular = Typeface.createFromAsset(context.getAssets(), "opensans_regular.ttf");
        opensans_semibold = Typeface.createFromAsset(context.getAssets(), "opensans_semibold.ttf");
        opensans_bold = Typeface.createFromAsset(context.getAssets(), "opensans_bold.ttf");

        caviar_dreams_thin = Typeface.createFromAsset(context.getAssets(), "caviar_dreams_regular.ttf");
        caviar_dreams_light = Typeface.createFromAsset(context.getAssets(), "caviar_dreams_regular.ttf");
        caviar_dreams_regular = Typeface.createFromAsset(context.getAssets(), "caviar_dreams_regular.ttf");
        caviar_dreams_semibold = Typeface.createFromAsset(context.getAssets(), "caviar_dreams_bold.ttf");
        caviar_dreams_bold = Typeface.createFromAsset(context.getAssets(), "caviar_dreams_bold.ttf");

        wellmet_bold = Typeface.createFromAsset(context.getAssets(), "wellmet_black.ttf");
        helvetica_regular = Typeface.createFromAsset(context.getAssets(), "helvetica_regular.ttf");
    }

    public Typeface getTypefaceThin(){
        return opensans_thin;
    }

    public Typeface getTypefaceLight(){
        return opensans_light;
    }

    public Typeface getTypefaceRegular(){
        return opensans_regular;
    }

    public Typeface getTypefaceSemiBold(){
        return opensans_semibold;
    }

    public Typeface getTypefaceBold(){
        return opensans_bold;
    }

    public Typeface getTypefaceQuote(){
        return opensans_light;
    }

    public Typeface getTypefaceHelvetiva(){
        return helvetica_regular;
    }
}

