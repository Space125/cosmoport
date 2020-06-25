package com.space.utility;

import com.space.model.Ship;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author Ivan Kurilov on 19.06.2020
 */
public class CalcRatingShipUtility {

    /**
     * Метод расчета райтинга коробля
     * <p>
     * Формула расчета R = 80 * v * k / y0 − y1 + 1,
     * где:
     * v - скорость коробля;
     * k — коэффициент, который равен 1 для нового корабля и 0,5 для использованного;
     * y0 — текущий год (не забудь, что «сейчас» 3019 год);
     * y1 — год выпуска корабля.
     *
     * @param ship в качестве параметра передается корабль
     * @return возвращает значение рейтинга коробля
     */
    public static Double calcRatingShip(Ship ship) {

        double k = ship.getUsed() ? 0.5 : 1.0;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setTime(ship.getProdDate());

        int prodDate = calendar.get(Calendar.YEAR);

        return BigDecimal.valueOf((80 * ship.getSpeed() * k) / (3019 - prodDate + 1))
                .setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
}
