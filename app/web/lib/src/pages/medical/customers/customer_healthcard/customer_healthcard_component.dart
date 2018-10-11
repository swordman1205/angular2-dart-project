import 'dart:async';
import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:app_facade/app_facade.dart';

class CustomerHealthCardComponent implements OnInit {
  final HealthApi _healthApi;

  @Input()
  CustomerInfo customer;

  //cards list
  Iterable<HealthCard> cards = [];

  //car type select;
  ItemRenderer<EnumWrapper> itemRenderer =
  new CachingItemRenderer<EnumWrapper>((enumWrapper) => "${enumWrapper.caption}");
  StringSelectionOptions<EnumWrapper> cardTypeOptions =
  new StringSelectionOptions<EnumWrapper>(Enums.cardTypes);
  SelectionModel<EnumWrapper> cardTypeSelection =
  new SelectionModel<EnumWrapper>.withList(allowMulti: false);

  String get cardTypeSelectedLabel =>
      cardTypeSelection.selectedValues.length > 0
          ? itemRenderer(cardTypeSelection.selectedValues.first)
          : 'Select card type';

  HealthCard card = new HealthCard();
  bool showTypeRequired = false;
  bool showInfoDialog = false;
  String createCardError;

  CustomerHealthCardComponent(this._healthApi);

  @override
  Future<Null> ngOnInit() async {
    updateCards();
    if (card.type != null) {
      cardTypeSelection.select(cardTypeOptions.optionsList.firstWhere((ew) => ew.name == card.type, orElse: () => null));
    }
  }

  Future<Null> updateCards() async {
    cards = await _healthApi.getCards(customer.userId);
  }

  Future<Null> createCard() async {
    if (cardTypeSelection.selectedValues.isEmpty) {
      showTypeRequired = true;
    } else {
      card.type = cardTypeSelection.selectedValues.first.name;
      card.userId = customer.userId;
      try {
        await _healthApi.createCard(card);
        showInfoDialog = true;
        card = new HealthCard();
        cardTypeSelection.clear();
        createCardError = null;
        updateCards();
      } catch (e) {
        createCardError = e.message;
      }
    }
  }
}