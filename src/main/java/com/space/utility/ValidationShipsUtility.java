package com.space.utility;

import com.space.model.Ship;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Ivan Kurilov on 19.06.2020
 */

public class ValidationShipsUtility {


    /**
     * Основной метод валидации параметров создаваемого корабля
     * <p>
     * Проверяет параметры по следующим правилам:
     * - длина значения параметра “name” или “planet” превышает
     * размер соответствующего поля в БД (50 символов);
     * - значение параметра “name” или “planet” пустая строка;
     * - скорость или размер команды находятся вне заданных пределов;
     * - “prodDate”:[Long] < 0;
     * - год производства находятся вне заданных пределов.
     *
     * @param ship в качестве параметра передается создаваемы корабль
     * @return возвращает true если все параметры не валидны
     */
    public static boolean isShipNotValid(Ship ship) {
        return isNameLengthNotValid(ship.getName())
                || isPlanetLengthNotValid(ship.getPlanet())
                || isCrewSizeNotValid(ship.getCrewSize())
                || isSpeedNotValid(ship.getSpeed())
                || isProdDateNotValid(ship.getProdDate());
    }

    /**
     * Метод проверяет ограничение длинны параметра name,
     * которое не должно превышать 50 символов
     *
     * @param name в качестве параметра name передается название корабля
     * @return возвращает true если название корабля более 50 символов
     */
    private static boolean isNameLengthNotValid(String name) {
        return name != null && (name.length() < 1 || name.length() > 50);
    }

    /**
     * Метод проверяет ограничение длинны параметра name,
     * которое не должно превышать 50 символов
     *
     * @param planet в качестве параметра planet передается название планеты
     * @return возвращает true если название планеты более 50 символов
     */
    private static boolean isPlanetLengthNotValid(String planet) {
        return planet != null && (planet.length() < 1 || planet.length() > 50);
    }

    /**
     * Метод проверяет допустимое значение членов экипажа корабля
     * Диапазон значений 1 - 9999 включительно.
     *
     * @param crewSize количество членов экипажа
     * @return возвращает true если количество членов экипажа находится вне диапазоне
     */
    private static boolean isCrewSizeNotValid(Integer crewSize) {
        return crewSize != null && (crewSize < 1 || crewSize > 9999);
    }

    /**
     * Метод проверяет допустимое значение скорости корабля
     * Диапазон значений 0,01 - 0,99 включительно.
     * Используется математическое округление до сотых.
     *
     * @param speed скорость корабля
     * @return возвращает true если скорость корабля вне разрешенного диапазона
     */
    private static boolean isSpeedNotValid(Double speed) {
        if (speed != null) {
            double roundSpeed = BigDecimal.valueOf(speed)
                    .setScale(2, RoundingMode.HALF_EVEN).doubleValue();

            return roundSpeed < 0.01 || roundSpeed > 0.99;

        }
        return false;
    }

    /**
     * Метод проверяет допустимые значения диапазона даты производства корабля
     * Диапазон значений года 2800 - 3019 включительно;
     * “prodDate”:[Long] < 0.
     *
     * @param date дата производства корабля
     * @return возвращает true если дата находится вне разрешенного диапазона
     */
    private static boolean isProdDateNotValid(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
            calendar.setTime(date);

            int prodDate = calendar.get(Calendar.YEAR);


            return date.getTime() < 0 && prodDate < 2800 || prodDate > 3019;
        }
        return false;
    }

}
