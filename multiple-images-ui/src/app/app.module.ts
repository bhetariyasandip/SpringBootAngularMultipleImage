import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { UploadFilesComponent } from './components/upload-files/upload-files.component';
// Import the library
import { NgxImageZoomModule } from 'ngx-image-zoom';

@NgModule({
  declarations: [AppComponent, UploadFilesComponent],
  imports: [BrowserModule, HttpClientModule, NgxImageZoomModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
