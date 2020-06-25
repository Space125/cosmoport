package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import com.space.utility.CalcRatingShipUtility;
import com.space.utility.ValidationShipsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Ivan Kurilov on 18.06.2020
 */

@Service
@Transactional
public class ShipServiceIml implements ShipService {

    private ShipRepository shipRepository;

    public ShipServiceIml() {
    }

    @Autowired
    public ShipServiceIml(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }


    @Override
    public List<Ship> getAllShips(Specification<Ship> spec, Pageable pageable) {
        return shipRepository.findAll(spec, pageable).getContent();
    }

    @Override
    public List<Ship> getAllShips(Specification<Ship> spec) {
        return shipRepository.findAll(spec);
    }

    @Override
    public void createShip(Ship ship) {

        if (ValidationShipsUtility.isShipNotValid(ship)) {
            throw new IllegalArgumentException();
        }

        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }

        ship.setRating(CalcRatingShipUtility.calcRatingShip(ship));


        shipRepository.save(ship);
    }

    @Override
    public Ship updateShip(Ship ship, Ship existsShip) {

        if (ValidationShipsUtility.isShipNotValid(ship)) {
            throw new IllegalArgumentException();
        }

        Field[] fields = existsShip.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("id") && !field.getName().equals("rating")) {
                    Object value = field.get(ship);
                    if (value == null) {
                        value = field.get(getShipById(existsShip.getId()));
                    }
                    field.set(existsShip, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        existsShip.setRating(CalcRatingShipUtility.calcRatingShip(existsShip));


        return shipRepository.save(existsShip);
    }

    @Override
    public Ship getShipById(Long id) {
        return shipRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteShip(Long id) {
        shipRepository.deleteById(id);
    }


}
