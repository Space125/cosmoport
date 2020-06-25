package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author Ivan Kurilov on 18.06.2020
 */

public interface ShipService {

    /**
     * Метод для получения списка всех кораблей на основе фильтра
     * Для получение списка кораблей используется {@link Pageable}
     * Для фильтра используется {@link Specification}
     *
     * @param spec     передается подготовленный запрос к БД с использованием {@link Specification},
     *                 который используется в качестве фильтра
     * @param pageable передается объект для постраничного вывода списка кораблей с использованием сортировки
     * @return возвращает список всех кораблей, если не указан ни один параметр фильтра,
     * либо возвращает список кораблей с учетом фильтра
     */
    List<Ship> getAllShips(Specification<Ship> spec, Pageable pageable);

    /**
     * Метод используется для подсчета количества кораблей согласно фильтра
     *
     * @param spec передается подготовленный запрос к БД с использованием {@link Specification},
     *             который используется в качестве фильтра
     * @return возвращает список всех кораблей, если не указан ни один параметр фильтра,
     * либо возвращает список кораблей с учетом фильтра
     */
    List<Ship> getAllShips(Specification<Ship> spec);

    /**
     * Метод создает новый корабль
     *
     * @param ship в качестве параметра передается новый корабль
     */
    void createShip(Ship ship);

    /**
     * Метод выполняет обновление существующего корабля
     *
     * @param updateShip параметр содержит корабль с новыми значениями
     * @param existShip  параметр содержит корабль, который необходимо обновить
     * @return возвращает корабль с обновленными значениями
     */
    Ship updateShip(Ship updateShip, Ship existShip);

    /**
     * Метод получения корабля по идентификатору
     *
     * @param id идентификатор корабля
     * @return возвращает корабль по идентификатору
     */
    Ship getShipById(Long id);

    /**
     * Метод удаления корабля по идентификатору
     *
     * @param id идентификатор корабля
     */
    void deleteShip(Long id);

}
