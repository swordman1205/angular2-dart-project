// Copyright (c) 2017, Qurasense. All rights reserved.

library app_facade;

import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart';
import 'package:intl/intl.dart';
import 'package:uuid/uuid.dart';

// generated
part 'src/generated/assay.dart';
part 'src/generated/customer_info.dart';
part 'src/generated/customer_status.dart';
part 'src/generated/health_card.dart';
part 'src/generated/health_info.dart';
part 'src/generated/health_record.dart';
//part 'src/generated/strip_sample.dart';
//part 'src/generated/tube_sample.dart';
part 'src/generated/trial.dart';
part 'src/generated/trial_participant.dart';
part 'src/generated/trial_session.dart';
part 'src/generated/user.dart';
part 'src/generated/organization.dart';

// handmade models
part 'src/model/sample.dart';

// api
part 'src/api/account_api.dart';
part 'src/api/assay_api.dart';
part 'src/api/consent_api.dart';
part 'src/api/gateway_api.dart';
part 'src/api/health_api.dart';
part 'src/api/inventory_api.dart';
part 'src/api/sample_api.dart';
part 'src/api/trial_api.dart';
part 'src/api/user_api.dart';

// api impl
part 'src/api/impl/account_api_base.dart';
part 'src/api/impl/assay_api_base.dart';
part 'src/api/impl/consent_api_base.dart';
part 'src/api/impl/gateway_api_base.dart';
part 'src/api/impl/health_api_base.dart';
part 'src/api/impl/inventory_api_base.dart';
part 'src/api/impl/sample_api_base.dart';
part 'src/api/impl/trial_api_base.dart';
part 'src/api/impl/user_api_base.dart';

// api mock
part 'src/api/mock/_mock_data.dart';
part 'src/api/mock/account_api_mock.dart';
part 'src/api/mock/assay_api_mock.dart';
part 'src/api/mock/consent_api_mock.dart';
part 'src/api/mock/gateway_api_mock.dart';
part 'src/api/mock/health_api_mock.dart';
part 'src/api/mock/inventory_api_mock.dart';
part 'src/api/mock/sample_api_mock.dart';
part 'src/api/mock/trial_api_mock.dart';
part 'src/api/mock/user_api_mock.dart';

// common utils
part 'src/common/clone_utils.dart';
part 'src/common/error_info.dart';
part 'src/common/mapping_utils.dart';
part 'src/common/metric_utils.dart';
part 'src/common/platform_utils.dart';
part 'src/common/time_utils.dart';
part 'src/common/period_utils.dart';

// common model
part 'src/common/model/enum_wrapper.dart';
part 'src/common/model/country_enum.dart';
part 'src/common/model/enums.dart';
part 'src/common/model/user_data.dart';

// service
part 'src/service/api_client.dart';
part 'src/service/security_service.dart';
part 'src/service/url_builder.dart';
part 'src/service/impl/security_service_base.dart';
part 'src/service/mock/security_service_mock.dart';

// test
part 'src/testdata/test_data_injection.dart';
part 'src/testdata/impl/api_client_test_data_impl.dart';
part 'src/testdata/impl/gateway_api_test_data_impl.dart';
part 'src/testdata/impl/health_api_test_data_impl.dart';
part 'src/testdata/impl/security_service_test_data_impl.dart';
part 'src/testdata/impl/test_data_url_builder_.dart';
part 'src/testdata/impl/user_api_test_data_impl.dart';
part 'src/testdata/impl/sample_api_test_data_impl.dart';
