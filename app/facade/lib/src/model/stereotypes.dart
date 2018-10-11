part of app_facade;

//enum OrganizationType { LABORATORY, COLLECTOR, CLINICAL, MANUFACTURER, LOGISTICS }
//class Organization extends Identifiable with Nameable {
//  OrganizationType type;
//}
//


class Nameable {
  String name;
}
//
class CreateStamp {
  String createUserId;
  DateTime createTime;
}
//
class Identifiable {
  String id;
}

// functionality
// - get owners
// - get currentOwner