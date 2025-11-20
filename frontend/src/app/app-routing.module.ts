import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AlbumListComponent } from './components/album-list/album-list.component';
import { AlbumFormComponent } from './components/album-form/album-form.component';
import { AlbumDetailsComponent } from './components/album-details/album-details.component';
import { SongFormComponent } from './components/song-form/song-form.component';
import { SongDetailsComponent } from './components/song-details/song-details.component';

const routes: Routes = [
  { path: '', redirectTo: '/albums', pathMatch: 'full' },

  // 1. List all categories
  { path: 'albums', component: AlbumListComponent },

  // 2. Add new Album
  { path: 'albums/add', component: AlbumFormComponent },

  // 3. Edit existing Album (path param id)
  { path: 'albums/edit/:id', component: AlbumFormComponent },

  // 4. Album details + Songs list
  { path: 'albums/:id', component: AlbumDetailsComponent },

  // 5. Add new Song for Album (catId)
  { path: 'albums/:id/songs/add', component: SongFormComponent },

  // 6. Edit existing Song (catId + elId)
  { path: 'albums/:catId/songs/edit/:elId', component: SongFormComponent },

  // 7. Song details
  { path: 'albums/:catId/songs/:elId', component: SongDetailsComponent },

  // fallback
  { path: '**', redirectTo: '/albums' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
