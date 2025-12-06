import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AlbumListComponent } from './components/album-list/album-list.component';
import { AlbumFormComponent } from './components/album-form/album-form.component';
import { AlbumDetailsComponent } from './components/album-details/album-details.component';
import { SongFormComponent } from './components/song-form/song-form.component';
import { SongDetailsComponent } from './components/song-details/song-details.component';

export const routes: Routes = [
  { path: '', redirectTo: '/albums', pathMatch: 'full' },
  { path: 'albums', component: AlbumListComponent },
  { path: 'albums/add', component: AlbumFormComponent },
  { path: 'albums/edit/:albumId', component: AlbumFormComponent },
  { path: 'albums/:albumId', component: AlbumDetailsComponent },
  { path: 'albums/:albumId/songs/add', component: SongFormComponent },
  { path: 'albums/:albumId/songs/edit/:songId', component: SongFormComponent },
  { path: 'albums/:albumId/songs/:songId', component: SongDetailsComponent },
  { path: '**', redirectTo: '/albums' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
