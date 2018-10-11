import 'package:angular/angular.dart';

@Component(
  selector: 'trial-process',
  templateUrl: 'trial_process_component.html',
  styleUrls: const ['trial_process_component.css'],
  directives: const [
    CORE_DIRECTIVES,
  ],
)
class TrialProcessComponent {
  @Input() String theme = '';
}
