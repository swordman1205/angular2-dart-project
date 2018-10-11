package com.qurasense.userApi.samples.objectify;


import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class FilterTest {

    @Test
    @Ignore
    public void testFilters() {
        ObjectifyService.register(Car.class);
        ObjectifyService.run(() -> {
            Car p = new Car("BMW", 250);
            ofy().save().entity(p).now();

            Car carByModel = ofy().load().type(Car.class).filter("model", p.getModel()).first().now();
            Car carByHorses = ofy().load().type(Car.class).filter("horses >=", 1).first().now();
            System.out.println(carByModel);
            System.out.println(carByHorses);
            Assert.assertTrue(carByModel != null && carByHorses != null);
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
