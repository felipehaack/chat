import {Email} from './email.model'

export interface Message {
  text: string
}

export interface SendMessage {
  email: Email,
  message: Message
}

export interface RetrieveMessage {
  created: Date,
  email: Email,
  message: Message
}