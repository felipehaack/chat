import {Injectable} from '@angular/core'
import {Email} from 'models/email.model'

@Injectable()
export class LoginService {

  email: Email

  setEmail(email: Email) {
    this.email = email
  }
}