import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_web/src/pages/site/navbar_service.dart';

@Component(
  selector: 'navbar',
  styleUrls: const ['navbar_component.css'],
  templateUrl: 'navbar_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES
  ],
)
class NavBarComponent {
  final NavBarService navBarService;
  final Router _router;

  @ViewChild('drawer') MaterialTemporaryDrawerComponent drawer;

  String get currentPage => navBarService.currentPage;

  NavBarComponent(this._router, this.navBarService);

  navigateTo(List<dynamic> url) {
    _router.navigate(url);
    drawer.toggle();
  }
}
