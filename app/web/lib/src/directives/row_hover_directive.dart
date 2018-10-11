import 'dart:html';

import 'package:angular/angular.dart';

@Directive(selector: '[rowHover]')
class RowHoverDirective implements OnInit {
  final Element _el;
  String _rowClass;

  @Input('rowHover')
  dynamic row;

  RowHoverDirective(this._el);

  @HostListener('mouseenter')
  void onMouseEnter() {
    _highlight('#efefef');
  }

  @HostListener('mouseleave')
  void onMouseLeave() {
    _highlight();
  }

  void _highlight([String color]) {
    var rowSelector = ".$_rowClass";
    var items = _el.parent.querySelectorAll(rowSelector);
    //print("color $color row [$rowSelector] items ${items.length}");
    items.style.backgroundColor = color;
  }
  @override
  ngOnInit() {
    _rowClass = "Rtable-row-$row";
    _el.classes.add(_rowClass);
  }
}