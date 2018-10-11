package com.qurasense.userApi.samples.objectify;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.AlsoLoad;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class TransformTest {

    @Test
    @Ignore
    public void test() {
        ObjectifyService.register(Car.class);
        ObjectifyService.run(() -> {
            List<Car> list = ofy().load().type(Car.class).list();
            for (Car car: list) {
                System.out.println(car.getTwicePower() + " " + car.getHalfPoser());
            }
            return null;
        });
    }

    @Entity
    public static class Car {
        @Id
        private Long id;
        private String model;
        private int halfPoser;
        private int twicePower;

        public Car() {
        }

        void importPower(@AlsoLoad({"horses", "power"}) Integer origPower) {
            halfPoser = origPower / 2;
            twicePower = origPower * 2;
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

        public int getHalfPoser() {
            return halfPoser;
        }

        public int getTwicePower() {
            return twicePower;
        }
    }
}
