import {NgModule} from '@angular/core'
import {FormsModule, ReactiveFormsModule} from '@angular/forms'
import {HttpModule} from '@angular/http'
import {BrowserModule} from '@angular/platform-browser'
import {BrowserAnimationsModule} from '@angular/platform-browser/animations'
import {RouterModule, Routes} from '@angular/router'
import {AppComponent} from './app.component'
import {RESTService} from 'services/rest.service'
import {MdButtonModule, MdInputModule, MdCardModule, MdIconModule} from '@angular/material'
import {LoginComponent} from './login/login.component'
import {ChatComponent} from './chat/chat.component'
import {LoginService} from 'services/login.service'
import {ChatService} from 'services/chat.service'
import {OrderBy} from 'pipes/orderby'

const appRoutes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'chat',
    component: ChatComponent
  },
  {
    path: '**',
    redirectTo: '/login'
  }
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ChatComponent,
    OrderBy
  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpModule,
    MdButtonModule,
    MdInputModule,
    MdCardModule,
    MdIconModule,
    RouterModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    RESTService,
    LoginService,
    ChatService
  ],
  bootstrap: [AppComponent]
})

export class AppModule {
}
