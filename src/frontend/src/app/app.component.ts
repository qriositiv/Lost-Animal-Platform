import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './components/shared/header/header.component';
import { FooterComponent } from './components/shared/footer/footer.component';
import { SpinnerComponent } from './components/shared/spinner/spinner.component';
import { HammerModule } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent, HttpClientModule, SpinnerComponent, HammerModule],
  templateUrl: './app.component.html',
})
export class AppComponent {
  title = 'StraySafe';
}
