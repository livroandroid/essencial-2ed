package br.com.livroandroid.carros.domain;

import android.content.Context;

import br.com.livroandroid.carros.R;

public enum TipoCarro {
    classicos(R.string.classicos),
    esportivos(R.string.esportivos),
    luxo(R.string.luxo),
    favoritos(R.string.favoritos);

    private final int s;

    public final int getResource() {
        return s;
    }

    public final String getText(Context context) {
        return context.getString(s);
    }

    TipoCarro(int s) {
        this.s = s;
    }
}