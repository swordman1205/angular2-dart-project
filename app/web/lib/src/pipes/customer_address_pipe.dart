import 'package:angular/angular.dart';
import 'package:app_facade/app_facade.dart';

@Pipe('customerAddress')
class CustomerAddressPipe extends PipeTransform {

  String transform(CustomerInfo value) {
    var country = CountryEnum.find(value.country).caption;
    return "${country} ${value.addressLine} ${value.city} ${value.zip} ${value.state}";
  }

}