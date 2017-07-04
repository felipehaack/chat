import {Observable} from 'rxjs'
import {Injectable} from '@angular/core'
import {BaseRequestOptions, RequestOptions, Http, Response} from '@angular/http'
import {environment} from 'environments/environment'

@Injectable()
export class RESTService extends BaseRequestOptions {

  private baseUrl: String = environment.URL

  constructor(protected http: Http) {
    super()
  }

  get(url: string, options?: RequestOptions): Observable<Response> {
    return this.http.get(this.baseUrl + url, options)
  }

  post(url: string, body: any, options?: RequestOptions): Observable<Response> {
    return this.http.post(this.baseUrl + url, body, options)
  }

  put(url: string, body: any, options?: RequestOptions): Observable<Response> {
    return this.http.put(this.baseUrl + url, body, options)
  }

  delete(url: string, options?: RequestOptions): Observable<Response> {
    return this.http.delete(this.baseUrl + url, options)
  }
}
