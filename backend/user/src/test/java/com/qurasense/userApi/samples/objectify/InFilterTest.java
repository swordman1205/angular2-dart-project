package com.qurasense.userApi.samples.objectify;

import java.util.Arrays;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class InFilterTest {

    @Test
    @Ignore
    public void testFilters() {
        ObjectifyService.init();
        ObjectifyService.register(Item.class);
        ObjectifyService.run(() -> {
            Item p1 = new Item("first");
            Item p2 = new Item("second");
            Item p3 = new Item("third");
            ofy().save().entities(p1, p2, p3).now();
            return null;
        });

        ObjectifyService.run(() -> {
            List<Item> list = ofy().load().type(Item.class).filter("model in", Arrays.asList("first", "third")).list();
            System.out.println(list.size());
            return null;
        });
    }

    @Entity
    public static class Item {
        @Id
        private Long id;
        private String model;

        public Item() {
        }

        public Item(String model) {
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

}
