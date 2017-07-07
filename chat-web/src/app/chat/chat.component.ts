import {Component, OnInit, OnDestroy, ViewChild, ElementRef} from '@angular/core'
import {LoginService} from 'services/login.service'
import {RetrieveMessage, SendMessage, Message} from 'models/message.model'
import {ChatService} from 'services/chat.service'
import {Email} from 'models/email.model'
import {Router} from '@angular/router'
import {Response} from '@angular/http'

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {

  @ViewChild("content") content: ElementRef

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
          (response: Response) => {
            let isScroll = this.scrollBottom()

            this.concatMessages(response.json())

            if (isScroll)
              this.scrollToBottom()
          },
          (errors: Response) => "" //TODO - Improve errors method
        )
    }, 3000)
  }

  submit() {

    this.sendMessage = this.createSendMessage()

    this.message.text = ''

    this.chatService.deliveryMessage(this.sendMessage)
      .subscribe(
        (response: Response) => {

          let isScroll = this.scrollBottom()

          this.concatMessages([response.json()])

          if (isScroll)
            this.scrollToBottom()
        },
        (errors: Response) => "" //TODO - Improve errors method
      )
  }

  private scrollBottom(): boolean {
    let element = this.content.nativeElement
    return element.scrollHeight - element.scrollTop === element.clientHeight;
  }

  private scrollToBottom() {
    let element = this.content.nativeElement
    element.scrollTop = element.scrollHeight
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
