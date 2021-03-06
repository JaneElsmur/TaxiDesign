package janeelsmur.taxi.Utilites;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import java.util.ArrayList;

public class SharedPrefManager {

    private final String TAG = "SavingLocations";

    public static final String FROM_LOCATION = "fromLocation";
    public static final String WHERE_LOCATION = "whereLocation";
    public static final String WISH = "wish";
    public static final String PAY_WAY = "payWay";
    public static final String TARIFF = "tariff";
    public static final String TIME = "time";
    public static final String AS_SOON_AS_POSSIBLE = "now";
    public static final String TIME_AS_TEXT = "timeAsText";
    public static final String FAVORITE = "favorite";
    public static final String SAVED_FAVORITES_COUNTER = "savedFavCounter";

    /** Это значение по умолчанию возвращается в значения массива
     *  с сохраненными значениями пикеров, если до этого там не было значений */
    public static final int TIME_IS_NOT_SAVED = 10000;
    /** Количество сохраняемых пожеланий. Если нужно добавить еще, то нужно увеличить эту константу */
    public static final int WISHES_COUNT = 8;

    private SharedPreferences sharedPreferences;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
    }

//    public void saveFavotiteLocation(String[] favoriteLocation) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(FAVORITE + 0, fromLocation[0]);
//        editor.putString(FAVORITE + 1, fromLocation[1]);
//        editor.putString(FAVORITE + 2, fromLocation[2]);
//        editor.putString(FAVORITE + 3, fromLocation[3]);
//    }

    /** Сохранение адреса "от куда"
     *
     * @param fromLocation:
     *                    index 0 - улица (возможно, что будет записана с домом, в этом случае в индексе 1 будет записан null, если пользователь не записал другое значение)
     *                    index 1 - дом
     *                    index 2 - подъезд/объект, к которому нужно подъехать
     *                    index 3 - addition **/
    public void saveFromLocation(String[] fromLocation) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FROM_LOCATION + 0, fromLocation[0]);
        editor.putString(FROM_LOCATION + 1, fromLocation[1]);
        editor.putString(FROM_LOCATION + 2, fromLocation[2]);
        editor.putString(FROM_LOCATION + 3, fromLocation[3]);
        editor.commit();
    }

    /** Сохранение адреса "куда"
     *
     * @param whereLocation:
     *                    index 0 - улица (возможно, что будет записана с домом, в этом случае в индексе 1 будет записан null, если пользователь не записал другое значение)
     *                    index 1 - дом
     *                    index 2 - подъезд/объект, к которому нужно подъехать
     *                    index 3 - комментарий **/
    public void saveWhereLocation(String[] whereLocation, int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WHERE_LOCATION + id + 0, whereLocation[0]);
        editor.putString(WHERE_LOCATION + id + 1, whereLocation[1]);
        editor.putString(WHERE_LOCATION + id + 2, whereLocation[2]);
        editor.putString(WHERE_LOCATION + id + 3, whereLocation[3]);
        editor.commit();
    }

    /** Удаление адреса "куда" **/
    public void deleteWhereLocation(int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(WHERE_LOCATION + id + 0);
        editor.remove(WHERE_LOCATION + id + 1);
        editor.remove(WHERE_LOCATION + id + 2);
        editor.remove(WHERE_LOCATION + id + 3);
        editor.commit();
    }

    /** Получение адреса "откуда"
     *
     * @return  whereLocation:
     *                    index 0 - улица (возможно, что будет записана с домом, в этом случае в индексе 1 будет записан null, если пользователь не записал другое значение)
     *                    index 1 - дом
     *                    index 2 - подъезд/объект, к которому нужно подъехать
     *                    index 3 - комментарий **/
    public String[] getWhereLocation(int id) {
        String[] whereLocation = new String[4];
        whereLocation[0] = sharedPreferences.getString(WHERE_LOCATION + id + 0, null);
        if(whereLocation[0] == null) return null;
        whereLocation[1] = sharedPreferences.getString(WHERE_LOCATION + id + 1, null);
        whereLocation[2] = sharedPreferences.getString(WHERE_LOCATION + id + 2, null);
        whereLocation[3] = sharedPreferences.getString(WHERE_LOCATION + id + 3, null);
        return whereLocation; //Иначе возвращаем весь массив
    }

    /** Получение адреса "куда"
     *
     * @return  fromLocation:
     *                    index 0 - улица (возможно, что будет записана с домом, в этом случае в индексе 1 будет записан null, если пользователь не записал другое значение)
     *                    index 1 - дом
     *                    index 2 - подъезд/объект, к которому нужно подъехать
     *                    index 3 - комментарий **/
    public String[] getFromLocation() {
        String[] fromLocation = new String[4];
        fromLocation[0] = sharedPreferences.getString(FROM_LOCATION + 0, null);
        if(fromLocation[0] == null) return null; //Если аддрес пустой, то ничего не возвращаем
        fromLocation[1] = sharedPreferences.getString(FROM_LOCATION + 1, null);
        fromLocation[2] = sharedPreferences.getString(FROM_LOCATION + 2, null);
        fromLocation[3] = sharedPreferences.getString(FROM_LOCATION + 3, null);
        return fromLocation; //Иначе возвращаем весь массив
    }

    /** Сохранение адреса в избранное
     *
     * @param   favoriteAddress - массив значений адреса
     *                    index 0 - улица (возможно, что будет записана с домом, в этом случае в индексе 1 будет записан null, если пользователь не записал другое значение)
     *                    index 1 - дом
     *                    index 2 - подъезд/объект, к которому нужно подъехать
     *                    index 3 - комментарий **/
    public void saveAddressInFavorites(String[] favoriteAddress) {
        int currentId = sharedPreferences.getInt(SAVED_FAVORITES_COUNTER, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FAVORITE + currentId + 0, favoriteAddress[0]);
        editor.putString(FAVORITE + currentId + 1, favoriteAddress[1]);
        editor.putString(FAVORITE + currentId + 2, favoriteAddress[2]);
        editor.putString(FAVORITE + currentId + 3, favoriteAddress[3]);
        editor.commit();
        incrementFavoritesId();
    }

    /** Получение адреса из избранного
     *
     * @param addressId - id сохраненного адреса
     *
     * @return  index 0 - улица (возможно, что будет записана с домом, в этом случае в индексе 1 будет записан null, если пользователь не записал другое значение)
     *          index 1 - дом
     *          index 2 - подъезд/объект, к которому нужно подъехать
     *          index 3 - комментарий **/
    public String[] getSavedFavoriteAddress(int addressId){
        String[] address = new String[4];
        address[0] = sharedPreferences.getString(FAVORITE + addressId + 0, null);
        if(address[0]==null) return null; //Если аддрес пустой, то ничего не возвращаем
        address[1] = sharedPreferences.getString(FAVORITE + addressId + 1, null);
        address[2] = sharedPreferences.getString(FAVORITE + addressId + 2, null);
        address[3] = sharedPreferences.getString(FAVORITE + addressId + 3, null);
        return address;
    }

    /** Удаление избранного адреса
     *
     * @param addressId - id адреса
     */
    public void deleteAddressFromFavorites(int addressId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(FAVORITE + addressId + 0);
        editor.remove(FAVORITE + addressId + 1);
        editor.remove(FAVORITE + addressId + 2);
        editor.remove(FAVORITE + addressId + 3);
        editor.commit();
    }

    private void incrementFavoritesId() {
        int currentId = sharedPreferences.getInt(SAVED_FAVORITES_COUNTER, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SAVED_FAVORITES_COUNTER, currentId + 1);
        editor.apply();
    }

    public int getFavoritesMaxId() {
        return sharedPreferences.getInt(SAVED_FAVORITES_COUNTER, 0);
    }


    /** Сохранение пожеланий
     *
     * @param wishes - массив с чекбоксами пожеланий
     *               0 - Нужна сдача
     *               1 - Нужно детское кресло
     *               2 - Некурящий салон
     *               3 - Провоз животкного
     *               4 - I don't speck Russian
     *               5 - Нужен пустой багажник
     *               6 - Нужен универсал
     *               7 - Нужен чек
     */
    public void saveWishes(CheckBox[] wishes, int wishesCount){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int i = 0; i<wishesCount; i++) {
            Log.d(TAG, "saveWishes: " + wishes[i].isChecked());
            editor.putBoolean(WISH + i, wishes[i].isChecked());
        }
        editor.commit();
    }

    /** Взять сохраненные пожелания
     *
     * @return wishes - массив с чекбоксами пожеланий
     *               0 - Нужна сдача
     *               1 - Нужно детское кресло
     *               2 - Некурящий салон
     *               3 - Провоз животкного
     *               4 - I don't speck Russian
     *               5 - Нужен пустой багажник
     *               6 - Нужен универсал
     *               7 - Нужен чек
     */
    public boolean[] getWishes(int wishesCount) {
        boolean[] checkBoxes = new boolean[wishesCount];
        for (int i = 0; i<wishesCount; i++){
            Log.d(TAG, "getWishes: " + sharedPreferences.getBoolean(WISH + i, false));
            checkBoxes[i] = sharedPreferences.getBoolean(WISH + i, false);
        }
        return checkBoxes;
    }

    /** Сохранение способа платежа
     *
     * @param payWay - способ платежа
     *               0 - наличные
     *               1 - через Сбербанк Онлайн
     *               2 - картой
     */
    public void savePayWay(int payWay) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PAY_WAY, payWay);
        editor.commit();
    }

    /** Получение способа платежа
     *
     * @return payWay - способ платежа
     *               0 - наличные
     *               1 - через Сбербанк Онлайн
     *               2 - картой
     */
    public int getSavedPayWay() {
        return sharedPreferences.getInt(PAY_WAY, 0);
    }

    /** Сохранение выбранного тарифа
     *
     * @param tariff - тариф
     *               0 - первый
     *               1 - второй
     *               2 - третий
     *               3 - четвертый
     */
    public void saveTariff(int tariff) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TARIFF, tariff);
        editor.commit();
    }

    /** Получение сохраненного тарифа
     *
     * @return tariff - тариф
     *               0 - первый
     *               1 - второй
     *               2 - третий
     *               3 - четвертый
     */
    public int getSavedTariff() {
        return sharedPreferences.getInt(TARIFF, 0);
    }

    /** Сохранение выбранных времени и даты заказа такси
     *
     * @param pickersValues - массив со значениями пикеров
     *                      0 - дата
     *                      1 - часы
     *                      2 - минуты
     * @param asSoonAsPossible - выбрано ли "Как можно скорее"
     *
     * @param timeAsText - время, переведенное в текст
     */
    public void saveTime(int[] pickersValues, boolean asSoonAsPossible, String timeAsText) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int i = 0; i<3; i++){
            editor.putInt(TIME + i, pickersValues[i]);
        }
        editor.putBoolean(AS_SOON_AS_POSSIBLE, asSoonAsPossible);
        editor.putString(TIME_AS_TEXT, timeAsText);
        editor.commit();
    }

    /** Получение сохраненный ханее даты и времени заказа такси
     *
     * @return Массив со значениями пикеров
     *                      0 - дата
     *                      1 - часы
     *                      2 - минуты
     */
    public int[] getSavedTime() {
        int[] savedTime = new int[3];
        for (int i = 0; i<3; i++) {
            savedTime[i] = sharedPreferences.getInt(TIME + i, TIME_IS_NOT_SAVED);
        }
        return savedTime;
    }

    public String getSavedTimeAsText() {
        return sharedPreferences.getString(TIME_AS_TEXT, null);
    }

    public boolean isAsSoonAsPossibleChecked(){
        return sharedPreferences.getBoolean(AS_SOON_AS_POSSIBLE, true);
    }
}
