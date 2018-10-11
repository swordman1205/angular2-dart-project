import cgi
import textwrap
import urllib
import datetime

from google.appengine.ext import ndb

import webapp2
import logging

class User(ndb.Model):
    fullName = ndb.StringProperty();
    email = ndb.StringProperty();
    role = ndb.StringProperty();
    encryptedPassword = ndb.StringProperty();
    createTime = ndb.DateTimeProperty();
    lastLoginTime = ndb.DateTimeProperty();

class CustomerInfo(ndb.Model):
    email = ndb.StringProperty();
    fullName = ndb.StringProperty();
    userId = ndb.IntegerProperty();
    customerStatus = ndb.StringProperty();
    state = ndb.StringProperty();
    city = ndb.StringProperty();
    zip = ndb.StringProperty();
    addressLine = ndb.StringProperty();
    country = ndb.StringProperty();
    phone = ndb.StringProperty();
    contactDay = ndb.StringProperty();
    contactTimes = ndb.StringProperty(repeated=True);
    dateOfBirth = ndb.DateTimeProperty();

class HealthInfo(ndb.Model):
    typicalCycleLength = ndb.IntegerProperty();

class Sample(ndb.Model):
    additionalFeedback = ndb.StringProperty();

class Trial(ndb.Model):
    name = ndb.StringProperty();
    method = ndb.StringProperty();
    laboratoryId = ndb.IntegerProperty();

class HealthCustomer(ndb.Model):
    customerUserId = ndb.IntegerProperty();
    customerId = ndb.IntegerProperty();
    nurseId = ndb.IntegerProperty();
    trialId = ndb.IntegerProperty();
    createTime = ndb.DateTimeProperty();

class Wipe(webapp2.RequestHandler):

  def get(self):

    logging.info('start clear datastore')
    user_keys = User.query().fetch(keys_only=True);
    ndb.delete_multi(user_keys);

    customer_info_keys = CustomerInfo.query().fetch(keys_only=True);
    ndb.delete_multi(customer_info_keys);

    health_keys = HealthInfo.query().fetch(keys_only=True);
    ndb.delete_multi(health_keys);

    sample_keys = Sample.query().fetch(keys_only=True);
    ndb.delete_multi(sample_keys);

    trial_keys = Trial.query().fetch(keys_only=True);
    ndb.delete_multi(trial_keys);

    health_customer_keys = HealthCustomer.query().fetch(keys_only=True)
    ndb.delete_multi(health_customer_keys)
    logging.info('datastore was cleared')

    logging.info('start create test users data at datastore')
    secret_encrypted = '$2a$10$mow7QIZkoOoynav4uyDpQ.dAmWP5y7Vv17S8X2k8ZC1sSiymSC2NC';
    admin = User(fullName = 'Admin admin',
                 email = '_admin@qurasense.com',
                 role = 'ADMIN',
                 encryptedPassword = secret_encrypted,
                 createTime = datetime.datetime.now(),
                 lastLoginTime = None,
                 parent = ndb.Key("UserList", "default"));
    admin.put();

    lab = User(fullName = 'Lab technicial',
               email = '_labtech@qurasense.com',
               role = 'LAB_TECH',
               encryptedPassword = secret_encrypted,
               createTime = datetime.datetime.now(),
               lastLoginTime = None,
               parent = ndb.Key("UserList", "default"));
    lab.put();

    medic = User(fullName = 'Medical professional',
                 email = '_medical@qurasense.com',
                 role = 'MEDICAL',
                 encryptedPassword = secret_encrypted,
                 createTime = datetime.datetime.now(),
                 lastLoginTime = None,
                 parent = ndb.Key("UserList", "default"));
    medic.put();

    nurse = User(fullName = 'Nurse nurse',
                 email = '_nurse@qurasense.com',
                 role = 'NURSE',
                 encryptedPassword = secret_encrypted,
                 createTime = datetime.datetime.now(),
                 lastLoginTime = None,
                 parent = ndb.Key("UserList", "default"));
    nurse_key = nurse.put();

    def init_customer_info(user):
        return CustomerInfo(email = user.email,
                          fullName = user.fullName,
                          userId = user.key.id(),
                          customerStatus = 'APPROVED',
                          parent = ndb.Key("UserInfoList", "default"))

    marie = User(fullName = 'Marie Curie',
                 email = '_marie@qurasense.com',
                 role = 'CUSTOMER',
                 encryptedPassword = secret_encrypted,
                 createTime = datetime.datetime.now(),
                 lastLoginTime = None,
                 parent = ndb.Key("UserList", "default"));
    marie_key = marie.put();
    marie_customer_info = init_customer_info(marie);
    marie_customer_info.state = 'CA';
    marie_customer_info.city = 'Palo Alto';
    marie_customer_info.zip = '94020';
    marie_customer_info.addressLine = '1450 Page Mill Rd';
    marie_customer_info.country = "US";
    marie_customer_info.phone = "12345678";
    marie_customer_info.contactDay = 'MON_FRI';
    marie_customer_info.contactTimes = ['AFTER_NOON'];
    marie_customer_info.dateOfBirth = datetime.datetime.now();
    marie_customer_key = marie_customer_info.put();

    lise = User(fullName = 'Lise Meitner',
                email = '_lise@qurasense.com',
                role = 'CUSTOMER',
                encryptedPassword = secret_encrypted,
                createTime = datetime.datetime.now(),
                lastLoginTime = None,
                parent = ndb.Key("UserList", "default"));
    lise_key = lise.put();
    lise_customer_info = init_customer_info(lise);
    lise_customer_info.state = 'CA';
    lise_customer_info.city = 'Palo Alto';
    lise_customer_info.zip = '94020';
    lise_customer_info.addressLine = '1450 Page Mill Rd';
    lise_customer_info.country = "US";
    lise_customer_info.phone = "12345678";
    lise_customer_info.contactDay = 'MON_FRI';
    lise_customer_info.contactTimes = ['AFTER_NOON'];
    lise_customer_info.dateOfBirth = datetime.datetime.now();
    lise_customer_key = lise_customer_info.put();

    san_diego_trial = Trial(name = 'San Diego', method = 'NURSE_VISIT');
    san_diego_trial_key = san_diego_trial.put();

    stanford_trial = Trial(name = 'Stanford', method = 'NURSE_VISIT');
    stanford_trial_key = stanford_trial.put();

    def init_health_customer(userKey, customerKey, nurseKey, trialKey):
        logging.info('userId: %s' % userKey.id())
        logging.info('customerKey: %s' % customerKey.id())
        logging.info('nurseKey: %s' % nurseKey.id())
        logging.info('trialKey: %s' % trialKey.id())
        return HealthCustomer(customerUserId = userKey.id(),
                              customerId = customerKey.id(),
                              nurseId = nurseKey.id(),
                              trialId = trialKey.id(),
                              createTime = datetime.datetime.now(),
                              parent = ndb.Key("HealthCustomerList", "default"))

    marie_health_customer = init_health_customer(marie_key, marie_customer_key, nurse_key, san_diego_trial_key);
    marie_health_customer.put();

    lise_health_customer = init_health_customer(lise_key, lise_customer_key, nurse_key, san_diego_trial_key);
    lise_health_customer.put();

app = webapp2.WSGIApplication(
    [
        ('/cloud-datastore-users-wipe', Wipe),
    ], debug=True)