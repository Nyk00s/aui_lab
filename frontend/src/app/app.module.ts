import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';


import { AlbumListComponent } from './components/album-list/album-list.component';
import { AlbumFormComponent } from './components/album-form/album-form.component';
import { AlbumDetailsComponent } from './components/album-details/album-details.component';
import { SongFormComponent } from './components/song-form/song-form.component';
import { SongDetailsComponent } from './components/song-details/song-details.component';

@NgModule({
  declarations: [
    AppComponent,
    AlbumListComponent,
    AlbumFormComponent,
    AlbumDetailsComponent,
    SongFormComponent,
    SongDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
