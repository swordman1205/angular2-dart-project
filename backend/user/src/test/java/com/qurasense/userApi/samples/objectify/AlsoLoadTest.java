package com.qurasense.userApi.samples.objectify;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.AlsoLoad;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AlsoLoadTest {

    @Test
    @Ignore
    public void testFilters() {
        ObjectifyService.register(Car.class);
        ObjectifyService.run(() -> {
            Car p = new Car("BMW", 250);
            ofy().save().entity(p).now();

            List<Car> list = ofy().load().type(Car.class).list();
            for (Car car: list) {
                System.out.println(car.getPower());
            }
            return null;
        });
    }

    @Entity
    public static class Car {
        @Id
        private Long id;
        private String model;
        @AlsoLoad("horses")
        private int power;

        public Car() {
        }

        public Car(String model, int power) {
            this.model = model;
            this.power = power;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }
    }
}
