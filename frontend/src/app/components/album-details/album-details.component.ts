import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Album } from '../../models/album.model';
import { Song } from '../../models/song.model';

@Component({
  selector: 'app-album-details',
  templateUrl: './album-details.component.html'
})
export class AlbumDetailsComponent implements OnInit {
  album?: Album;
  songs: Song[] = [];
  albumId?: string;

  constructor(private api: ApiService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(pm => {
      const id = pm.get('id');
      if (id) {
        this.albumId = id;
        this.loadAll(id);
      }
    });
  }

  loadAll(id: string) {
    this.api.getAlbum(id).subscribe({
      next: c => this.album = c,
      error: () => alert('Error while fetching album')
    });

    this.api.getAlbumSongs(id).subscribe({
      next: e => this.songs = e,
      error: () => alert('Error while fetching album songs')
    });
  }

  removeSong(el: Song) {
    if (!this.albumId) return;
    if (!confirm(`Delete song: "${el.name}" ?`)) return;
    this.api.deleteSong(el.id!).subscribe({
      next: () => this.loadAll(this.albumId!),
      error: () => alert('Error while fetching songs')
    });
  }

  addSong() {
    this.router.navigate(['/albums', this.albumId, 'songs', 'add']);
  }

  editSong(el: Song) {
    this.router.navigate(['/albums', this.albumId, 'songs', 'edit', el.id]);
  }

  viewSong(el: Song) {
    this.router.navigate(['/album', this.albumId, 'songs', el.id]);
  }

  back() {
    this.router.navigate(['/album']);
  }
}
