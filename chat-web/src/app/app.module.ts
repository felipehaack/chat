import {NgModule} from '@angular/core'
import {FormsModule, ReactiveFormsModule} from '@angular/forms'
import {HttpModule} from '@angular/http'
import {BrowserModule} from '@angular/platform-browser'
import {BrowserAnimationsModule} from '@angular/platform-browser/animations'
import {RouterModule, Routes} from '@angular/router'
import {AppComponent} from './app.component'
import {RESTService} from 'services/rest.service'
import {MdButtonModule, MdInputModule, MdCardModule, MdIconModule} from '@angular/material'

const appRoutes: Routes = []

@NgModule({
  declarations: [
    AppComponent
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
    RESTService
  ],
  bootstrap: [AppComponent]
})

export class AppModule {
}
