package com.space.Specification;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

/**
 * @author Ivan Kurilov on 22.06.2020
 */
public class ShipSpecification {

    /**
     * Метод возвращает данные из базы по различным фильтрам
     *
     * @param name        наименование корабля
     * @param planet      наименование планеты
     * @param shipType    тип корабля
     * @param after       дата производства начало интервала
     * @param before      дата производства окончание интервала
     * @param isUsed      признак использования корабля true используется false не используется
     * @param minSpeed    минимальная скорость корабля
     * @param maxSpeed    максимальная скорость корабля
     * @param minCrewSize минимальное количество членов экипажа
     * @param maxCrewSize максимальное количество членов экипажа
     * @param minRating   минимальный рейтинг корабля
     * @param maxRating   максимальный рейтинг корабля
     * @return возвращает список кораблей согласно фильтра
     */
    public static Specification<Ship> getAllSpecification(
            String name,
            String planet,
            ShipType shipType,
            Long after, Long before,
            Boolean isUsed,
            Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize,
            Double minRating, Double maxRating) {
        return Specification.where(specShipsByName(name))
                .and(specShipsByPlanet(planet))
                .and(specShipsByShipType(shipType))
                .and(specShipsByProdDateBetween(after, before))
                .and(specShipsByIsUsed(isUsed))
                .and(specShipsBySpeedBetween(minSpeed, maxSpeed))
                .and(specShipsByCrewSizeBetween(minCrewSize, maxCrewSize))
                .and(specShipsByRatingBetween(minRating, maxRating));
    }

    /**
     * Метод отфильтровывает корабли по наименованию корабля
     *
     * @param name наименование корабля
     * @return если name указан возвращает корабли, в которых содержится
     * как полное, так и частичное имя корабля
     */
    private static Specification<Ship> specShipsByName(String name) {
        return (r, q, cb) -> name == null ? null : cb.like(r.get("name"), "%" + name + "%");
    }

    /**
     * Метод отфильтровывает корабли по наименованию планеты
     *
     * @param planet наименование планеты
     * @return если planet указан возвращает корабли, в которых содержится
     * как полное так и частичное наименование планеты
     */
    private static Specification<Ship> specShipsByPlanet(String planet) {
        return (r, q, cb) -> planet == null ? null : cb.like(r.get("planet"), "%" + planet + "%");
    }

    /**
     * Метод отфильтровывает корабли по типу
     *
     * @param shipType тип корабля
     * @return если тип указан возвращает корабли по типам TRANSPORT, MILITARY, MERCHANT
     */
    private static Specification<Ship> specShipsByShipType(ShipType shipType) {
        return (r, g, cb) -> shipType == null ? null : cb.equal(r.get("shipType"), shipType);
    }

    /**
     * Метод отфильтровывает корабли по дате производства
     *
     * @param after  дата производства начало интервала
     * @param before дата производства окончание интервала
     * @return возвращает отфильтрованный список по указанному диапазону даты производства
     */
    private static Specification<Ship> specShipsByProdDateBetween(Long after, Long before) {
        return (r, g, cb) -> {
            if (after == null && before == null) return null;
            if (after == null) {
                Date dateBefore = new Date(before);
                return cb.lessThanOrEqualTo(r.get("prodDate"), dateBefore);
            }
            if (before == null) {
                Date dateAfter = new Date(after);
                return cb.greaterThanOrEqualTo(r.get("prodDate"), dateAfter);
            }

            Date dateAfter = new Date(after);
            Date dateBefore = new Date(before);

            return cb.between(r.get("prodDate"), dateAfter, dateBefore);
        };
    }

    /**
     * Метод отфильтровывает корабли по использованию
     *
     * @param isUsed признак использования корабля, true используется false не используется
     * @return возвращает отфильтрованный список по признаку использования
     */
    private static Specification<Ship> specShipsByIsUsed(Boolean isUsed) {
        return (r, q, cb) -> {
            if (isUsed == null) return null;
            if (isUsed) {
                return cb.isTrue(r.get("isUsed"));
            } else {
                return cb.isFalse(r.get("isUsed"));
            }
        };
    }

    /**
     * Метод отфильтровывает корабли по указанному диапазону скорости
     *
     * @param minSpeed минимальная скорость корабля
     * @param maxSpeed максимальная скорость корабля
     * @return возвращает отфильтрованный список по указанному диапазону скорости
     */
    private static Specification<Ship> specShipsBySpeedBetween(Double minSpeed, Double maxSpeed) {
        return (r, q, cb) -> {
            if (minSpeed == null && maxSpeed == null) return null;
            if (minSpeed == null) return cb.lessThanOrEqualTo(r.get("speed"), maxSpeed);
            if (maxSpeed == null) return cb.greaterThanOrEqualTo(r.get("speed"), minSpeed);

            return cb.between(r.get("speed"), minSpeed, maxSpeed);
        };
    }

    /**
     * Метод отфильтровывает корабли по диапазону членов экипажа
     *
     * @param minCrewSize минимальное количество членов экипажа
     * @param maxCrewSize максимальное количество членов экипажа
     * @return возвращает отфильтрованный список по указанному диапазону членов экипажа
     */
    private static Specification<Ship> specShipsByCrewSizeBetween(Integer minCrewSize, Integer maxCrewSize) {
        return (r, q, cb) -> {
            if (minCrewSize == null && maxCrewSize == null) return null;
            if (minCrewSize == null) return cb.lessThanOrEqualTo(r.get("crewSize"), maxCrewSize);
            if (maxCrewSize == null) return cb.greaterThanOrEqualTo(r.get("crewSize"), minCrewSize);

            return cb.between(r.get("crewSize"), minCrewSize, maxCrewSize);
        };
    }

    /**
     * Метод отфильтровывает корабли по диапазону рейтинга
     *
     * @param minRating минимальный рейтинг корабля
     * @param maxRating максимальный рейтинг корабля
     * @return возвращает отфильтрованный список по указанному диапазону рейтинга
     */
    private static Specification<Ship> specShipsByRatingBetween(Double minRating, Double maxRating) {
        return (r, q, cb) -> {
            if (minRating == null && maxRating == null) return null;
            if (minRating == null) return cb.lessThanOrEqualTo(r.get("rating"), maxRating);
            if (maxRating == null) return cb.greaterThanOrEqualTo(r.get("rating"), minRating);

            return cb.between(r.get("rating"), minRating, maxRating);
        };
    }
}
