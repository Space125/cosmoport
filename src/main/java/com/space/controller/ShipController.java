package com.space.controller;

import com.space.Specification.ShipSpecification;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Kurilov on 18.06.2020
 */

@RestController
@RequestMapping(value = "/rest")
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }


    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    public List<Ship> getAllShips(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "planet", required = false) String planet,
            @RequestParam(name = "shipType", required = false) ShipType shipType,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed", required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating,
            @RequestParam(name = "order", required = false, defaultValue = "ID") ShipOrder order,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "3") Integer pageSize
    ) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        Specification<Ship> spec = ShipSpecification.getAllSpecification(
                name, planet, shipType,
                after, before,
                isUsed,
                minSpeed, maxSpeed,
                minCrewSize, maxCrewSize,
                minRating, maxRating);

        return shipService.getAllShips(spec, pageable);
    }

    ;


    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public @ResponseBody
    Integer getCount(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "planet", required = false) String planet,
            @RequestParam(name = "shipType", required = false) ShipType shipType,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed", required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating
    ) {

        Specification<Ship> spec = ShipSpecification.getAllSpecification(
                name, planet, shipType,
                after, before,
                isUsed,
                minSpeed, maxSpeed,
                minCrewSize, maxCrewSize,
                minRating, maxRating);

        return shipService.getAllShips(spec).size();
    }

    @GetMapping(value = "/ships/{id}")
    @ResponseBody
    public ResponseEntity<Ship> getShipId(@PathVariable("id") Long shipId) {

        if (shipId == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shipService.getShipById(shipId);

        if (ship == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(ship, HttpStatus.OK);

    }

    @PostMapping(value = "/ships")
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {

        try {
            shipService.createShip(ship);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(ship);
    }

    @PostMapping(value = "/ships/{id}")
    public ResponseEntity<Ship> updateShip(@RequestBody Ship ship, @PathVariable Long id) {

        if (id == 0) return ResponseEntity.badRequest().body(ship);

        Ship checkExistShip = shipService.getShipById(id);
        if (checkExistShip == null) return ResponseEntity.notFound().build();

        Ship updateShip;
        try {
            updateShip = shipService.updateShip(ship, checkExistShip);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ship);
        }
        return ResponseEntity.ok().body(updateShip);

    }

    @DeleteMapping(value = "/ships/{id}")
    @ResponseBody
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") Long id) {

        if (id == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shipService.getShipById(id);

        if (ship == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        shipService.deleteShip(id);

        return new ResponseEntity<>(HttpStatus.OK);

    }


}
