// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class CloneUtils {

  static User clone(User input) {
    User output = new User();
    output = new User();
    output.id = input.id;
    output.fullName = input.fullName;
    output.email = input.email;
    output.createTime = input.createTime;
    output.role = input.role;
    output.lastLoginTime = input.lastLoginTime;
    return output;
  }

}