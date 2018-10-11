package com.qurasense.userApi.samples.objectify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class RefTest {

    @Test
    @Ignore
    public void test() {
        ObjectifyService.init();
        ObjectifyService.register(Car.class);
        ObjectifyService.register(Passenger.class);
        String carId = UUID.randomUUID().toString();
        String bobId = UUID.randomUUID().toString();
        ObjectifyService.run(() -> {

            Car car = new Car();
            car.setId(carId);

            Map<Key<Passenger>, Passenger> passengerMap = ofy().save().entities(
                    new Passenger(bobId, "Rob"),
                    new Passenger(UUID.randomUUID().toString(), "Nicky"),
                    new Passenger(UUID.randomUUID().toString(), "Scott")).now();
            passengerMap.values().forEach(car::addPassenger);
            ofy().save().entity(car).now();
            return null;
        });

        ObjectifyService.run(() -> {
            Car car = ofy().load().type(Car.class).id(carId).now();
            Assert.assertEquals(3, car.getPassengers().size());
            return null;
        });

        ObjectifyService.run(() -> {
            Passenger passenger = ofy().load().type(Passenger.class).id(bobId).now();
            Assert.assertNotNull(passenger);
            return null;
        });

        ObjectifyService.run(() -> {
            List<Passenger> passengers = ofy().load().type(Passenger.class).list();
            Assert.assertEquals(3, passengers.size());
            return null;
        });
    }

    @Entity
    public static class Passenger {
        @Id
        private String id;
        private String name;

        public Passenger() {
        }

        public Passenger(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Entity
    public static class Car {
        @Id
        private String id;

        private List<Ref<Passenger>> passengers = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Passenger> getPassengers() {
            List<Ref<Passenger>> passengers = this.passengers;
            return passengers.stream().map(r->r.get()).collect(Collectors.toList());
        }

        public void addPassenger(Passenger passenger) {
            passengers.add(Ref.create(Key.create(passenger)));
        }

    }
}
