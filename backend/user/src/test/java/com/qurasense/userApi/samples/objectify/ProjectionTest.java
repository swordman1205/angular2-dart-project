package com.qurasense.userApi.samples.objectify;


import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ProjectionTest {

    @Test
    @Ignore
    public void test() {
        ObjectifyService.register(Car.class);
        ObjectifyService.run(() -> {
            Car p = new Car("BMW", 250);
            ofy().save().entity(p).now();

            Car carByModel = ofy().load().type(Car.class).filter("model", p.getModel()).project("horses").first().now();
            System.out.println(carByModel);
            return null;
        });

    }

    @Entity
    public static class Car {
        @Id
        private Long id;
        @Index
        private String model;
        @Index
        private int horses;

        public Car() {
        }

        public Car(String model, int horses) {
            this.model = model;
            this.horses = horses;
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

        public int getHorses() {
            return horses;
        }

        public void setHorses(int horses) {
            this.horses = horses;
        }
    }
}
