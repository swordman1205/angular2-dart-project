part of app_facade;

// int
int _uidSequence = 0;
String _nextId() {
  return (++_uidSequence).toString();
}

// uuid
var uuid = new Uuid();
String _nextUid() {
  return uuid.v1();
}


