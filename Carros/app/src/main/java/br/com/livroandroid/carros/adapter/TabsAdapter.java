package br.com.livroandroid.carros.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.fragments.CarrosFragment;
import br.com.livroandroid.carros.fragments.FavoritosFragment;

public class TabsAdapter extends FragmentPagerAdapter {
    private Context context;

    public TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        // Quantidade de páginas
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Este título será mostrado nas Tabs
        if (position == 0) {
            return context.getString(R.string.classicos);
        } else if (position == 1) {
            return context.getString(R.string.esportivos);
        } else if (position == 2) {
            return context.getString(R.string.luxo);
        }
        return context.getString(R.string.favoritos);
    }

    @Override
    public Fragment getItem(int position) {
        // Cria o fragment para cada página
        Fragment f = null;
        if (position == 0) {
            f = CarrosFragment.newInstance(R.string.classicos);
        } else if (position == 1) {
            f = CarrosFragment.newInstance(R.string.esportivos);
        } else if (position == 2) {
            f = CarrosFragment.newInstance(R.string.luxo);
        } else {
            // Favoritos
            f = new FavoritosFragment();
        }
        return f;
    }

}
