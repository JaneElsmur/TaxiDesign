package janeelsmur.taxi.Utilites;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import janeelsmur.taxi.R;
import janeelsmur.taxi.fragments.FragmentFavoriteAddress;
import janeelsmur.taxi.fragments.FragmentLocation;
import janeelsmur.taxi.fragments.FragmentMyListItem;

import java.util.ArrayList;

/** Класс с методами прогрузки объектов (пожеланий, опций, выбранных адресов) во view */
public class ObjectsViewLoader {

    private static final String TAG = "ObjectsViewLoader";

    /** Прогрузка фрагментов и адресов, и добавление их в ArrayList */
    public static void loadLocations(ArrayList<FragmentLocation> fragmentLocations, FragmentManager fragmentManager, SharedPrefManager sharedPrefManager) {
        //Сохраненное значение адреса "откуда" готовое для вывода
        String[] fromAddressStrings = sharedPrefManager.getFromLocation();
        String savedFrom = getLocationString(fromAddressStrings);

        //Сохраненное значение первого адреса "куда" готовое для вывода
        String[] firstWhereAddressStrings = sharedPrefManager.getWhereLocation(1);
        String savedFirstWhere = getLocationString(firstWhereAddressStrings);


        if (fromAddressStrings == null && firstWhereAddressStrings == null) { //Добавление двух пустых фрагментов
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_FROM, null, 0));
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, null, 1));
            fragmentManager
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(0))
                    .add(R.id.locations_container, fragmentLocations.get(1))
                    .commit();
        }
        else if (savedFrom.length() != 0 && savedFirstWhere.length() != 0) { //Прогрузка всех сохраненных данных
            Log.d(TAG, "loadLocations: Прогрузка всего");
            //Добавление первого "от куда"
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_FROM, savedFrom, 0));
            fragmentManager
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(0))
                    .commit();

            //Добавление первого "куда"
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, savedFirstWhere, 1));
            fragmentManager
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(1))
                    .commit();

            //Добавление остальных "куда", если они есть
            int id = 2;
            while (sharedPrefManager.getWhereLocation(id) != null) {
                String[] whereAddressStrings = sharedPrefManager.getWhereLocation(id);
                String savedWhere = getLocationString(whereAddressStrings);

                fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, savedWhere, id));
                fragmentManager
                        .beginTransaction()
                        .add(R.id.locations_container, fragmentLocations.get(id))
                        .commit();
                id++;
            }

        } else if (fromAddressStrings != null && firstWhereAddressStrings == null) { //Прогрузка только "откуда" и пустого "куда", если сохранен только "куда"
            Log.d(TAG, "loadLocations: Прогрузка только \"откуда\" и пустого \"куда\", если сохранен только \"куда");
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_FROM, savedFrom, 0));
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, null, 1));
            fragmentManager
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(0))
                    .add(R.id.locations_container, fragmentLocations.get(1))
                    .commit();

        } else if (firstWhereAddressStrings != null) { //Прогрузка только "куда" и пустого "откуда", если он не сохранен
            //Добавление первого "куда" и пустого "откуда"
            Log.d(TAG, "loadLocations: Прогрузка только \"куда\" и пустого \"откуда\", если он не сохранен");
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_FROM, null, 0));
            fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, savedFirstWhere, 1));
            fragmentManager
                    .beginTransaction()
                    .add(R.id.locations_container, fragmentLocations.get(0))
                    .add(R.id.locations_container, fragmentLocations.get(1))
                    .commit();

            //Добавление остальных "куда", если они есть
            int id = 2;
            while (sharedPrefManager.getWhereLocation(id) != null) {
                String[] whereAddressSet = sharedPrefManager.getWhereLocation(id);
                String savedWhere = getLocationString(whereAddressSet);

                fragmentLocations.add(FragmentLocation.newInstance(FragmentLocation.MODE_WHERE, savedWhere, id));
                fragmentManager
                        .beginTransaction()
                        .add(R.id.locations_container, fragmentLocations.get(id))
                        .commit();
                id++;
            }
        }
    }

    /** Удаление фрагментов из view и из ArrayList */
    public static void cleanLocations(ArrayList<FragmentLocation> fragmentLocations, FragmentManager fragmentManager) {
        for (FragmentLocation fragmentLocation : fragmentLocations) {
            fragmentManager
                    .beginTransaction()
                    .remove(fragmentLocation)
                    .commit();
        }
        fragmentLocations.clear();
    }

    /** Прогрузка локаци в финальное информационное окно */
    public static void loadLocationsInFinalOrderingInformation(FragmentManager fragmentManager, SharedPrefManager sharedPrefManager, Context context) {

        //Сохраненное значение адреса "откуда" готовое для вывода
        String[] fromAddressStrings = sharedPrefManager.getFromLocation();
        String savedFrom = getLocationString(fromAddressStrings);

        fragmentManager.beginTransaction().add(R.id.location_from_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.from_short) + " " + savedFrom)).commit();

        //Прокгрузка "куда"
        int id = 1;
        while (sharedPrefManager.getWhereLocation(id)!=null) {
            String savedWhere = getLocationString(sharedPrefManager.getWhereLocation(id));
            fragmentManager.beginTransaction().add(R.id.where_locations_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.where_short) + " " + savedWhere)).commit();
            id++;
        }
    }

    /** Прогрузка избранных адресов */
    public static void loadFavoritesAddresses(ArrayList<FragmentFavoriteAddress> fragmentLocations, FragmentManager fragmentManager, SharedPrefManager sharedPrefManager, int mode) {
        int goToId = sharedPrefManager.getFavoritesMaxId();
        int arrayListObjectId = 0;
        int i = 0;
        while (i < goToId) {
            String[] address = sharedPrefManager.getSavedFavoriteAddress(i);
            if (address != null) {
                fragmentLocations.add(FragmentFavoriteAddress.newInstance(address, i, mode));
                fragmentManager
                        .beginTransaction()
                        .add(R.id.favorite_addresses_container, fragmentLocations.get(arrayListObjectId))
                        .commit();
                arrayListObjectId++;
            }
            i++;
        }
    }

    /** Очистка лейаута от фрагментов */
    public static void clearFavoriteAddresses(ArrayList<FragmentFavoriteAddress> fragmentLocations, FragmentManager fragmentManager) {
        for(FragmentFavoriteAddress fragmentFavoriteAddress: fragmentLocations){
            fragmentManager
                    .beginTransaction()
                    .remove(fragmentFavoriteAddress)
                    .commit();
        }
        fragmentLocations.clear();
    }

    /** Прогрузка тарифа
     *               0 - первый
     *               1 - второй
     *               2 - третий
     *               3 - четвертый
     * @return новый тариф (может быть тем же, что и прошлый, в этом случае значение возвратиться, но прогрузки не будет)
     */
    public static void loadTariff(FragmentManager fragmentManager, SharedPrefManager sharedPrefManager, Context context) {
        int tariff = sharedPrefManager.getSavedTariff();
        switch (tariff) {
            case 0:
                fragmentManager.beginTransaction().add(R.id.tariff_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.tariff_1))).commit();
                break;
            case 1:
                fragmentManager.beginTransaction().add(R.id.tariff_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.tariff_2))).commit();
                break;
            case 2:
                fragmentManager.beginTransaction().add(R.id.tariff_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.tariff_3))).commit();
                break;
            case 3:
                fragmentManager.beginTransaction().add(R.id.tariff_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.tariff_4))).commit();
                break;
        }
    }

    /** Прогрузка пожеланий
     *               0 - Нужна сдача
     *               1 - Нужно детское кресло
     *               2 - Некурящий салон
     *               3 - Провоз животкного
     *               4 - I don't speck Russian
     *               5 - Нужен пустой багажник
     *               6 - Нужен универсал
     *               7 - Нужен чек
     *
     * @return новый массив пожеланий
     */
    public static boolean[] loadWishes(FragmentManager fragmentManager, SharedPrefManager sharedPrefManager, Context context) {
        boolean[] wishes = sharedPrefManager.getWishes(SharedPrefManager.WISHES_COUNT);
        boolean isEmpty = true;
        if (wishes[0]) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.wishes_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.I_need_change)))
                    .commit();
            isEmpty = false;
        }
        if (wishes[1]) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.wishes_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.need_baby_chair)))
                    .commit();
            isEmpty = false;
        }
        if (wishes[2]) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.wishes_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.non_smoking)))
                    .commit();
            isEmpty = false;
        }
        if (wishes[3]) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.wishes_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.pet_transportation)))
                    .commit();
            isEmpty = false;
        }
        if (wishes[4]) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.wishes_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.i_don_t_speak_russian)))
                    .commit();
            isEmpty = false;
        }
        if (wishes[5]) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.wishes_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.I_need_empty_trunk)))
                    .commit();
            isEmpty = false;
        }
        if (wishes[6]) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.wishes_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.wagon)))
                    .commit();
            isEmpty = false;
        }
        if (wishes[7]) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.wishes_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.need_check)))
                    .commit();
            isEmpty = false;
        }
        if (isEmpty) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.wishes_container, FragmentMyListItem.newInstance(context.getResources().getString(R.string.without_wishes)))
                    .commit();
        }
        return wishes;
    }

    /**
     * Здесь составляется строка, которая будет выводиться в фрагменте "откуда"
     *
     * @param addressStrings - массив с данными адреса, где:
     *                           0 - улица (возможно, что будет записана с домом, в этом случае в индексе 1 будет записан null, если пользователь не записал другое значение)
     *                           1 - дом
     *                           2 - подъезд/объект, к которому нужно подъехать
     *                           3 - комментарий
     * @return Строка адреса
     */
    public static String getLocationString(String[] addressStrings) {
        StringBuilder savedFromBuilder = new StringBuilder();
        if (addressStrings != null) {
            savedFromBuilder.append(addressStrings[0]);
            if (!addressStrings[0].contains(",")) { //Если улица не содержит дом
                savedFromBuilder.append(", ");
                savedFromBuilder.append(addressStrings[1]);
            }
            if (addressStrings[2] != null) {
                savedFromBuilder.append(", ");
                savedFromBuilder.append(addressStrings[2]);
            }
        }
        return savedFromBuilder.toString(); //Сохраненное значение готовое для вывода
    }
}
