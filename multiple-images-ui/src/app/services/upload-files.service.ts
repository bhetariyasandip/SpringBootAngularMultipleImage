import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UploadFilesService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  upload(file: File): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    formData.append('file', file);

    const req = new HttpRequest(
      'POST',
      `${this.baseUrl}/uploadImages`,
      formData,
      {
        reportProgress: true,
        responseType: 'json',
      }
    );

    return this.http.request(req);
  }

  getFiles(): Observable<any> {
    return this.http.get(`${this.baseUrl}/files`);
  }
  deleteImage(fileName: String): Promise<any> {
    // this.http.get(`${this.baseUrl}/deleteFiles/`+filename);
    return this.http
      .delete(`${this.baseUrl}/deleteFile/` + fileName)
      .toPromise()
      .then((response) => response)
      .catch(this.handleError);
  }

  private handleError(error: any) {
    console.error('Some error occured', error);
    return Promise.reject(error.message || error);
  }
}
