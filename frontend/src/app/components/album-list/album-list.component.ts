import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Album } from '../../models/album.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-album-list',
  templateUrl: './album-list.component.html'
})
export class AlbumListComponent implements OnInit {
  album: Album[] = [];
  loading = false;
  error = '';

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.load();
  }

  load() {
    this.loading = true;
    this.api.getAlbums().subscribe({
      next: data => { this.album = data; this.loading = false; },
      error: err => { this.error = 'Error while getting album'; this.loading = false; }
    });
  }

  goAdd() {
    this.router.navigate(['/album/add']);
  }

  edit(cat: Album) {
    this.router.navigate(['/album/edit', cat.id]);
  }

  viewDetails(cat: Album) {
    this.router.navigate(['/album', cat.id]);
  }

  remove(cat: Album) {
    if (!confirm(`Delete Album "${cat.name}" ?`)) return;
    this.api.deleteAlbum(cat.id!).subscribe({
      next: () => this.load(),
      error: () => alert('error while deleting')
    });
  }
}