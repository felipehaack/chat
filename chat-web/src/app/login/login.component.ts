import {Component} from '@angular/core'
import {Router} from '@angular/router'
import {LoginService} from 'services/login.service'
import {Email} from 'models/email.model'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  origin: string
  destination: string

  constructor(private router: Router,
              private loginService: LoginService) {
  }

  next() {

    let email: Email = {
      origin: this.origin,
      destination: this.destination
    }

    this.loginService.setEmail(email)

    this.router.navigate(['/chat'])
  }
}