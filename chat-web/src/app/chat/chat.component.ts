import {Component, OnInit, OnDestroy} from '@angular/core'
import {LoginService} from 'services/login.service'
import {RetrieveMessage, SendMessage, Message} from 'models/message.model'
import {ChatService} from 'services/chat.service'
import {Response} from '@angular/http'
import {Email} from 'models/email.model'
import {Router} from '@angular/router'

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {

  private email: Email
  private retrieveInterval: any

  sendMessage: SendMessage
  message: Message = {text: ''}
  retrievedMessages: RetrieveMessage[] = []

  constructor(private router: Router,
              private chatService: ChatService,
              private loginService: LoginService) {

    this.email = this.loginService.email

    this.retrieveInterval = setInterval(() => {
      this.chatService.retrieveMessages(this.email.origin)
        .subscribe(
          (response: Response) => this.concatMessages(response.json()),
          (errors: Response) => "" //TODO - Improve errors method
        )
    }, 3000)
  }

  submit() {
    this.sendMessage = this.createSendMessage()

    this.message.text = ''

    this.chatService.deliveryMessage(this.sendMessage)
      .subscribe(
        (response: Response) => this.concatMessages([response.json()]),
        (errors: Response) => "" //TODO - Improve errors method
      )
  }

  private createSendMessage(): SendMessage {
    return {
      email: this.email,
      message: {
        text: this.message.text
      }
    }
  }

  private concatMessages(retrieve: RetrieveMessage[]) {
    if (retrieve.length > 0) {
      this.retrievedMessages = this.retrievedMessages.concat(retrieve)
    }
  }

  ngOnInit() {
    if (!this.loginService.email)
      this.router.navigate([''])
  }

  ngOnDestroy() {
    clearInterval(this.retrieveInterval)
  }
}
