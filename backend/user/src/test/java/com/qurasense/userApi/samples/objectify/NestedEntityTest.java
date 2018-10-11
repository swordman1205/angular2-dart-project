package com.qurasense.userApi.samples.objectify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class NestedEntityTest {

    @Test
    @Ignore
    public void testFilters() {
        ObjectifyService.init();
        ObjectifyService.register(Item.class);
        Long itemId = ObjectifyService.run(() -> {
            Status firstStatus = new Status("first", new Date());
            Item p = new Item("stuff", firstStatus);
            ofy().save().entity(p).now();
            return p.id;
        });

        ObjectifyService.run(() -> {
            Item loaded = ofy().load().type(Item.class).id(itemId).now();
            Status secondStatus = new Status("second", new Date());
            loaded.getOldStatuses().add(loaded.getStatus());
            loaded.setStatus(secondStatus);
            ofy().save().entity(loaded).now();
            return null;
        });

        ObjectifyService.run(() -> {

            Item loaded = ofy().load().type(Item.class).id(itemId).now();
            Status thirdStatus = new Status("third", new Date());
            loaded.getOldStatuses().add(loaded.getStatus());
            loaded.setStatus(thirdStatus);
            ofy().save().entity(loaded).now();
            return null;
        });

        ObjectifyService.run(() -> {
            Item loaded = ofy().load().type(Item.class).id(itemId).now();
            Assert.assertEquals(2, loaded.getOldStatuses().size());
            Assert.assertEquals("third", loaded.getStatus().getState());
            return null;
        });
    }

    @Entity
    public static class Item {
        @Id
        private Long id;
        private String model;
        private Status status;
        private List<Status> oldStatuses = new ArrayList<>();

        public Item() {
        }

        public Item(String model, Status status) {
            this.model = model;
            this.status = status;
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

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public List<Status> getOldStatuses() {
            return oldStatuses;
        }

        public void setOldStatuses(List<Status> oldStatuses) {
            this.oldStatuses = oldStatuses;
        }
    }

    public static class Status {
        private String state;
        private Date time;

        public Status() {
        }

        public Status(String state, Date time) {
            this.state = state;
            this.time = time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }

}
