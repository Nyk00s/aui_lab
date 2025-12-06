import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Album } from '../../models/album.model';
import { Song } from '../../models/song.model';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-album-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './album-details.component.html'
})
export class AlbumDetailsComponent implements OnInit {
  album?: Album;
  songs: Song[] = [];
  albumId?: string;

  constructor(private api: ApiService, private route: ActivatedRoute, private router: Router, private cd: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(pm => {
      const albumId = pm.get('albumId');
      if (albumId) {
        this.albumId = albumId;
        this.loadAll(albumId);
      }
    });
  }

  loadAll(id: string) {
    this.api.getAlbum(id).subscribe({
      next: a => { this.album = a; this.cd.detectChanges(); },
      error: () => alert('Error while fetching album')
    });

    this.api.getAlbumSongs(id).subscribe({
      next: s => { this.songs = s; this.cd.detectChanges(); },
      error: () => alert('Error while fetching album songs')
    });
  }

  removeSong(song: Song) {
    if (!this.albumId) return;
    if (!confirm(`Delete song: "${song.name}" ?`)) return;
    this.api.deleteSong(song.id!).subscribe({
      next: () => this.loadAll(this.albumId!),
      error: () => alert('Error while fetching songs')
    });
  }

  addSong() {
    this.router.navigate(['/albums', this.albumId, 'songs', 'add']);
  }

  editSong(song: Song) {
    this.router.navigate(['/albums', this.albumId, 'songs', 'edit', song.id]);
  }

  viewSong(song: Song) {
    this.router.navigate(['/albums', this.albumId, 'songs', song.id]);
  }

  back() {
    this.router.navigate(['/albums']);
  }
}
