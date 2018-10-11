package com.qurasense.userApi.samples.objectify;

import java.util.List;
import java.util.UUID;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Subclass;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class InheritanceEntityTest {

    @Test
    @Ignore
    public void testFilters() {
        ObjectifyService.init();
        ObjectifyService.register(Animal.class);
        ObjectifyService.register(Mammal.class);
        ObjectifyService.register(Cat.class);
        String animalId = ObjectifyService.run(() -> {
            Animal animal = new Animal();
            animal.setId(UUID.randomUUID().toString());
            ofy().save().entity(animal).now();
            return animal.id;
        });
        ObjectifyService.run(() -> {
            Animal loaded = ofy().load().type(Animal.class).id(animalId).now();
            Assert.assertNotNull(loaded);
            return null;
        });
        ObjectifyService.run(() -> {
            Mammal mammal = new Mammal();
            mammal.setId(UUID.randomUUID().toString());
            mammal.setLongHair(true);
            ofy().save().entity(mammal).now();
            return mammal.getId();
        });
        ObjectifyService.run(() -> {
            Cat cat = new Cat();
            cat.setId(UUID.randomUUID().toString());
            cat.setLongHair(true);
            cat.setHypoallergenic(true);
            ofy().save().entity(cat).now();
            return cat.getId();
        });
        ObjectifyService.run(() -> {
            List<Animal> animals = ofy().load().type(Animal.class).list();
            System.out.println(animals.size());
            return null;
        });
    }

    @Entity
    public static class Animal {
        @Id
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


    @Subclass(index=true)
    public static class Mammal extends Animal {
        boolean longHair;

        public boolean isLongHair() {
            return longHair;
        }

        public void setLongHair(boolean longHair) {
            this.longHair = longHair;
        }
    }

    @Subclass(index=true)
    public static class Cat extends Mammal {
        boolean hypoallergenic;

        public boolean isHypoallergenic() {
            return hypoallergenic;
        }

        public void setHypoallergenic(boolean hypoallergenic) {
            this.hypoallergenic = hypoallergenic;
        }
    }

}
