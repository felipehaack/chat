import {Injectable} from '@angular/core'
import {SendMessage} from 'models/message.model'
import {Observable} from 'rxjs'
import {Response} from '@angular/http'
import {RESTService} from './rest.service'

@Injectable()
export class ChatService {

  constructor(private rest: RESTService) {

  }

  deliveryMessage(send: SendMessage): Observable<Response> {
    return this.rest.post('/message', send)
  }

  retrieveMessages(email: string): Observable<Response> {
    return this.rest.get(`/message/${email}`)
  }
}