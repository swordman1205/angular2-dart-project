package com.qurasense.userApi.samples.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.*;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ChangeOnSaveTest {

    @Test
    @Ignore
    public void test() {
        ObjectifyService.register(Car.class);
        ObjectifyService.register(CarDetails.class);
        ObjectifyService.run(() -> {
            List<Car> cars = ofy().load().type(Car.class).list();
            for (Car car: cars) {
                System.out.println("From car:" + car.getCarDetails().getModel());
            }
            List<CarDetails> carDetails = ofy().load().type(CarDetails.class).list();
            for (CarDetails carDetail: carDetails) {
                System.out.println("From cardetails: " + carDetail.getModel());
            }
            return null;
        });
    }

    @Entity
    public static class CarDetails {
        @Id
        private Long id;
        private String model;

        public CarDetails() {
        }

        public CarDetails(String model) {
            this.model = model;
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
    }

    @Entity
    public static class Car {
        @Id
        private Long id;

        @IgnoreSave
        private String model;

        CarDetails carDetails;

        public Car() {
        }

        @OnLoad
        void onLoad() {
            if (this.model != null) {
                this.carDetails = new CarDetails(this.model);
                ofy().save().entity(this.carDetails).now();
                ofy().save().entity(this).now();
            }
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

        public CarDetails getCarDetails() {
            return carDetails;
        }

        public void setCarDetails(CarDetails carDetails) {
            this.carDetails = carDetails;
        }
    }
}
