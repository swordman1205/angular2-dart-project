package com.qurasense.userApi.samples.objectify;


import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class DatastoreObjectifySample {

    public static Object doSample() {
        Person p = new Person("Clara");
        ofy().save().entity(p).now();
        System.out.println(p.getId());

        Result<Person> result = ofy().load().key(Key.create(Person.class, p.getId()));  // Result is async
        Person fetched1 = result.now();
        System.out.println(fetched1.getFullName());

        Person person = ofy().load().type(Person.class).id(p.getId()).now();
        System.out.println(person.toString());

        ofy().load().type(Person.class).forEach(System.out::println);

        p.setFullName("Zoya Kosmodemjanskaya");
        ofy().save().entity(p).now();
        System.out.println(ofy().load().type(Person.class).id(p.getId()).now().getFullName());

        return null;
    }

    public static void main(String... args) throws Exception {
        ObjectifyService.register(Person.class);
        ObjectifyService.run(() -> doSample());

    }
}
